package io.github.bsayli.codegen.initializr.adapter.out.filesystem;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectArchiveIOException;
import io.github.bsayli.codegen.initializr.adapter.error.exception.ProjectArchiveInvalidRootException;
import io.github.bsayli.codegen.initializr.application.port.out.archive.ProjectArchiverPort;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileSystemProjectArchiverAdapter implements ProjectArchiverPort {

    private static final String ZIP_EXTENSION = ".zip";
    private static final char ZIP_SEPARATOR = '/';

    @Override
    public Path archive(Path projectRoot, String artifactId) {
        if (projectRoot == null) {
            throw new ProjectArchiveInvalidRootException(null);
        }

        Path parent = projectRoot.getParent();
        if (parent == null) {
            throw new ProjectArchiveInvalidRootException(projectRoot);
        }

        if (!Files.exists(projectRoot) || !Files.isDirectory(projectRoot)) {
            throw new ProjectArchiveInvalidRootException(projectRoot);
        }

        String baseName =
                (artifactId == null || artifactId.isBlank())
                        ? projectRoot.getFileName().toString()
                        : artifactId;

        Path archivePath = parent.resolve(baseName + ZIP_EXTENSION);

        try (ZipOutputStream zipOut = new ZipOutputStream(Files.newOutputStream(archivePath))) {
            writeDirectoryToZip(projectRoot, baseName, zipOut);
            return archivePath;
        } catch (IOException e) {
            throw new ProjectArchiveIOException(projectRoot, e);
        }
    }

    private void writeDirectoryToZip(Path root, String rootName, ZipOutputStream zos)
            throws IOException {
        Path normalizedRoot = root.toAbsolutePath().normalize();

        try (Stream<Path> paths = Files.walk(normalizedRoot)) {
            paths.forEachOrdered(
                    path -> {
                        try {
                            writeEntry(normalizedRoot, rootName, path, zos);
                        } catch (IOException e) {
                            throw new UncheckedIOException(e);
                        }
                    });
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    private void writeEntry(Path root, String rootName, Path current, ZipOutputStream zos)
            throws IOException {

        Path relative = root.relativize(current);
        StringBuilder entryName = new StringBuilder();
        entryName.append(rootName).append(ZIP_SEPARATOR);

        String rel = relative.toString();
        if (!rel.isEmpty()) {
            String fsSep = root.getFileSystem().getSeparator();
            if (!fsSep.equals(String.valueOf(ZIP_SEPARATOR))) {
                rel = rel.replace(fsSep, String.valueOf(ZIP_SEPARATOR));
            }
            entryName.append(rel);
        }

        boolean directory = Files.isDirectory(current);
        if (directory && entryName.charAt(entryName.length() - 1) != ZIP_SEPARATOR) {
            entryName.append(ZIP_SEPARATOR);
        }

        ZipEntry entry = new ZipEntry(entryName.toString());
        zos.putNextEntry(entry);

        if (!directory) {
            Files.copy(current, zos);
        }

        zos.closeEntry();
    }
}