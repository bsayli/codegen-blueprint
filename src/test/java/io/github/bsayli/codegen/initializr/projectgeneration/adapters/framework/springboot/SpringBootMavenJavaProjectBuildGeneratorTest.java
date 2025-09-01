package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bsayli.codegen.initializr.projectgeneration.model.Dependency;
import io.github.bsayli.codegen.initializr.projectgeneration.model.spring.SpringBootJavaProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.spring.SpringBootJavaProjectMetadata.SpringBootJavaProjectMetadataBuilder;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringBootMavenJavaProjectBuildGeneratorTest {

  private static final String POM_FILE_NAME = "pom.xml";
  private static final String WRAPPER_FILE_DIR = ".mvn/wrapper";
  private static final String WRAPPER_FILE_NAME = "maven-wrapper.properties";
  private static final String WRAPPER_VERSION = "3.3.3";

  @Autowired private SpringBootMavenJavaProjectBuildGenerator generator;

  @TempDir private Path tempFolder;

  @Test
  void testGenerateBuildConfiguration_CreatesPomFileAndWrapper() throws IOException {
    Dependency dependencySpringBootStarterWeb =
        new Dependency.DependencyBuilder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-web")
            .build();

    Dependency dependencySpringBootStarterTest =
        new Dependency.DependencyBuilder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-test")
            .scope("test")
            .build();

    SpringBootJavaProjectMetadataBuilder projectMetadataBuilder =
        new SpringBootJavaProjectMetadata.SpringBootJavaProjectMetadataBuilder();
    projectMetadataBuilder
        .groupId("com.codegen")
        .artifactId("codegen-initialzr")
        .name("codegen-initialzr")
        .description("Codegen Initialzr")
        .packageName("com.codegen.initialzr")
        .dependencies(List.of(dependencySpringBootStarterWeb, dependencySpringBootStarterTest));

    SpringBootJavaProjectMetadata springBootJavaProjectMetadata =
        projectMetadataBuilder.springBootVersion("3.5.5").javaVersion("21").build();

    File projectDestination = tempFolder.toFile();

    generator.generateBuildConfiguration(projectDestination, springBootJavaProjectMetadata);

    File pomFile = new File(projectDestination, POM_FILE_NAME);
    assertTrue(pomFile.exists(), "pom.xml file should be created");

    File wrapperFileDir = new File(projectDestination, WRAPPER_FILE_DIR);
    assertTrue(wrapperFileDir.exists(), "Wrapper file directory should be created!");

    File wrapperFile = new File(wrapperFileDir, WRAPPER_FILE_NAME);
    assertTrue(wrapperFile.exists(), "Wrapper file maven-wrapper.properties should be created!");
  }

  @Test
  void testGenerateBuildConfiguration_CreatesFileAndVerifiesContent() throws IOException {
    Dependency dependencySpringBootStarterWeb =
        new Dependency.DependencyBuilder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-web")
            .build();

    Dependency dependencySpringBootStarterTest =
        new Dependency.DependencyBuilder()
            .groupId("org.springframework.boot")
            .artifactId("spring-boot-starter-test")
            .scope("test")
            .build();

    SpringBootJavaProjectMetadataBuilder projectMetadataBuilder =
        new SpringBootJavaProjectMetadata.SpringBootJavaProjectMetadataBuilder();
    projectMetadataBuilder
        .groupId("com.codegen")
        .artifactId("codegen-initialzr")
        .name("codegen-initialzr")
        .description("Codegen Initialzr")
        .packageName("com.codegen.initialzr")
        .dependencies(List.of(dependencySpringBootStarterWeb, dependencySpringBootStarterTest));

    String javaVersion = "21";
    String springBootVersion = "3.5.5";
    SpringBootJavaProjectMetadata springBootJavaProjectMetadata =
        projectMetadataBuilder
            .springBootVersion(springBootVersion)
            .javaVersion(javaVersion)
            .build();

    File projectDestination = tempFolder.toFile();

    generator.generateBuildConfiguration(projectDestination, springBootJavaProjectMetadata);

    File pomFile = new File(projectDestination, POM_FILE_NAME);
    String pomContent = Files.readString(pomFile.toPath());
    pomContent = pomContent.trim().replaceAll("\\s*", "");

    String expectedJavaVersionLine = "<java.version>" + javaVersion + "</java.version>";
    assertTrue(
        pomContent.contains(expectedJavaVersionLine),
        "Generated content should contain " + expectedJavaVersionLine);

    String parentContent =
        """
		  <parent>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-parent</artifactId>
			<version>3.5.5</version>
			<relativePath/>
		  </parent>
		""";

    parentContent = parentContent.trim().replaceAll("\\s*", "");
    assertTrue(
        pomContent.contains(parentContent), "Generated content should contain " + parentContent);

    File wrapperFileDir = new File(projectDestination, WRAPPER_FILE_DIR);
    File wrapperFile = new File(wrapperFileDir, WRAPPER_FILE_NAME);
    assertTrue(wrapperFile.exists(), "Wrapper file should be created!");

    List<String> wrapperFileList = Files.readAllLines(wrapperFile.toPath());
    assertThat(wrapperFileList, hasItem("wrapperVersion=" + WRAPPER_VERSION));
  }
}
