package io.github.blueprintplatform.codegen.domain.policy.naming;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.PROJECT_NAME;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.*;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.*;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.CompositeRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class ProjectNamePolicy {

  private static final int MIN = 3;
  private static final int MAX = 40;

  private static final Set<String> RESERVED_BASE = Set.of("con", "prn", "aux", "nul");
  private static final Set<String> RESERVED_NAMES =
      Stream.concat(
              RESERVED_BASE.stream(),
              Stream.concat(
                  IntStream.rangeClosed(1, 9).mapToObj(i -> "com" + i),
                  IntStream.rangeClosed(1, 9).mapToObj(i -> "lpt" + i)))
          .map(s -> s.toLowerCase(Locale.ROOT))
          .collect(Collectors.toUnmodifiableSet());

  private ProjectNamePolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null) throw new DomainViolationException(compose(PROJECT_NAME, NOT_BLANK));
    return raw.trim()
        .replaceAll("\\s+", "-")
        .replace('_', '-')
        .toLowerCase(Locale.ROOT)
        .replaceAll("-{2,}", "-");
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new NotBlankRule(PROJECT_NAME),
            new LengthBetweenRule(MIN, MAX, PROJECT_NAME),
            new AllowedCharsRule("[a-z0-9-]", PROJECT_NAME, INVALID_CHARS),
            new StartsWithLetterRule(PROJECT_NAME, STARTS_WITH_LETTER),
            new NoEdgeCharRule('-', PROJECT_NAME, EDGE_CHAR),
            new ReservedNamesRule(RESERVED_NAMES, PROJECT_NAME));
    rule.check(value);
  }
}
