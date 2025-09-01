package io.github.bsayli.codegen.initializr.projectgeneration.generator.springboot.maven;

import io.github.bsayli.codegen.initializr.projectgeneration.generator.base.AbstractProjectGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ApplicationYamlGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.FrameworkProjectStarterClassGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.FrameworkSpecificTestUnitGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.GitIgnoreGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectArchiver;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectBuildGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectDirectoryInitializer;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectDocumentationGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectLayoutGenerator;
import org.springframework.stereotype.Component;

@Component("springBootMavenJavaProjectGenerator")
public class SpringBootMavenJavaProjectGenerator extends AbstractProjectGenerator {

  public SpringBootMavenJavaProjectGenerator(
      ProjectDirectoryInitializer projectRootDirectoryInitializer,
      GitIgnoreGenerator gitIgnoreFileGenerator,
      ProjectArchiver projectZipArchiver,
      ProjectLayoutGenerator mavenJavaProjectLayoutGenerator,
      ProjectBuildGenerator springBootMavenJavaProjectBuildGenerator,
      ApplicationYamlGenerator springBootApplicationYamlGenerator,
      FrameworkProjectStarterClassGenerator springBootJavaMainClassGenerator,
      FrameworkSpecificTestUnitGenerator springBootJavaTestClassGenerator,
      ProjectDocumentationGenerator springBootMavenJavaReadMeGenerator) {
    super(
        projectRootDirectoryInitializer,
        gitIgnoreFileGenerator,
        projectZipArchiver,
        mavenJavaProjectLayoutGenerator,
        springBootMavenJavaProjectBuildGenerator,
        springBootApplicationYamlGenerator,
        springBootJavaMainClassGenerator,
        springBootJavaTestClassGenerator,
        springBootMavenJavaReadMeGenerator);
  }
}
