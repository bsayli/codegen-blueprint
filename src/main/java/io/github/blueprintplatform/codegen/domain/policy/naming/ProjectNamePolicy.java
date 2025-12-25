package io.github.blueprintplatform.codegen.domain.policy.naming;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.PROJECT_NAME;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.INVALID_CHARS;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.LENGTH;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.NOT_BLANK;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.AllowedCharsRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.LengthBetweenRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.NotBlankRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.CompositeRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;

public final class ProjectNamePolicy {

  private static final int MIN = 3;
  private static final int MAX = 60;

  private static final String ALLOWED_CHARS = "[A-Za-z0-9 .,_'()\\-]";

  private static final ErrorCode CODE_NOT_BLANK = compose(PROJECT_NAME, NOT_BLANK);
  private static final ErrorCode CODE_LENGTH = compose(PROJECT_NAME, LENGTH);
  private static final ErrorCode CODE_INVALID_CHARS = compose(PROJECT_NAME, INVALID_CHARS);

  private ProjectNamePolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null) {
      throw new DomainViolationException(CODE_NOT_BLANK);
    }
    return raw.trim();
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new NotBlankRule(CODE_NOT_BLANK),
            new LengthBetweenRule(MIN, MAX, CODE_LENGTH),
            new AllowedCharsRule(ALLOWED_CHARS, CODE_INVALID_CHARS));

    rule.check(value);
  }
}
