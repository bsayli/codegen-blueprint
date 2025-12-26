package io.github.blueprintplatform.codegen.adapter.out.build.shared;

import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependencies;
import io.github.blueprintplatform.codegen.domain.model.value.dependency.Dependency;
import java.util.ArrayList;
import java.util.List;

public class BuildDependencyMapper {

  public List<BuildDependency> from(Dependencies dependencies) {
    if (dependencies == null || dependencies.isEmpty()) return List.of();
    var list = new ArrayList<BuildDependency>(dependencies.asList().size());
    for (Dependency d : dependencies.asList()) list.add(from(d));
    return list;
  }

  public BuildDependency from(Dependency d) {
    var v = (d.version() == null || d.version().value().isBlank()) ? null : d.version().value();
    var s = (d.scope() == null || d.scope().value().isBlank()) ? null : d.scope().value();
    return BuildDependency.of(
        d.coordinates().groupId().value(), d.coordinates().artifactId().value(), v, s);
  }
}
