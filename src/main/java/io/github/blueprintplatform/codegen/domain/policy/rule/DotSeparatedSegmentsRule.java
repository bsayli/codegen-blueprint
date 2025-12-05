package io.github.blueprintplatform.codegen.domain.policy.rule;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;

import io.github.blueprintplatform.codegen.domain.error.code.Field;
import io.github.blueprintplatform.codegen.domain.error.code.Violation;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class DotSeparatedSegmentsRule implements Rule<String> {
  private final Pattern segmentPattern;
  private final Field field;
  private final Violation violation;

  public DotSeparatedSegmentsRule(Pattern segmentPattern, Field field, Violation violation) {
    this.segmentPattern = segmentPattern;
    this.field = field;
    this.violation = violation;
  }

  @Override
  public void check(String value) {
    if (value == null) {
      throw new DomainViolationException(compose(field, violation));
    }
    String[] parts = value.split("\\.", -1);
    if (parts.length == 0) {
      throw new DomainViolationException(compose(field, violation));
    }
    for (String p : parts) {
      if (p.isEmpty() || !segmentPattern.matcher(p).matches()) {
        throw new DomainViolationException(compose(field, violation), p);
      }
    }
  }
}
