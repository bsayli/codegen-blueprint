package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.doc;

import static java.util.Map.entry;

import io.github.blueprintplatform.codegen.adapter.out.build.shared.BuildDependency;
import io.github.blueprintplatform.codegen.adapter.out.build.shared.BuildDependencyMapper;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.dependency.DependencyFeature;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ProjectDocumentationPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import io.github.blueprintplatform.codegen.domain.model.value.pkg.PackageName;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProjectDocumentationAdapter extends AbstractSingleTemplateArtifactAdapter
    implements ProjectDocumentationPort {

  private final BuildDependencyMapper buildDependencyMapper;

  public ProjectDocumentationAdapter(
      TemplateRenderer renderer,
      ArtifactSpec artifactSpec,
      BuildDependencyMapper buildDependencyMapper) {
    super(renderer, artifactSpec);
    this.buildDependencyMapper = buildDependencyMapper;
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.PROJECT_DOCUMENTATION;
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint bp) {
    ProjectIdentity id = bp.getMetadata().identity();
    PackageName pkg = bp.getMetadata().packageName();
    TechStack stack = bp.getPlatform().techStack();
    SpringBootJvmTarget target = (SpringBootJvmTarget) bp.getPlatform().platformTarget();
    Dependencies deps = bp.getDependencies();

    Map<String, Boolean> features =
        deps == null || deps.isEmpty()
            ? Map.of()
            : Arrays.stream(ProjectDocumentationModel.FEATURES_SET)
                .collect(
                    Collectors.toMap(
                        DependencyFeature::key, f -> deps.asList().stream().anyMatch(f.matches())));

    return Map.ofEntries(
        entry(ProjectDocumentationModel.PROJECT_NAME, bp.getMetadata().name().value()),
        entry(
            ProjectDocumentationModel.PROJECT_DESCRIPTION, bp.getMetadata().description().value()),
        entry(ProjectDocumentationModel.GROUP_ID, id.groupId().value()),
        entry(ProjectDocumentationModel.ARTIFACT_ID, id.artifactId().value()),
        entry(ProjectDocumentationModel.PACKAGE_NAME, pkg.value()),
        entry(ProjectDocumentationModel.BUILD_TOOL, stack.buildTool().key()),
        entry(ProjectDocumentationModel.LANGUAGE, stack.language().key()),
        entry(ProjectDocumentationModel.FRAMEWORK, stack.framework().key()),
        entry(ProjectDocumentationModel.JAVA_VERSION, target.java().asString()),
        entry(ProjectDocumentationModel.SPRING_BOOT_VERSION, target.springBoot().defaultVersion()),
        entry(ProjectDocumentationModel.DEPENDENCIES, mapDependencies(deps)),
        entry(ProjectDocumentationModel.LAYOUT, bp.getArchitecture().layout().key()),
        entry(
            ProjectDocumentationModel.ENFORCEMENT, bp.getArchitecture().governance().mode().key()),
        entry(
            ProjectDocumentationModel.SAMPLE_CODE,
            bp.getArchitecture().sampleCodeOptions().level().key()),
        entry(ProjectDocumentationModel.FEATURES, features));
  }

  private List<BuildDependency> mapDependencies(Dependencies deps) {
    return buildDependencyMapper.from(deps);
  }
}
