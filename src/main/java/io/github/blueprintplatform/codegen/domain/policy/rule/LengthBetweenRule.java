package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class LengthBetweenRule implements Rule<String> {
  private final int min;
  private final int max;
  private final ErrorCode errorCode;

  public LengthBetweenRule(int min, int max, ErrorCode errorCode) {
    this.min = min;
    this.max = max;
    this.errorCode = errorCode;
  }

  @Override
  public void check(String v) {
    if (v == null || v.length() < min || v.length() > max) {
      throw new DomainViolationException(errorCode, min, max);
    }
  }
}
