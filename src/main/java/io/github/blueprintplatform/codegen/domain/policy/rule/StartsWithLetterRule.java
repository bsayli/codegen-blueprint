package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class StartsWithLetterRule implements Rule<String> {
  private final ErrorCode errorCode;

  public StartsWithLetterRule(ErrorCode errorCode) {
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null || value.isEmpty()) {
      throw new DomainViolationException(errorCode);
    }
    char c = value.charAt(0);
    if (c < 'a' || c > 'z') {
      throw new DomainViolationException(errorCode);
    }
  }
}
