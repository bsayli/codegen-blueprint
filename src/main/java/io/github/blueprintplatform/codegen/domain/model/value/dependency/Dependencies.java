package io.github.blueprintplatform.codegen.domain.model.value.dependency;

import io.github.blueprintplatform.codegen.domain.policy.dependency.DependenciesPolicy;
import java.util.List;

public final class Dependencies {
  private final List<Dependency> items;

  private Dependencies(List<Dependency> items) {
    this.items = List.copyOf(items);
  }

  public static Dependencies of(List<Dependency> raw) {
    return new Dependencies(DependenciesPolicy.enforce(raw));
  }

  public List<Dependency> asList() {
    return List.copyOf(items);
  }

  public boolean isEmpty() {
    return items.isEmpty();
  }
}
