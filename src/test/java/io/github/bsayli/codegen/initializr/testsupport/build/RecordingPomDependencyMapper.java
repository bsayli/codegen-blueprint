package io.github.bsayli.codegen.initializr.testsupport.build;

import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependency;
import io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared.PomDependencyMapper;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
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
