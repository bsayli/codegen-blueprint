package io.github.bsayli.codegen.initializr.projectgeneration.adapters;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ZipProjectArchiverTest {

  @TempDir public Path tempFolder;
  @Autowired private ZipProjectArchiver zipProjectArchiver;

  @Test
  void testArchiveProject_CreatesArchiveFile() throws IOException {
    String projectName = "codegen-demo";
    Path projectDir = tempFolder.resolve(projectName);
    Files.createDirectories(projectDir);

    File testFile1 = tempFolder.resolve(projectDir.toString() + "/demo-01.txt").toFile();
    boolean created1 = testFile1.createNewFile();
    assertTrue(created1 || testFile1.exists(), "demo-01.txt could not be created");

    File testFile2 = tempFolder.resolve(projectDir.toString() + "/folder-04/file-02.txt").toFile();
    File parent2 = testFile2.getParentFile();
    boolean mkParent2 = parent2.mkdirs();
    assertTrue(mkParent2 || parent2.exists(), "folder-04 could not be created");
    boolean created2 = testFile2.createNewFile();
    assertTrue(created2 || testFile2.exists(), "file-02.txt could not be created");

    Path archivedProjectPath = zipProjectArchiver.archiveProject(projectDir.toFile(), projectName);
    File archivedProjectFile = archivedProjectPath.toFile();
    assertTrue(archivedProjectFile.exists(), "Archive project file should be created");
  }

  @Test
  void testArchiveProject_CreatesArchiveAndExtractsContent() throws IOException {
    String projectName = "codegen-enterprise";
    Path projectDir = tempFolder.resolve(projectName);
    Files.createDirectories(projectDir);

    String enterprise01FileName = "enterprise-01.txt";
    File testFile1 =
        tempFolder.resolve(projectDir.toString() + "/" + enterprise01FileName).toFile();
    boolean created1 = testFile1.createNewFile();
    assertTrue(created1 || testFile1.exists(), enterprise01FileName + " could not be created");

    File testFile2 = tempFolder.resolve(projectDir.toString() + "/folder-01/file-01.txt").toFile();
    File parent2 = testFile2.getParentFile();
    boolean mkParent2 = parent2.mkdirs();
    assertTrue(mkParent2 || parent2.exists(), "folder-01 could not be created");
    boolean created2 = testFile2.createNewFile();
    assertTrue(created2 || testFile2.exists(), "file-01.txt could not be created");

    Path archivedProjectPath = zipProjectArchiver.archiveProject(projectDir.toFile(), projectName);
    File archivedProjectFile = archivedProjectPath.toFile();
    assertTrue(archivedProjectFile.exists(), "Archive project file should be created");

    File extractedDir = createExtractedProject(projectDir, archivedProjectFile);
    assertTrue(
        extractedDir.exists() && !FileUtils.isEmptyDirectory(extractedDir),
        "Archived file was corrupted!");

    String archivedProjectName = archivedProjectFile.getName().replace(".zip", "");
    File extractedProjectDir = new File(extractedDir, archivedProjectName);
    String enterprise01FileNameFromArchived = "enterprise_01.txt";
    File enterprise01FileFromUnarchived =
        new File(extractedProjectDir, enterprise01FileNameFromArchived);
    assertTrue(
        enterprise01FileFromUnarchived.exists(),
        "Archived project file does not contain " + enterprise01FileNameFromArchived);
  }

  @Test
  void testArchiveProject_SuccessInEmptyDirectoryCreation() throws IOException {
    String projectName = "codegen-demo-empty";
    Path projectDir = tempFolder.resolve(projectName);
    Files.createDirectories(projectDir);

    Path archivedProjectPath = zipProjectArchiver.archiveProject(projectDir.toFile(), projectName);
    File archivedProjectFile = archivedProjectPath.toFile();

    assertTrue(
        archivedProjectFile.exists(),
        "Archived project file should be created even for empty directory");
  }

  @Test
  void testArchiveProject_InvalidProjectFilePath_IOException() throws IOException {
    String projectName = "codegen-demo";
    Path invalidProjectDir = Paths.get(tempFolder.toString(), "invalid/path");

    try {
      zipProjectArchiver.archiveProject(invalidProjectDir.toFile(), projectName);
      fail("Expected an IOException for invalid project directory");
    } catch (IOException e) {
      assertTrue(
          e.getMessage() != null && e.getMessage().contains("No such file"),
          "Unexpected exception message: " + e.getMessage());
    }
  }

  @Test
  void testArchiveProject_CorruptedFile_IOException() throws IOException {
    String projectName = "codegen-demo";
    Path projectDir = tempFolder.resolve(projectName);
    Files.createDirectories(projectDir);

    File testFile1 = tempFolder.resolve(projectDir.toString() + "/demo-01.txt").toFile();
    boolean created1 = testFile1.createNewFile();
    assertTrue(created1 || testFile1.exists(), "demo-01.txt could not be created");

    File testFile2 = tempFolder.resolve(projectDir.toString() + "/folder-04/file-02.txt").toFile();
    File parent2 = testFile2.getParentFile();
    boolean mkParent2 = parent2.mkdirs();
    assertTrue(mkParent2 || parent2.exists(), "folder-04 could not be created");
    boolean created2 = testFile2.createNewFile();
    assertTrue(created2 || testFile2.exists(), "file-02.txt could not be created");

    File corruptedFile = tempFolder.resolve(projectDir.toString() + "/corrupted-file.txt").toFile();
    boolean createdCorrupted = corruptedFile.createNewFile();
    assertTrue(
        createdCorrupted || corruptedFile.exists(), "corrupted-file.txt could not be created");
    try (OutputStream corruptedOutputStream = new FileOutputStream(corruptedFile)) {
      corruptedOutputStream.write("This is a corrupted file!".getBytes());
      corruptedOutputStream.write(new byte[] {1, 2, 3, -12});
    }

    // Try to remove permissions; accept success OR already-in-effect states.
    boolean execUnset = corruptedFile.setExecutable(false, false);
    assertTrue(
        execUnset || !corruptedFile.canExecute(),
        "Failed to unset execute permission on corrupted file");

    boolean readUnset = corruptedFile.setReadable(false, false);
    assertTrue(
        readUnset || !corruptedFile.canRead(), "Failed to unset read permission on corrupted file");

    boolean writeUnset = corruptedFile.setWritable(false, false);
    assertTrue(
        writeUnset || !corruptedFile.canWrite(),
        "Failed to unset write permission on corrupted file");

    try {
      zipProjectArchiver.archiveProject(projectDir.toFile(), projectName);
      fail("Expected an IOException for archiving corrupted file");
    } catch (IOException e) {
      assertTrue(
          e.getMessage() != null && e.getMessage().contains("Error processing file"),
          "Unexpected exception message: " + e.getMessage());
    }
  }

  private File createExtractedProject(Path projectDir, File archivedFile) throws IOException {
    File extractedDir = tempFolder.resolve(projectDir.getParent() + "/unarchived").toFile();
    boolean mkExtracted = extractedDir.mkdirs();
    assertTrue(mkExtracted || extractedDir.exists(), "unarchived directory could not be created");

    try (ZipInputStream zis = new ZipInputStream(new FileInputStream(archivedFile))) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (entry.isDirectory()) {
          File directory = new File(extractedDir, entry.getName());
          boolean mk = directory.mkdirs();
          assertTrue(mk || directory.exists(), "Failed to create directory: " + directory);
        } else {
          File file = new File(extractedDir, entry.getName());
          File parent = file.getParentFile();
          boolean mk = parent.mkdirs();
          assertTrue(mk || parent.exists(), "Failed to create parent directory: " + parent);

          try (OutputStream outputStream = new FileOutputStream(file)) {
            zis.transferTo(outputStream);
          }
        }
        zis.closeEntry();
      }
    }

    return extractedDir;
  }
}
