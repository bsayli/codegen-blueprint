package io.github.blueprintplatform.codegen.domain.policy.naming;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.PROJECT_DESCRIPTION;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.CONTROL_CHARS;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.LENGTH;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.NOT_BLANK;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.LengthBetweenRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.NotBlankRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.RegexMatchRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.CompositeRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class ProjectDescriptionPolicy {

  private static final int MIN = 10;
  private static final int MAX = 280;

  private static final Pattern NO_CONTROL_CHARS = Pattern.compile("^\\P{Cntrl}*$");

  private static final ErrorCode CODE_NOT_BLANK = compose(PROJECT_DESCRIPTION, NOT_BLANK);
  private static final ErrorCode CODE_LENGTH = compose(PROJECT_DESCRIPTION, LENGTH);
  private static final ErrorCode CODE_CONTROL_CHARS = compose(PROJECT_DESCRIPTION, CONTROL_CHARS);

  private ProjectDescriptionPolicy() {}

  public static String enforce(String raw) {
    if (raw == null) {
      throw new DomainViolationException(CODE_NOT_BLANK);
    }

    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    return raw.trim().replaceAll("\\s+", " ");
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new NotBlankRule(CODE_NOT_BLANK),
            new LengthBetweenRule(MIN, MAX, CODE_LENGTH),
            new RegexMatchRule(NO_CONTROL_CHARS, CODE_CONTROL_CHARS));
    rule.check(value);
  }
}
