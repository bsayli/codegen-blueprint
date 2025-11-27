package io.github.bsayli.codegen.initializr.domain.policy.dependency;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;
import static io.github.bsayli.codegen.initializr.domain.error.code.Field.DEPENDENCY_VERSION;
import static io.github.bsayli.codegen.initializr.domain.error.code.Violation.INVALID_CHARS;
import static io.github.bsayli.codegen.initializr.domain.error.code.Violation.NOT_BLANK;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.policy.rule.LengthBetweenRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.RegexMatchRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.CompositeRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;
import java.util.regex.Pattern;

public final class DependencyVersionPolicy {

  private static final Pattern ALLOWED = Pattern.compile("^[A-Za-z0-9._\\-+\\[\\](),:{}$\\s]+$");

  private static final int MIN = 1;
  private static final int MAX = 100;

  private DependencyVersionPolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new DomainViolationException(compose(DEPENDENCY_VERSION, NOT_BLANK));
    }
    return raw.trim();
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new LengthBetweenRule(MIN, MAX, DEPENDENCY_VERSION),
            new RegexMatchRule(ALLOWED, DEPENDENCY_VERSION, INVALID_CHARS));
    rule.check(value);
  }
}
