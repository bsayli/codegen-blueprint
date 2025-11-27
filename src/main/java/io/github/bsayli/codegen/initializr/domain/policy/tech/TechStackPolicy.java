package io.github.bsayli.codegen.initializr.domain.policy.tech;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;

public final class TechStackPolicy {

  private TechStackPolicy() {}

  public static TechStack enforce(TechStack techStack) {
    if (techStack == null
        || techStack.framework() == null
        || techStack.buildTool() == null
        || techStack.language() == null) {
      throw new DomainViolationException(() -> "project.tech-stack.not.blank");
    }
    return techStack;
  }

  public static void requireNonNull(Framework framework, BuildTool buildTool, Language language) {
    if (framework == null || buildTool == null || language == null) {
      throw new DomainViolationException(() -> "project.tech-stack.not.blank");
    }
  }
}
