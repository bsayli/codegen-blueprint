package io.github.bsayli.codegen.initializr.domain.policy.rule;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;
import static io.github.bsayli.codegen.initializr.domain.error.code.Violation.NOT_BLANK;

import io.github.bsayli.codegen.initializr.domain.error.code.Field;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;

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
