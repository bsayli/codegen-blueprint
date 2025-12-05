package io.github.blueprintplatform.codegen.domain.policy.rule;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;

import io.github.blueprintplatform.codegen.domain.error.code.Field;
import io.github.blueprintplatform.codegen.domain.error.code.Violation;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class StartsWithLetterRule implements Rule<String> {
  private final Field field;
  private final Violation violation;

  public StartsWithLetterRule(Field field, Violation violation) {
    this.field = field;
    this.violation = violation;
  }

  @Override
  public void check(String value) {
    if (value == null || value.isEmpty()) {
      throw new DomainViolationException(compose(field, violation));
    }
    char c = value.charAt(0);
    if (c < 'a' || c > 'z') {
      throw new DomainViolationException(compose(field, violation));
    }
  }
}
