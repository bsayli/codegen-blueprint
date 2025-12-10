package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("integration")
@Tag("cli-e2e")
class SpringBootHexagonalBasicCliIT {

  @TempDir Path tempDir;
  @Autowired private CodegenCliExecutor cliExecutor;

  private static List<String> zipEntries(ZipFile zipFile) {
    Enumeration<? extends ZipEntry> entries = zipFile.entries();
    List<String> names = new java.util.ArrayList<>();
    while (entries.hasMoreElements()) {
      names.add(entries.nextElement().getName());
    }
    return names;
  }

  private static String readTextEntry(ZipFile zipFile, ZipEntry entry) throws IOException {
    try (InputStream in = zipFile.getInputStream(entry)) {
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Test
  @DisplayName(
      "CLI springboot hexagonal basic (web + data_jpa) should generate project archive with core files and expected content")
  void cli_springboot_hexagonal_basic_shouldGenerateProjectArchive() throws IOException {
    // given
    String[] args = {
      "springboot",
      "--group-id",
      "io.github.blueprintplatform.samples",
      "--artifact-id",
      "greeting-service",
      "--name",
      "Greeting Service",
      "--description",
      "Sample Greeting Service project",
      "--package-name",
      "io.github.blueprintplatform.samples.greeting",
      "--layout",
      "hexagonal",
      "--sample-code",
      "basic",
      "--dependency",
      "web",
      "--dependency",
      "data_jpa",
      "--target-dir",
      tempDir.toString()
    };

    // when
    int exitCode = cliExecutor.execute(args);

    // then
    assertThat(exitCode).isZero();

    Path archivePath = tempDir.resolve("greeting-service.zip");
    assertThat(Files.exists(archivePath))
        .as("Archive should be created at target-dir/greeting-service.zip")
        .isTrue();

    try (ZipFile zipFile = new ZipFile(archivePath.toFile())) {
      List<String> entryNames = zipEntries(zipFile);

      assertThat(entryNames).contains("greeting-service/");

      assertThat(entryNames)
          .as("pom.xml should be present at root of project")
          .contains("greeting-service/pom.xml");
      assertThat(entryNames)
          .as("application.yml should be present under src/main/resources")
          .contains("greeting-service/src/main/resources/application.yml");
      assertThat(entryNames)
          .as("README.md should be present at root of project")
          .contains("greeting-service/README.md");

      assertThat(entryNames)
          .anySatisfy(
              name ->
                  assertThat(name)
                      .startsWith(
                          "greeting-service/src/main/java/io/github/blueprintplatform/samples/greeting/")
                      .endsWith("Application.java"));

      assertThat(entryNames)
          .anySatisfy(
              name ->
                  assertThat(name)
                      .startsWith(
                          "greeting-service/src/test/java/io/github/blueprintplatform/samples/greeting/")
                      .endsWith("ApplicationTests.java"));

      assertThat(entryNames)
          .contains(
              "greeting-service/src/main/java/io/github/blueprintplatform/samples/greeting/adapter/sample/greeting/in/rest/GreetingController.java");

      ZipEntry pomEntry = zipFile.getEntry("greeting-service/pom.xml");
      assertThat(pomEntry).isNotNull();
      String pomXml = readTextEntry(zipFile, pomEntry);

      assertThat(pomXml)
          .contains("<groupId>io.github.blueprintplatform.samples</groupId>")
          .contains("<artifactId>greeting-service</artifactId>");

      assertThat(pomXml)
          .contains("spring-boot-starter")
          .contains("spring-boot-starter-web")
          .contains("spring-boot-starter-data-jpa")
          .contains("spring-boot-starter-test");

      ZipEntry appYamlEntry =
          zipFile.getEntry("greeting-service/src/main/resources/application.yml");
      assertThat(appYamlEntry).isNotNull();
      String appYaml = readTextEntry(zipFile, appYamlEntry);

      assertThat(appYaml)
          .contains("spring:")
          .contains("application:")
          .contains("name:")
          .contains("greeting-service");

      ZipEntry readmeEntry = zipFile.getEntry("greeting-service/README.md");
      assertThat(readmeEntry).isNotNull();
      String readme = readTextEntry(zipFile, readmeEntry);

      assertThat(readme)
          .contains("Greeting Service")
          .contains("io.github.blueprintplatform.samples.greeting");
    }
  }
}
