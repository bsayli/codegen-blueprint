package io.github.bsayli.codegen.initializr.domain.policy.rule;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;

import io.github.bsayli.codegen.initializr.domain.error.code.Field;
import io.github.bsayli.codegen.initializr.domain.error.code.Violation;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;

public final class NoEdgeCharRule implements Rule<String> {
  private final char edgeChar;
  private final Field field;
  private final Violation violation;

  public NoEdgeCharRule(char edgeChar, Field field, Violation violation) {
    this.edgeChar = edgeChar;
    this.field = field;
    this.violation = violation;
  }

  @Override
  public void check(String value) {
    if (value == null || value.isEmpty()) {
      throw new DomainViolationException(compose(field, violation));
    }
    if (value.charAt(0) == edgeChar || value.charAt(value.length() - 1) == edgeChar) {
      throw new DomainViolationException(compose(field, violation));
    }
  }
}
