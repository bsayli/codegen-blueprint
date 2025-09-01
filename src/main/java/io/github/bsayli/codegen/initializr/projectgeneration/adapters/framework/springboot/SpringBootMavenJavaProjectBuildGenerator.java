package io.github.bsayli.codegen.initializr.projectgeneration.adapters.framework.springboot;

import io.github.bsayli.codegen.initializr.projectgeneration.model.ProjectMetadata;
import io.github.bsayli.codegen.initializr.projectgeneration.model.maven.MavenPlugin;
import io.github.bsayli.codegen.initializr.projectgeneration.model.maven.MavenPom;
import io.github.bsayli.codegen.initializr.projectgeneration.model.maven.MavenPom.MavenPomBuilder;
import io.github.bsayli.codegen.initializr.projectgeneration.model.templating.TemplateType;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectBuildGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.ProjectBuildWrapperGenerator;
import io.github.bsayli.codegen.initializr.projectgeneration.ports.TemplateEngine;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component("springBootMavenJavaProjectBuildGenerator")
public class SpringBootMavenJavaProjectBuildGenerator implements ProjectBuildGenerator {

  private static final String MAVEN_MODEL_VERSION = "4.0.0";
  private static final String MAVEN_PROJECT_VERSION = "0.0.1-SNAPSHOT";

  private final TemplateEngine freeMarkerTemplateEngine;
  private final ProjectBuildWrapperGenerator mavenBuildWrapperGenerator;
  private final List<MavenPlugin> springBootMavenJavaPlugins;

  public SpringBootMavenJavaProjectBuildGenerator(
      TemplateEngine freeMarkerTemplateEngine,
      ProjectBuildWrapperGenerator mavenBuildWrapperGenerator,
      List<MavenPlugin> springBootMavenJavaPlugins) {
    this.freeMarkerTemplateEngine = freeMarkerTemplateEngine;
    this.springBootMavenJavaPlugins = springBootMavenJavaPlugins;
    this.mavenBuildWrapperGenerator = mavenBuildWrapperGenerator;
  }

  @Override
  public void generateBuildConfiguration(File projectDestination, ProjectMetadata projectMetadata)
      throws IOException {
    MavenPomBuilder mavenPomBuilder = new MavenPom.MavenPomBuilder();
    MavenPom mavenPom =
        mavenPomBuilder
            .modelVersion(MAVEN_MODEL_VERSION)
            .version(MAVEN_PROJECT_VERSION)
            .projectMetadata(projectMetadata)
            .addDependencies(projectMetadata.getDependencies())
            .addPlugins(springBootMavenJavaPlugins)
            .build();

    Map<String, Object> pomModel = new HashMap<>();
    pomModel.put("pom", mavenPom);

    freeMarkerTemplateEngine.generateFileFromTemplate(
        TemplateType.SPRING_BOOT_JAVA_POM, pomModel, projectDestination);

    mavenBuildWrapperGenerator.generateBuildWrapper(projectDestination, projectMetadata);
  }
}
