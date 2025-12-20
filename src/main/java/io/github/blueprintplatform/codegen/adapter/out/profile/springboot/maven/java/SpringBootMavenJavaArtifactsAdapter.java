package io.github.blueprintplatform.codegen.adapter.out.profile.springboot.maven.java;

import io.github.blueprintplatform.codegen.adapter.out.shared.artifact.ArtifactPipelineExecutor;
import io.github.blueprintplatform.codegen.application.port.out.ProjectArtifactsPort;
import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactPort;
import io.github.blueprintplatform.codegen.domain.model.ProjectBlueprint;
import io.github.blueprintplatform.codegen.domain.port.out.artifact.GeneratedResource;
import java.util.List;

public class SpringBootMavenJavaArtifactsAdapter implements ProjectArtifactsPort {

  private final List<ArtifactPort> artifacts;
  private final ArtifactPipelineExecutor artifactPipelineExecutor;

  public SpringBootMavenJavaArtifactsAdapter(
      ArtifactPipelineExecutor artifactPipelineExecutor, List<ArtifactPort> artifacts) {
    this.artifactPipelineExecutor = artifactPipelineExecutor;
    this.artifacts = artifacts;
  }

  @Override
  public Iterable<? extends GeneratedResource> generate(ProjectBlueprint blueprint) {
    return artifactPipelineExecutor.execute(artifacts, blueprint);
  }
}
