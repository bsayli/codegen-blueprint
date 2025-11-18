package io.github.bsayli.codegen.initializr.domain.policy.rule;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;

import io.github.bsayli.codegen.initializr.domain.error.code.Field;
import io.github.bsayli.codegen.initializr.domain.error.code.Violation;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;
import java.util.Locale;
import java.util.Set;

public final class ReservedPrefixRule implements Rule<String> {
  private final Set<String> reservedPrefixesLower;
  private final Field field;
  private final Violation violation;

  public ReservedPrefixRule(Set<String> reservedPrefixes, Field field, Violation violation) {
    this.reservedPrefixesLower =
        reservedPrefixes.stream()
            .map(s -> s.toLowerCase(Locale.ROOT))
            .collect(java.util.stream.Collectors.toUnmodifiableSet());
    this.field = field;
    this.violation = violation;
  }

  @Override
  public void check(String value) {
    if (value == null) throw new DomainViolationException(compose(field, violation));
    String lower = value.toLowerCase(Locale.ROOT);
    for (String p : reservedPrefixesLower) {
      if (lower.equals(p) || lower.startsWith(p + ".")) {
        throw new DomainViolationException(compose(field, violation), p);
      }
    }
  }
}
