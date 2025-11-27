package io.github.bsayli.codegen.initializr.domain.model.value.identity;

import io.github.bsayli.codegen.initializr.domain.policy.identity.GroupIdPolicy;

public record GroupId(String value) {
  public GroupId {
    value = GroupIdPolicy.enforce(value);
  }
}
