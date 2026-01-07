package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java.build;

import static java.util.Map.entry;

import io.github.blueprintplatform.codegen.adapter.out.build.shared.BuildDependency;
import io.github.blueprintplatform.codegen.adapter.out.build.shared.BuildDependencyMapper;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.BuildConfigurationPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MavenPomBuildConfigurationAdapter extends AbstractSingleTemplateArtifactAdapter
    implements BuildConfigurationPort {

  private final BuildDependencyMapper buildDependencyMapper;

  public MavenPomBuildConfigurationAdapter(
      TemplateRenderer renderer,
      ArtifactSpec artifactSpec,
      BuildDependencyMapper buildDependencyMapper) {
    super(renderer, artifactSpec);
    this.buildDependencyMapper = buildDependencyMapper;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.BUILD_CONFIG;
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint bp) {
    ProjectIdentity id = bp.getMetadata().identity();
    SpringBootJvmTarget target = (SpringBootJvmTarget) bp.getPlatform().platformTarget();

    return Map.ofEntries(
        entry(MavenPomBuildModel.KEY_GROUP_ID, id.groupId().value()),
        entry(MavenPomBuildModel.KEY_ARTIFACT_ID, id.artifactId().value()),
        entry(MavenPomBuildModel.KEY_JAVA_VERSION, target.java().asString()),
        entry(MavenPomBuildModel.KEY_SPRING_BOOT_VER, target.springBoot().defaultVersion()),
        entry(MavenPomBuildModel.KEY_PROJECT_NAME, bp.getMetadata().name().value()),
        entry(MavenPomBuildModel.KEY_PROJECT_DESCRIPTION, bp.getMetadata().description().value()),
        entry(MavenPomBuildModel.KEY_POM_PROPERTIES, governancePomProperties(bp)),
        entry(MavenPomBuildModel.KEY_DEPENDENCIES, buildPomDependencies(bp)));
  }

  private Map<String, String> governancePomProperties(ProjectBlueprint bp) {
    if (!bp.getArchitecture().governance().isEnabled()) {
      return Map.of();
    }
    return Map.of(MavenPomBuildModel.ARCH_UNIT_VERSION_KEY, MavenPomBuildModel.ARCH_UNIT_VERSION);
  }

  private List<BuildDependency> buildPomDependencies(ProjectBlueprint bp) {
    List<BuildDependency> deps = new ArrayList<>();

    deps.add(MavenPomBuildModel.CORE_STARTER);
    deps.addAll(buildDependencyMapper.from(bp.getDependencies()));

    if (isSampleEnabled(bp) && !isWebSelected(bp)) {
      deps.add(MavenPomBuildModel.WEB_STARTER);
    }

    if (isJpaSelected(bp)) {
      deps.add(MavenPomBuildModel.H2_DB);
    }

    if (bp.getArchitecture().governance().isEnabled()) {
      deps.add(MavenPomBuildModel.ARCH_UNIT_TEST);
    }

    deps.add(MavenPomBuildModel.TEST_STARTER);

    return List.copyOf(deps);
  }

  private boolean isSampleEnabled(ProjectBlueprint bp) {
    var arch = bp.getArchitecture();
    return arch != null && arch.sampleCodeOptions() != null && arch.sampleCodeOptions().isEnabled();
  }

  private boolean isWebSelected(ProjectBlueprint bp) {
    var deps = bp.getDependencies();
    return deps != null
        && !deps.isEmpty()
        && deps.asList().stream().anyMatch(MavenPomBuildModel.WEB_STARTER_FEATURE.matches());
  }

  private boolean isJpaSelected(ProjectBlueprint bp) {
    var deps = bp.getDependencies();
    return deps != null
        && !deps.isEmpty()
        && deps.asList().stream().anyMatch(MavenPomBuildModel.JPA_STARTER.matches());
  }
}
