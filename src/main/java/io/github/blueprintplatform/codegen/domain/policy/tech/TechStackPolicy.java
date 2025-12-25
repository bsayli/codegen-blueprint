package io.github.blueprintplatform.codegen.domain.policy.tech;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.BuildTool;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Framework;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.Language;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;

public final class TechStackPolicy {

  private static final ErrorCode TECH_STACK_NOT_BLANK = () -> "project.tech-stack.not.blank";

  private TechStackPolicy() {}

  public static TechStack enforce(TechStack techStack) {
    if (techStack == null
        || techStack.framework() == null
        || techStack.buildTool() == null
        || techStack.language() == null) {
      throw new DomainViolationException(TECH_STACK_NOT_BLANK);
    }
    return techStack;
  }

  public static void requireNonNull(Framework framework, BuildTool buildTool, Language language) {
    if (framework == null || buildTool == null || language == null) {
      throw new DomainViolationException(TECH_STACK_NOT_BLANK);
    }
  }
}
