package io.github.blueprintplatform.codegen.domain.model.value.tech.stack;

import io.github.blueprintplatform.codegen.domain.policy.tech.TechStackPolicy;

public record TechStack(Framework framework, BuildTool buildTool, Language language) {
  public TechStack {
    TechStackPolicy.requireNonNull(framework, buildTool, language);
  }
}
