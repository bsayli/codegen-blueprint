package io.github.bsayli.codegen.initializr.domain.model.value.dependency;

import io.github.bsayli.codegen.initializr.domain.policy.dependency.DependencyVersionPolicy;

public record DependencyVersion(String value) {
  public DependencyVersion {
    value = DependencyVersionPolicy.enforce(value);
  }
}
