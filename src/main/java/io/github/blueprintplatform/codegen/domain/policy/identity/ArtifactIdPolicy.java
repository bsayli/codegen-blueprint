package io.github.blueprintplatform.codegen.domain.policy.identity;

import static io.github.blueprintplatform.codegen.domain.error.code.ErrorKeys.compose;
import static io.github.blueprintplatform.codegen.domain.error.code.Field.ARTIFACT_ID;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.EDGE_CHAR;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.INVALID_CHARS;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.LENGTH;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.NOT_BLANK;
import static io.github.blueprintplatform.codegen.domain.error.code.Violation.STARTS_WITH_LETTER;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
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

  private static final ErrorCode CODE_NOT_BLANK = compose(ARTIFACT_ID, NOT_BLANK);
  private static final ErrorCode CODE_LENGTH = compose(ARTIFACT_ID, LENGTH);
  private static final ErrorCode CODE_INVALID_CHARS = compose(ARTIFACT_ID, INVALID_CHARS);
  private static final ErrorCode CODE_STARTS_WITH_LETTER = compose(ARTIFACT_ID, STARTS_WITH_LETTER);
  private static final ErrorCode CODE_EDGE_CHAR = compose(ARTIFACT_ID, EDGE_CHAR);

  private ArtifactIdPolicy() {}

  public static String enforce(String raw) {
    String n = normalize(raw);
    validate(n);
    return n;
  }

  private static String normalize(String raw) {
    if (raw == null) {
      throw new DomainViolationException(CODE_NOT_BLANK);
    }
    return raw.trim()
        .replaceAll("\\s+", "-")
        .replace('_', '-')
        .toLowerCase(Locale.ROOT)
        .replaceAll("-{2,}", "-");
  }

  private static void validate(String value) {
    Rule<String> rule =
        CompositeRule.of(
            new NotBlankRule(CODE_NOT_BLANK),
            new LengthBetweenRule(MIN, MAX, CODE_LENGTH),
            new AllowedCharsRule("[a-z0-9-]", CODE_INVALID_CHARS),
            new StartsWithLetterRule(CODE_STARTS_WITH_LETTER),
            new NoEdgeCharRule('-', CODE_EDGE_CHAR));
    rule.check(value);
  }
}
