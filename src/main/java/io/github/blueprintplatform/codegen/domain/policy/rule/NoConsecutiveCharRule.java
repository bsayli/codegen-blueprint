package io.github.blueprintplatform.codegen.domain.policy.rule;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;

import io.github.blueprintplatform.codegen.domain.error.code.Field;
import io.github.blueprintplatform.codegen.domain.error.code.Violation;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class NoConsecutiveCharRule implements Rule<String> {
  private final char ch;
  private final Field field;
  private final Violation violation;

  public NoConsecutiveCharRule(char ch, Field field, Violation violation) {
    this.ch = ch;
    this.field = field;
    this.violation = violation;
  }

  @Override
  public void check(String value) {
    if (value == null) {
      throw new DomainViolationException(compose(field, violation));
    }
    for (int i = 1; i < value.length(); i++) {
      if (value.charAt(i) == ch && value.charAt(i - 1) == ch) {
        throw new DomainViolationException(compose(field, violation));
      }
    }
  }
}
