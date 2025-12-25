package io.github.blueprintplatform.codegen.domain.policy.rule;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class AllowedCharsRule implements Rule<String> {
  private final Pattern allowed;
  private final ErrorCode errorCode;

  public AllowedCharsRule(String allowedCharClassRegex, ErrorCode errorCode) {
    this.allowed = Pattern.compile("^(?:" + allowedCharClassRegex + ")+$");
    this.errorCode = errorCode;
  }

  @Override
  public void check(String value) {
    if (value == null || !allowed.matcher(value).matches()) {
      throw new DomainViolationException(errorCode);
    }
  }
}
