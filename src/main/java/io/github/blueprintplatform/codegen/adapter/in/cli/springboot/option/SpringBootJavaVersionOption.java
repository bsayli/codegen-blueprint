package io.github.blueprintplatform.codegen.adapter.in.cli.springboot.option;

import io.github.blueprintplatform.codegen.adapter.in.cli.shared.CliEnumParser;
import io.github.blueprintplatform.codegen.adapter.in.cli.shared.CliKeyedEnum;

public enum SpringBootJavaVersionOption implements CliKeyedEnum {
  JAVA_21("21"),
  JAVA_25("25");

  private static final String UNKNOWN_KEY = "adapter.cli.springboot.java-version.unknown";

  private final String value;

  SpringBootJavaVersionOption(String value) {
    this.value = value;
  }

  public static SpringBootJavaVersionOption fromKey(String raw) {
    return CliEnumParser.parse(SpringBootJavaVersionOption.class, raw, UNKNOWN_KEY);
  }

  public String value() {
    return value;
  }

  @Override
  public String key() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
