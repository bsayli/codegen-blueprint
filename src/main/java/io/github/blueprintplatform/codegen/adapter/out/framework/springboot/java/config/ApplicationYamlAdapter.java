package io.github.blueprintplatform.codegen.adapter.out.framework.springboot.java.config;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.AbstractSingleTemplateArtifactAdapter;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.dependency.DependencyFeature;
import io.github.blueprintplatform.codegen.adapter.out.templating.TemplateRenderer;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ApplicationConfigurationPort;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.model.value.identity.ProjectIdentity;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ApplicationYamlAdapter extends AbstractSingleTemplateArtifactAdapter
    implements ApplicationConfigurationPort {

  public ApplicationYamlAdapter(TemplateRenderer renderer, ArtifactSpec artifactSpec) {
    super(renderer, artifactSpec);
  }

  @Override
  public ArtifactKey artifactKey() {
    return ArtifactKey.APP_CONFIG;
  }

  @Override
  protected Map<String, Object> buildModel(ProjectBlueprint blueprint) {
    ProjectIdentity id = blueprint.getMetadata().identity();
    var deps = blueprint.getDependencies();

    Map<String, Boolean> features =
        deps == null || deps.isEmpty()
            ? Map.of()
            : Arrays.stream(ApplicationYamlModel.FEATURES)
                .collect(
                    Collectors.toMap(
                        DependencyFeature::key, f -> deps.asList().stream().anyMatch(f.matches())));

    return Map.of(
        ApplicationYamlModel.KEY_APP_NAME,
        id.artifactId().value(),
        ApplicationYamlModel.KEY_FEATURES,
        features);
  }
}
