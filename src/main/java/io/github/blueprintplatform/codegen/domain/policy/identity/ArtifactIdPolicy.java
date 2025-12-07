package io.github.blueprintplatform.codegen.domain.policy.identity;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.ARTIFACT_ID;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.*;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import io.github.blueprintplatform.codegen.domain.policy.rule.AllowedCharsRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.LengthBetweenRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.NoEdgeCharRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.NotBlankRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.StartsWithLetterRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.CompositeRule;
import io.github.blueprintplatform.codegen.domain.policy.rule.base.Rule;
import java.util.Locale;

public final class ArtifactIdPolicy {

  private static final int MIN = 3;
  private static final int MAX = 50;

  private ArtifactIdPolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null) throw new DomainViolationException(compose(ARTIFACT_ID, NOT_BLANK));
    return raw.trim()
        .replaceAll("\\s+", "-")
        .replace('_', '-')
        .toLowerCase(Locale.ROOT)
        .replaceAll("-{2,}", "-");
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new NotBlankRule(ARTIFACT_ID),
            new LengthBetweenRule(MIN, MAX, ARTIFACT_ID),
            new AllowedCharsRule("[a-z0-9-]", ARTIFACT_ID, INVALID_CHARS),
            new StartsWithLetterRule(ARTIFACT_ID, STARTS_WITH_LETTER),
            new NoEdgeCharRule('-', ARTIFACT_ID, EDGE_CHAR));
    rule.check(value);
  }
}
