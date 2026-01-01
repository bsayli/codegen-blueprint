package io.github.blueprintplatform.codegen.bootstrap.wiring.in.cli;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("integration")
@Tag("cli-e2e")
class SpringBootCliE2EIT {

  @TempDir Path tempDir;
  @Autowired private CodegenCliExecutor cliExecutor;

  static Stream<Arguments> scenarios() {
    return Stream.of(
        Arguments.of(true, "21", "3.5", "3.5.9", null),
        Arguments.of(true, "25", "3.5", "3.5.9", null),
        Arguments.of(true, "21", "3.4", "3.4.13", null),
        Arguments.of(false, "25", "3.4", "3.4.13", "platform.target.incompatible"));
  }

  private static String artifactId(String java, String boot) {
    String bootKey = boot.replace(".", "");
    return "greeting-j" + java + "-b" + bootKey;
  }

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

  private static CapturedIo captureIo(CodegenCliExecutor executor, String[] args) {
    ByteArrayOutputStream err = new ByteArrayOutputStream();
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    try (PrintStream errPs = new PrintStream(err, true, StandardCharsets.UTF_8);
        PrintStream outPs = new PrintStream(out, true, StandardCharsets.UTF_8)) {

      int exitCode = executor.execute(args, outPs, errPs);
      return new CapturedIo(
          exitCode, out.toString(StandardCharsets.UTF_8), err.toString(StandardCharsets.UTF_8));
    }
  }

  private static void assertSpringBootParentVersion(String pomXml, String expectedVersion) {
    assertThat(pomXml)
        .contains("<parent>")
        .contains("<groupId>org.springframework.boot</groupId>")
        .contains("<artifactId>spring-boot-starter-parent</artifactId>")
        .contains("<version>" + expectedVersion + "</version>");
  }

  @ParameterizedTest(name = "java={1}, boot={2} shouldSucceed={0}")
  @MethodSource("scenarios")
  @DisplayName(
      "CLI springboot should generate valid archives for supported targets and fail for incompatible target")
  void cli_springboot_matrix_shouldBehaveAsExpected(
      boolean shouldSucceed,
      String java,
      String boot,
      String expectedBootPatch,
      String expectedErrorCode)
      throws IOException {

    String artifactId = artifactId(java, boot);
    String packageName = "io.github.blueprintplatform." + artifactId.replace("-", "");

    String[] args = {
      "springboot",
      "--group-id",
      "io.github.blueprintplatform",
      "--artifact-id",
      artifactId,
      "--name",
      "Greeting",
      "--description",
      "Greeting sample built with hexagonal architecture",
      "--package-name",
      packageName,
      "--layout",
      "hexagonal",
      "--guardrails",
      "basic",
      "--sample-code",
      "basic",
      "--java",
      java,
      "--boot",
      boot,
      "--dependency",
      "web",
      "--dependency",
      "data_jpa",
      "--dependency",
      "actuator",
      "--target-dir",
      tempDir.toString()
    };

    CapturedIo io = captureIo(cliExecutor, args);

    Path archivePath = tempDir.resolve(artifactId + ".zip");

    if (shouldSucceed) {
      assertThat(io.exitCode()).isZero();

      assertThat(Files.exists(archivePath))
          .as("Archive should be created at target-dir/%s.zip".formatted(artifactId))
          .isTrue();

      try (ZipFile zipFile = new ZipFile(archivePath.toFile())) {
        List<String> entryNames = zipEntries(zipFile);

        assertThat(entryNames).contains(artifactId + "/");

        assertThat(entryNames).contains(artifactId + "/pom.xml");
        assertThat(entryNames).contains(artifactId + "/src/main/resources/application.yml");
        assertThat(entryNames).contains(artifactId + "/README.md");

        assertThat(entryNames)
            .anySatisfy(
                name ->
                    assertThat(name)
                        .startsWith(
                            artifactId + "/src/main/java/" + packageName.replace('.', '/') + "/")
                        .endsWith("Application.java"));

        assertThat(entryNames)
            .anySatisfy(
                name ->
                    assertThat(name)
                        .startsWith(
                            artifactId + "/src/test/java/" + packageName.replace('.', '/') + "/")
                        .endsWith("ApplicationTests.java"));

        ZipEntry pomEntry = zipFile.getEntry(artifactId + "/pom.xml");
        assertThat(pomEntry).isNotNull();
        String pomXml = readTextEntry(zipFile, pomEntry);

        assertThat(pomXml)
            .contains("<groupId>io.github.blueprintplatform</groupId>")
            .contains("<artifactId>" + artifactId + "</artifactId>")
            .contains("<java.version>" + java + "</java.version>")
            .contains("spring-boot-starter-web")
            .contains("spring-boot-starter-data-jpa")
            .contains("spring-boot-starter-actuator")
            .contains("spring-boot-starter-test");

        assertSpringBootParentVersion(pomXml, expectedBootPatch);

        ZipEntry appYamlEntry =
            zipFile.getEntry(artifactId + "/src/main/resources/application.yml");
        assertThat(appYamlEntry).isNotNull();
        String appYaml = readTextEntry(zipFile, appYamlEntry);

        assertThat(appYaml).contains("spring:").contains("application:").contains("name:");

        ZipEntry readmeEntry = zipFile.getEntry(artifactId + "/README.md");
        assertThat(readmeEntry).isNotNull();
        String readme = readTextEntry(zipFile, readmeEntry);

        assertThat(readme).contains("Greeting").contains(packageName);
      }
    } else {
      assertThat(io.exitCode()).isEqualTo(1);
      assertThat(Files.exists(archivePath))
          .as("Archive should NOT be created for incompatible target")
          .isFalse();

      assertThat(io.stderr()).contains("(code: " + expectedErrorCode + ")");
    }
  }

  private record CapturedIo(int exitCode, String stdout, String stderr) {}
}
