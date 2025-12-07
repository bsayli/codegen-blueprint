package io.github.blueprintplatform.codegen.domain.policy.naming;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.PROJECT_DESCRIPTION;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.CONTROL_CHARS;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.NOT_BLANK;

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

  private ProjectDescriptionPolicy() {}

  public static String enforce(String raw) {
    if (raw == null) {
      throw new DomainViolationException(compose(PROJECT_DESCRIPTION, NOT_BLANK));
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
                    new NotBlankRule(PROJECT_DESCRIPTION),
                    new LengthBetweenRule(MIN, MAX, PROJECT_DESCRIPTION),
                    new RegexMatchRule(NO_CONTROL_CHARS, PROJECT_DESCRIPTION, CONTROL_CHARS));
    rule.check(value);
  }
}