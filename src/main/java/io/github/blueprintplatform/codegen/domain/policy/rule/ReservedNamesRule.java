package io.github.blueprintplatform.codegen.domain.policy.rule;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;

import io.github.blueprintplatform.codegen.domain.error.code.Field;
import io.github.blueprintplatform.codegen.domain.error.code.Violation;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class ReservedNamesRule implements Rule<String> {
  private final Set<String> reservedLower;
  private final Field field;

  public ReservedNamesRule(Set<String> reserved, Field field) {
    this.reservedLower =
        reserved.stream()
            .map(s -> s.toLowerCase(Locale.ROOT))
            .collect(Collectors.toUnmodifiableSet());
    this.field = field;
  }

  @Override
  public void check(String value) {
    if (value == null) throw new DomainViolationException(compose(field, Violation.RESERVED));
    String lower = value.toLowerCase(Locale.ROOT);
    if (reservedLower.contains(lower)) {
      throw new DomainViolationException(compose(field, Violation.RESERVED), value);
    }
  }
}
