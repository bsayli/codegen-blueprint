package io.github.blueprintplatform.codegen.bootstrap.mapper;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactSpec;
import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.TemplateSpec;
import io.github.blueprintplatform.codegen.bootstrap.config.properties.ArtifactDefinition;
import org.springframework.stereotype.Component;

@Component
public class ArtifactSpecMapper {

  public ArtifactSpec from(ArtifactDefinition def) {
    return new ArtifactSpec(
        def.templateBasePath(),
        def.templates().stream().map(t -> new TemplateSpec(t.template(), t.outputPath())).toList());
  }
}
