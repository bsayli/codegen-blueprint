package io.github.blueprintplatform.codegen.domain.model.value.identity;

import io.github.blueprintplatform.codegen.domain.policy.identity.GroupIdPolicy;

public record GroupId(String value) {
  public GroupId {
    value = GroupIdPolicy.enforce(value);
  }
}
