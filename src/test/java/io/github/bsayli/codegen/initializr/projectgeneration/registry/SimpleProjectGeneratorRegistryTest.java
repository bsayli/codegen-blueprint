package io.github.bsayli.codegen.initializr.projectgeneration.registry;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.bsayli.codegen.initializr.projectgeneration.generator.ProjectGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.generator.springboot.maven.SpringBootMavenJavaProjectGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectType;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.BuildTool;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.Framework;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.Language;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SimpleProjectGeneratorRegistryTest {

  @Autowired private SimpleProjectGeneratorRegistry registry;

  @Test
  void testGetProjectGenerator_ExistingProjectType_ReturnsGenerator() {
    ProjectType expectedProjectType =
        new ProjectType(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    Optional<ProjectGenerator> generatorOptional =
        registry.getProjectGenerator(expectedProjectType);

    assertTrue(generatorOptional.isPresent());

    ProjectGenerator projectGenerator = generatorOptional.get();

    assertSame(SpringBootMavenJavaProjectGenerator.class, projectGenerator.getClass());
  }

  @Test
  void testGetProjectGenerator_NonExistingProjectType_ReturnsEmptyOptional() {
    ProjectType unsupportedProjectType =
        new ProjectType(Framework.QUARKUS, BuildTool.MAVEN, Language.JAVA);

    Optional<ProjectGenerator> generatorOptional =
        registry.getProjectGenerator(unsupportedProjectType);

    assertFalse(generatorOptional.isPresent());
  }
}
