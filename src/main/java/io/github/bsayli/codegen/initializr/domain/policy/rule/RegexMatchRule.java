package io.github.bsayli.codegen.initializr.domain.policy.rule;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;

import io.github.bsayli.codegen.initializr.domain.error.code.Field;
import io.github.bsayli.codegen.initializr.domain.error.code.Violation;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class RegexMatchRule implements Rule<String> {
  private final Pattern pattern;
  private final Field field;
  private final Violation violation;

  public RegexMatchRule(Pattern pattern, Field field, Violation violation) {
    this.pattern = pattern;
    this.field = field;
    this.violation = violation;
  }

  @Override
  public void check(String value) {
    if (value == null || !pattern.matcher(value).matches()) {
      throw new DomainViolationException(compose(field, violation));
    }
  }
}
