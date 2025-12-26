package io.github.blueprintplatform.codegen.testsupport.build;

import io.github.blueprintplatform.codegen.adapter.out.build.shared.BuildDependency;
import io.github.blueprintplatform.codegen.adapter.out.build.shared.BuildDependencyMapper;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import java.util.List;

public final class RecordingBuildDependencyMapper extends BuildDependencyMapper {

  private final List<BuildDependency> toReturn;
  public Dependencies capturedDependencies;

  public RecordingBuildDependencyMapper(List<BuildDependency> toReturn) {
    this.toReturn = toReturn;
  }

  @Override
  public List<BuildDependency> from(Dependencies dependencies) {
    this.capturedDependencies = dependencies;
    return toReturn;
  }
}
