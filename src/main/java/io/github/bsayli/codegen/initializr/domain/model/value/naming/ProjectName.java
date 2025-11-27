package io.github.bsayli.codegen.initializr.domain.model.value.naming;

import io.github.bsayli.codegen.initializr.domain.policy.naming.ProjectNamePolicy;

public record ProjectName(String value) {
  public ProjectName {
    value = ProjectNamePolicy.enforce(value);
  }
}
