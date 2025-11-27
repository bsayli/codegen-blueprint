package io.github.bsayli.codegen.initializr.domain.model.value.naming;

import io.github.bsayli.codegen.initializr.domain.policy.naming.ProjectDescriptionPolicy;

public record ProjectDescription(String value) {
  public ProjectDescription {
    value = ProjectDescriptionPolicy.enforce(value);
  }

  public boolean isEmpty() {
    return value.isEmpty();
  }
}
