package io.github.blueprintplatform.codegen.adapter.out.shared.artifact;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.util.List;
import java.util.stream.StreamSupport;

public class ArtifactPipelineExecutor {

  public List<GeneratedResource> execute(List<ArtifactPort> artifacts, ProjectBlueprint blueprint) {
    return artifacts.stream()
        .flatMap(p -> StreamSupport.stream(p.generate(blueprint).spliterator(), false))
        .map(GeneratedResource.class::cast)
        .toList();
  }
}
