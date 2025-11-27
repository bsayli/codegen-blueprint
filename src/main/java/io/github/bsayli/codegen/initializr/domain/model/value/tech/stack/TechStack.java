package io.github.bsayli.codegen.initializr.domain.model.value.tech.stack;

import io.github.bsayli.codegen.initializr.domain.policy.tech.TechStackPolicy;

public record TechStack(Framework framework, BuildTool buildTool, Language language) {
  public TechStack {
    TechStackPolicy.requireNonNull(framework, buildTool, language);
  }
}
