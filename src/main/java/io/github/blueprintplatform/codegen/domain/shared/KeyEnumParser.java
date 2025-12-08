package io.github.blueprintplatform.codegen.domain.shared;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;

public final class KeyEnumParser {

  private KeyEnumParser() {}

  public static <E extends Enum<E> & KeyedEnum> E parse(
      Class<E> type, String raw, ErrorCode unknownCode) {

    if (raw == null || raw.isBlank()) {
      throw new DomainViolationException(unknownCode, raw);
    }

    String normalized = raw.trim().toLowerCase();

    for (E e : type.getEnumConstants()) {
      if (e.key().equalsIgnoreCase(normalized)) {
        return e;
      }
    }

    throw new DomainViolationException(unknownCode, normalized);
  }
}
