package io.github.blueprintplatform.codegen.adapter.shared.naming;

import java.util.regex.Pattern;

public final class StringCaseFormatter {

  private static final String EMPTY = "";
  private static final Pattern NON_ALPHANUMERIC_DELIMITER = Pattern.compile("[^A-Za-z0-9]+");

  public String toPascalCase(String raw) {
    if (raw == null || raw.isBlank()) return EMPTY;

    String[] parts = NON_ALPHANUMERIC_DELIMITER.split(raw.trim());
    StringBuilder sb = new StringBuilder(parts.length * 8);

    for (String part : parts) {
      if (part.isEmpty()) continue;
      sb.append(Character.toUpperCase(part.charAt(0)));
      if (part.length() > 1) sb.append(part.substring(1).toLowerCase());
    }

    return sb.toString();
  }
}
