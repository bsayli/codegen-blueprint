package io.github.bsayli.codegen.initializr.domain.policy.identity;

import static io.github.bsayli.codegen.initializr.domain.error.code.ErrorKeys.compose;
import static io.github.bsayli.codegen.initializr.domain.error.code.Field.GROUP_ID;
import static io.github.bsayli.codegen.initializr.domain.error.code.Violation.*;

import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.policy.rule.DotSeparatedSegmentsRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.LengthBetweenRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.NotBlankRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.CompositeRule;
import io.github.bsayli.codegen.initializr.domain.policy.rule.base.Rule;
import java.util.Locale;
import java.util.regex.Pattern;

public final class GroupIdPolicy {

  private static final int MIN = 3;
  private static final int MAX = 100;

  private static final Pattern SEGMENT = Pattern.compile("^[a-z][a-z0-9]*$");

  private GroupIdPolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null) throw new DomainViolationException(compose(GROUP_ID, NOT_BLANK));
    return raw.trim().replaceAll("\\s+", "").toLowerCase(Locale.ROOT);
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new NotBlankRule(GROUP_ID),
            new LengthBetweenRule(MIN, MAX, GROUP_ID),
            new DotSeparatedSegmentsRule(SEGMENT, GROUP_ID, SEGMENT_FORMAT));
    rule.check(value);
  }
}
