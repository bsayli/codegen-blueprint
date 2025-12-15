package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.build;

import static java.util.Map.entry;

import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependency;
import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.BuildConfigurationPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
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

  private static final String SPRING_BOOT_GROUP_ID = "org.springframework.boot";
  private static final String STARTER_DATA_JPA = "spring-boot-starter-data-jpa";

  private static final PomDependency CORE_STARTER =
      PomDependency.of(SPRING_BOOT_GROUP_ID, "spring-boot-starter");

  private static final PomDependency TEST_STARTER =
      PomDependency.of(SPRING_BOOT_GROUP_ID, "spring-boot-starter-test", null, "test");

  private static final PomDependency H2_DB = PomDependency.of("com.h2database", "h2", null, null);

  private static final String KEY_POM_PROPERTIES = "pomProperties";

  private static final String ARCHUNIT_VERSION_KEY = "archunit.version";
  private static final String ARCHUNIT_VERSION = "1.4.1";

  private static final PomDependency ARCH_UNIT_TEST =
      PomDependency.ofWithVersionProperty(
          "com.tngtech.archunit", "archunit-junit5", ARCHUNIT_VERSION_KEY, "test");

  private final PomDependencyMapper pomDependencyMapper;

  public MavenPomBuildConfigurationAdapter(
      TemplateRenderer renderer,
      ArtifactSpec artifactSpec,
      PomDependencyMapper pomDependencyMapper) {
    super(renderer, artifactSpec);
    this.pomDependencyMapper = pomDependencyMapper;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.BUILD_CONFIG;
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint bp) {
    ProjectIdentity id = bp.getMetadata().identity();
    SpringBootJvmTarget pt = (SpringBootJvmTarget) bp.getPlatform().platformTarget();

    return Map.ofEntries(
        entry(KEY_GROUP_ID, id.groupId().value()),
        entry(KEY_ARTIFACT_ID, id.artifactId().value()),
        entry(KEY_JAVA_VERSION, pt.java().asString()),
        entry(KEY_SPRING_BOOT_VER, pt.springBoot().defaultVersion()),
        entry(KEY_PROJECT_NAME, bp.getMetadata().name().value()),
        entry(KEY_PROJECT_DESCRIPTION, bp.getMetadata().description().value()),
        entry(KEY_POM_PROPERTIES, resolvePomProperties(bp)),
        entry(KEY_DEPENDENCIES, resolvePomDependencies(bp)));
  }

  private List<PomDependency> resolvePomDependencies(ProjectBlueprint bp) {
    List<PomDependency> deps = new ArrayList<>();

    deps.add(CORE_STARTER);
    deps.addAll(pomDependencyMapper.from(bp.getDependencies()));

    if (isStarterJpaSelected(bp)) {
      deps.add(H2_DB);
    }

    if (bp.getArchitecture().governance().isEnabled()) {
      deps.add(ARCH_UNIT_TEST);
    }

    deps.add(TEST_STARTER);

    return List.copyOf(deps);
  }

  private boolean isStarterJpaSelected(ProjectBlueprint bp) {
    return bp.getDependencies().asList().stream()
        .map(Dependency::coordinates)
        .anyMatch(
            c ->
                MavenPomBuildConfigurationAdapter.SPRING_BOOT_GROUP_ID.equalsIgnoreCase(
                        c.groupId().value())
                    && MavenPomBuildConfigurationAdapter.STARTER_DATA_JPA.equalsIgnoreCase(
                        c.artifactId().value()));
  }

  private Map<String, String> resolvePomProperties(ProjectBlueprint bp) {
    if (!bp.getArchitecture().governance().isEnabled()) {
      return Map.of();
    }
    return Map.of(ARCHUNIT_VERSION_KEY, ARCHUNIT_VERSION);
  }
}
