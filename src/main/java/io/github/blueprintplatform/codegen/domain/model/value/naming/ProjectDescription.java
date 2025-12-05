package io.github.blueprintplatform.codegen.domain.model.value.naming;

import io.github.blueprintplatform.codegen.domain.policy.naming.ProjectDescriptionPolicy;

public record ProjectDescription(String value) {
  public ProjectDescription {
    value = ProjectDescriptionPolicy.enforce(value);
  }

  public boolean isEmpty() {
    return value.isEmpty();
  }
}
