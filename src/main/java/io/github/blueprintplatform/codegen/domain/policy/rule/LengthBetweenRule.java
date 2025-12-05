package io.github.blueprintplatform.codegen.domain.policy.rule;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.LENGTH;

import io.github.blueprintplatform.codegen.domain.error.code.Field;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class LengthBetweenRule implements Rule<String> {
  private final int min;
  private final int max;
  private final Field field;

  public LengthBetweenRule(int min, int max, Field field) {
    this.min = min;
    this.max = max;
    this.field = field;
  }

  @Override
  public void check(String v) {
    if (v == null || v.length() < min || v.length() > max)
      throw new DomainViolationException(compose(field, LENGTH), min, max);
  }
}
