package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class NotBlankRule implements Rule<String> {
  private final ErrorCode errorCode;

  public NotBlankRule(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null || value.isBlank()) {
      throw new DomainViolationException(errorCode);
    }
  }
}
