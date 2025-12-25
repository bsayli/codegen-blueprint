package io.github.blueprintplatform.codegen.domain.error.code;

public enum Violation {
  NOT_BLANK(".not.blank"),
  LENGTH(".length"),
  INVALID_CHARS(".invalid.chars"),
  RESERVED(".reserved"),

  STARTS_WITH_LETTER(".starts.with.letter"),
  EDGE_CHAR(".edge.char"),
  CONSECUTIVE_CHAR(".consecutive.char"),
  SEGMENT_FORMAT(".segment.format"),
  RESERVED_PREFIX(".reserved.prefix"),
  CONTROL_CHARS(".control.chars");

  private final String suffix;

  Violation(String suffix) {
    this.suffix = suffix;
  }

  public String suffix() {
    return suffix;
  }
}
