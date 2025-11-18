package io.github.bsayli.codegen.initializr.domain.policy.naming;

import static io.github.bsayli.codegen.initializr.domain.error.code.Field.PROJECT_DESCRIPTION;
import static io.github.bsayli.codegen.initializr.domain.error.code.Violation.CONTROL_CHARS;

import io.github.bsayli.codegen.initializr.domain.policy.rule.LengthBetweenRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.RegexMatchRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.CompositeRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;
import java.util.Locale;
import java.util.regex.Pattern;

public final class ProjectDescriptionPolicy {

  private static final int MIN = 0;
  private static final int MAX = 280;

  private static final Pattern NO_CONTROL_CHARS = Pattern.compile("^\\P{Cntrl}*$");

  private ProjectDescriptionPolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null) return "";
    return raw.trim().replaceAll("\\s+", " ").toLowerCase(Locale.ROOT);
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new LengthBetweenRule(MIN, MAX, PROJECT_DESCRIPTION),
            new RegexMatchRule(NO_CONTROL_CHARS, PROJECT_DESCRIPTION, CONTROL_CHARS));
    rule.check(value);
  }
}
