package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class RegexMatchRule implements Rule<String> {
  private final Pattern pattern;
  private final ErrorCode errorCode;

  public RegexMatchRule(Pattern pattern, ErrorCode errorCode) {
    this.pattern = pattern;
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null || !pattern.matcher(value).matches()) {
      throw new DomainViolationException(errorCode);
    }
  }
}
