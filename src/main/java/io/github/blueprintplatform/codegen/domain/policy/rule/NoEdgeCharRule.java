package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class NoEdgeCharRule implements Rule<String> {
  private final char edgeChar;
  private final ErrorCode errorCode;

  public NoEdgeCharRule(char edgeChar, ErrorCode errorCode) {
    this.edgeChar = edgeChar;
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null || value.isEmpty()) {
      throw new DomainViolationException(errorCode);
    }
    if (value.charAt(0) == edgeChar || value.charAt(value.length() - 1) == edgeChar) {
      throw new DomainViolationException(errorCode);
    }
  }
}
