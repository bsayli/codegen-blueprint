package io.github.blueprintplatform.codegen.domain.policy.dependency;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.DEPENDENCY_VERSION;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.INVALID_CHARS;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.LENGTH;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.NOT_BLANK;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.LengthBetweenRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.RegexMatchRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.CompositeRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class DependencyVersionPolicy {

  private static final Pattern ALLOWED = Pattern.compile("^[A-Za-z0-9._\\-+\\[\\](),:{}$\\s]+$");

  private static final int MIN = 1;
  private static final int MAX = 100;

  private static final ErrorCode CODE_NOT_BLANK = compose(DEPENDENCY_VERSION, NOT_BLANK);
  private static final ErrorCode CODE_LENGTH = compose(DEPENDENCY_VERSION, LENGTH);
  private static final ErrorCode CODE_INVALID_CHARS = compose(DEPENDENCY_VERSION, INVALID_CHARS);

  private DependencyVersionPolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new DomainViolationException(CODE_NOT_BLANK);
    }
    return raw.trim();
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new LengthBetweenRule(MIN, MAX, CODE_LENGTH),
            new RegexMatchRule(ALLOWED, CODE_INVALID_CHARS));
    rule.check(value);
  }
}
