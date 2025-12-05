package io.github.blueprintplatform.codegen.domain.model.value.dependency;

import io.github.blueprintplatform.codegen.domain.policy.dependency.DependencyVersionPolicy;

public record DependencyVersion(String value) {
  public DependencyVersion {
    value = DependencyVersionPolicy.enforce(value);
  }
}
