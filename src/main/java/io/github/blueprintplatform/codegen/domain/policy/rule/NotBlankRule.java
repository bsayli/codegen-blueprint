package io.github.blueprintplatform.codegen.domain.policy.rule;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.NOT_BLANK;

import io.github.blueprintplatform.codegen.domain.error.code.Field;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class NotBlankRule implements Rule<String> {
  private final Field field;

  public NotBlankRule(Field field) {
    this.field = field;
  }

  @Override
  public void check(String value) {
    if (value == null || value.isBlank())
      throw new DomainViolationException(compose(field, NOT_BLANK));
  }
}
