package io.github.blueprintplatform.codegen.domain.model.value.tech.platform;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.shared.KeyEnumParser;
import io.github.blueprintplatform.codegen.domain.shared.KeyedEnum;

public enum SpringBootVersion implements KeyedEnum {
  V3_5("3.5", "3.5.8"), // Latest known stable patch for 3.5.x
  V3_4("3.4", "3.4.12"); // Latest known stable patch for 3.4.x

  private static final ErrorCode UNKNOWN = () -> "platform.springboot-version.unknown";

  private final String key; // major.minor
  private final String defaultPatch; // full version, e.g. 3.5.8

  SpringBootVersion(String key, String defaultPatch) {
    this.key = key;
    this.defaultPatch = defaultPatch;
  }

  public static SpringBootVersion fromKey(String raw) {
    return KeyEnumParser.parse(SpringBootVersion.class, raw, UNKNOWN);
  }

  @Override
  public String key() {
    return key;
  }

  /** Major.minor representation, e.g. 3.5 */
  public String majorMinor() {
    return key;
  }

  /** Full default version, e.g. 3.5.8 */
  public String defaultVersion() {
    return defaultPatch;
  }

  @Override
  public String toString() {
    return defaultPatch;
  }
}
