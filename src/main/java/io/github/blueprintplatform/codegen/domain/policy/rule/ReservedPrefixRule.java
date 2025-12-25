package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class ReservedPrefixRule implements Rule<String> {
  private final Set<String> reservedPrefixesLower;
  private final ErrorCode errorCode;

  public ReservedPrefixRule(Set<String> reservedPrefixes, ErrorCode errorCode) {
    this.reservedPrefixesLower =
        reservedPrefixes.stream()
            .map(s -> s.toLowerCase(Locale.ROOT))
            .collect(Collectors.toUnmodifiableSet());
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null) {
      throw new DomainViolationException(errorCode);
    }
    String lower = value.toLowerCase(Locale.ROOT);
    for (String p : reservedPrefixesLower) {
      if (lower.equals(p) || lower.startsWith(p + ".")) {
        throw new DomainViolationException(errorCode, p);
      }
    }
  }
}
