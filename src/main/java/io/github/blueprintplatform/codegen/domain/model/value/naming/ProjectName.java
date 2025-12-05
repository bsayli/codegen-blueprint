package io.github.blueprintplatform.codegen.domain.model.value.naming;

import io.github.blueprintplatform.codegen.domain.policy.naming.ProjectNamePolicy;

public record ProjectName(String value) {
  public ProjectName {
    value = ProjectNamePolicy.enforce(value);
  }
}
