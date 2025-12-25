package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class DotSeparatedSegmentsRule implements Rule<String> {
  private final Pattern segmentPattern;
  private final ErrorCode errorCode;

  public DotSeparatedSegmentsRule(Pattern segmentPattern, ErrorCode errorCode) {
    this.segmentPattern = segmentPattern;
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null) {
      throw new DomainViolationException(errorCode);
    }

    String[] parts = value.split("\\.", -1);
    if (parts.length == 0) {
      throw new DomainViolationException(errorCode);
    }

    for (String p : parts) {
      if (p.isEmpty() || !segmentPattern.matcher(p).matches()) {
        throw new DomainViolationException(errorCode, p);
      }
    }
  }
}
