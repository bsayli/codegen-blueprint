package io.github.bsayli.codegen.initializr.domain.policy.rule;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;

import io.github.bsayli.codegen.initializr.domain.error.code.Field;
import io.github.bsayli.codegen.initializr.domain.error.code.Violation;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;
import java.util.Objects;
import java.util.regex.Pattern;

public final class AllowedCharsRule implements Rule<String> {
  private final Pattern allowed;
  private final Field field;
  private final Violation violation;

  public AllowedCharsRule(String allowedCharClassRegex, Field field, Violation violation) {
    Objects.requireNonNull(allowedCharClassRegex);
    this.allowed = Pattern.compile("^(?:" + allowedCharClassRegex + ")+$");
    this.field = field;
    this.violation = violation;
  }

  @Override
  public void check(String value) {
    if (value == null || !allowed.matcher(value).matches()) {
      throw new DomainViolationException(compose(field, violation));
    }
  }
}
