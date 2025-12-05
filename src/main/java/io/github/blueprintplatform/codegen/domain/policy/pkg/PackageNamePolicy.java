package io.github.blueprintplatform.codegen.domain.policy.pkg;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.PACKAGE_NAME;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.*;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.DotSeparatedSegmentsRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.LengthBetweenRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.NotBlankRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.ReservedPrefixRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.CompositeRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

public final class PackageNamePolicy {

  private static final int MIN = 3;
  private static final int MAX = 255;

  private static final Pattern SEGMENT = Pattern.compile("^[a-z][a-z0-9]*$");

  private static final Pattern SEP_CHARS = Pattern.compile("[\\s_\\-]+");
  private static final Pattern MULTI_DOTS = Pattern.compile("\\.{2,}");
  private static final Pattern LEADING_DOTS = Pattern.compile("^\\.+");
  private static final Pattern TRAILING_DOTS = Pattern.compile("\\.+$");

  private static final Set<String> RESERVED_PREFIXES = Set.of("java", "javax", "sun", "com.sun");

  private PackageNamePolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null) throw new DomainViolationException(compose(PACKAGE_NAME, NOT_BLANK));

    String s = raw.trim();
    s = SEP_CHARS.matcher(s).replaceAll(".");
    s = MULTI_DOTS.matcher(s).replaceAll(".");
    s = LEADING_DOTS.matcher(s).replaceAll("");
    s = TRAILING_DOTS.matcher(s).replaceAll("");
    s = s.toLowerCase(Locale.ROOT);
    return s;
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new NotBlankRule(PACKAGE_NAME),
            new LengthBetweenRule(MIN, MAX, PACKAGE_NAME),
            new DotSeparatedSegmentsRule(SEGMENT, PACKAGE_NAME, SEGMENT_FORMAT),
            new ReservedPrefixRule(RESERVED_PREFIXES, PACKAGE_NAME, RESERVED_PREFIX));
    rule.check(value);
  }
}
