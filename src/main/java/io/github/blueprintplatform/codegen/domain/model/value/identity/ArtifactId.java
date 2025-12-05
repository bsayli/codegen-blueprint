package io.github.blueprintplatform.codegen.domain.model.value.identity;

import io.github.blueprintplatform.codegen.domain.policy.identity.ArtifactIdPolicy;

public record ArtifactId(String value) {
  public ArtifactId {
    value = ArtifactIdPolicy.enforce(value);
  }
}
