package io.github.bsayli.codegen.initializr.domain.model.value.identity;

import io.github.bsayli.codegen.initializr.domain.policy.identity.ArtifactIdPolicy;

public record ArtifactId(String value) {
  public ArtifactId {
    value = ArtifactIdPolicy.enforce(value);
  }
}
