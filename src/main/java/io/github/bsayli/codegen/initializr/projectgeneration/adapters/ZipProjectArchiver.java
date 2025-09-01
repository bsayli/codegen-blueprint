package io.github.bsayli.codegen.initializr.projectgeneration.adapters;

import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectArchiver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

@Component("zipProjectArchiver")
public class ZipProjectArchiver implements ProjectArchiver {

  @Override
  public Path archiveProject(File projectDestination, String projectName) throws IOException {
    projectName = sanitizeFilename(projectName);
    String archiveFilename = projectName + ".zip";
    File archiveFile = new File(projectDestination.getParent(), archiveFilename);

    try (FileOutputStream fos = new FileOutputStream(archiveFile);
        ZipOutputStream zipOut = new ZipOutputStream(fos)) {

      zipOut.setLevel(ZipOutputStream.DEFLATED);

      Path projectPath = Paths.get(projectDestination.getAbsolutePath());

      addFilesToZip(projectPath, zipOut);
    }

    return archiveFile.toPath();
  }

  private void addFilesToZip(Path projectPath, ZipOutputStream zipOut) throws IOException {
    String rootFileName = sanitizeRootFileName(projectPath.getFileName().toString());
    List<String> processingErrors = new ArrayList<>();
    try (Stream<Path> walkStream = Files.walk(projectPath)) {
      walkStream.forEach(
          filePath -> {
            String entryName = getEntryName(projectPath, filePath, rootFileName);
            try {
              addFileToZip(zipOut, filePath, entryName);
            } catch (IOException e) {
              processingErrors.add(
                  String.format("Error processing file: %s. Reason: %s", filePath, e.getMessage()));
            }
          });
    }

    if (!processingErrors.isEmpty()) {
      throw new IOException(
          String.format(
              "Error encountered during archive creation for %s: %s",
              projectPath.getFileName(), String.join(", ", processingErrors)));
    }
  }

  private String getEntryName(Path projectPath, Path filePath, String rootFileName) {
    if (projectPath.equals(filePath)) {
      return rootFileName;
    } else {
      String entryName = sanitizeEntryName(projectPath.relativize(filePath).toString());
      return "/" + rootFileName + "/" + entryName;
    }
  }

  private void addFileToZip(ZipOutputStream zipOut, Path filePath, String entryName)
      throws IOException {
    if (Files.isDirectory(filePath)) {
      zipOut.putNextEntry(new ZipEntry(entryName + "/"));
    } else {
      zipOut.putNextEntry(new ZipEntry(entryName));
      try (FileInputStream fis = new FileInputStream(filePath.toFile())) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) != -1) {
          zipOut.write(buffer, 0, bytesRead);
        }
      }
    }
  }

  private String sanitizeFilename(String filename) {
    return filename.replaceAll("[/:*?<>|\\\\.^]", "").replaceAll("--+", "-");
  }

  private String sanitizeEntryName(String filename) {
    return filename.replaceAll("[:*?<>|\\\\^]", "").replaceAll("-+", "_");
  }

  private String sanitizeRootFileName(String filename) {
    return filename.replaceAll("[:*?<>|\\\\.^]", "").replaceAll("--+", "-");
  }
}
