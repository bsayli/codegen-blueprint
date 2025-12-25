package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class NoConsecutiveCharRule implements Rule<String> {
  private final char ch;
  private final ErrorCode errorCode;

  public NoConsecutiveCharRule(char ch, ErrorCode errorCode) {
    this.ch = ch;
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null) {
      throw new DomainViolationException(errorCode);
    }
    for (int i = 1; i < value.length(); i++) {
      if (value.charAt(i) == ch && value.charAt(i - 1) == ch) {
        throw new DomainViolationException(errorCode);
      }
    }
  }
}
