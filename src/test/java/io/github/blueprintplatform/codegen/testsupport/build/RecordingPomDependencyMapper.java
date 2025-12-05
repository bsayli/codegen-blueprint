package io.github.blueprintplatform.codegen.testsupport.build;

import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependency;
import io.github.blueprintplatform.codegen.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import java.util.List;

public final class RecordingPomDependencyMapper extends PomDependencyMapper {

  private final List<PomDependency> toReturn;
  public Dependencies capturedDependencies;

  public RecordingPomDependencyMapper(List<PomDependency> toReturn) {
    this.toReturn = toReturn;
  }

  @Override
  public List<PomDependency> from(Dependencies dependencies) {
    this.capturedDependencies = dependencies;
    return toReturn;
  }
}
