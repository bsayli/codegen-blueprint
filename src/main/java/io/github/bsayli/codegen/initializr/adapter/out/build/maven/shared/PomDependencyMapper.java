package io.github.bsayli.codegen.initializr.adapter.out.build.maven.shared;

import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependencies;
import io.github.bsayli.codegen.initializr.domain.model.value.dependency.Dependency;
import java.util.ArrayList;
import java.util.List;

public class PomDependencyMapper {

  public List<PomDependency> from(Dependencies dependencies) {
    if (dependencies == null || dependencies.isEmpty()) return List.of();
    var list = new ArrayList<PomDependency>(dependencies.asList().size());
    for (Dependency d : dependencies.asList()) list.add(from(d));
    return list;
  }

  public PomDependency from(Dependency d) {
    var v = (d.version() == null || d.version().value().isBlank()) ? null : d.version().value();
    var s = (d.scope() == null || d.scope().value().isBlank()) ? null : d.scope().value();
    return PomDependency.of(
        d.coordinates().groupId().value(), d.coordinates().artifactId().value(), v, s);
  }
}
