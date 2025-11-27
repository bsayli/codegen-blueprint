package io.github.bsayli.codegen.initializr.adapter.out.profile.springboot.maven.java.build;

import static java.util.Map.entry;

import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependency;
import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.bsayli.codegen.initializr.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.bsayli.codegen.initializr.adapter.out.templating.TemplateRenderer;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.BuildConfigurationPort;
import io.github.bsayli.codegen.initializr.bootstrap.config.ArtifactDefinition;
import io.github.bsayli.codegen.initializr.domain.model.ProjectBlueprint;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ProjectIdentity;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.platform.SpringBootJvmTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MavenPomBuildConfigurationAdapter extends AbstractSingleTemplateArtifactAdapter
    implements BuildConfigurationPort {

  private static final String KEY_GROUP_ID = "groupId";
  private static final String KEY_ARTIFACT_ID = "artifactId";
  private static final String KEY_JAVA_VERSION = "javaVersion";
  private static final String KEY_SPRING_BOOT_VER = "springBootVersion";
  private static final String KEY_DEPENDENCIES = "dependencies";
  private static final String KEY_PROJECT_NAME = "projectName";
  private static final String KEY_PROJECT_DESCRIPTION = "projectDescription";

  private static final PomDependency CORE_STARTER =
      PomDependency.of("org.springframework.boot", "spring-boot-starter");

  private static final PomDependency TEST_STARTER =
      PomDependency.of("org.springframework.boot", "spring-boot-starter-test", null, "test");

  private final PomDependencyMapper pomDependencyMapper;

  public MavenPomBuildConfigurationAdapter(
      TemplateRenderer renderer,
      ArtifactDefinition artifactDefinition,
      PomDependencyMapper pomDependencyMapper) {
    super(renderer, artifactDefinition);
    this.pomDependencyMapper = pomDependencyMapper;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.BUILD_CONFIG;
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint bp) {
    ProjectIdentity id = bp.getIdentity();
    SpringBootJvmTarget pt = (SpringBootJvmTarget) bp.getPlatformTarget();

    List<PomDependency> dependencies = new ArrayList<>();
    dependencies.add(CORE_STARTER);
    dependencies.addAll(pomDependencyMapper.from(bp.getDependencies()));
    dependencies.add(TEST_STARTER);

    return Map.ofEntries(
        entry(KEY_GROUP_ID, id.groupId().value()),
        entry(KEY_ARTIFACT_ID, id.artifactId().value()),
        entry(KEY_JAVA_VERSION, pt.java().asString()),
        entry(KEY_SPRING_BOOT_VER, pt.springBoot().value()),
        entry(KEY_PROJECT_NAME, bp.getName().value()),
        entry(KEY_PROJECT_DESCRIPTION, bp.getDescription().value()),
        entry(KEY_DEPENDENCIES, dependencies));
  }
}
