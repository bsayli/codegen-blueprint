package io.github.blueprintplatform.codegen.application.port.out.artifact;

import io.github.blueprintplatform.codegen.application.error.exception.UnknownArtifactKeyException;
import java.util.Arrays;

public enum ArtifactKey {
  BUILD_CONFIG("build-config"),
  BUILD_TOOL_METADATA("build-tool-metadata"),
  IGNORE_RULES("ignore-rules"),
  SOURCE_LAYOUT("source-layout"),
  APP_CONFIG("app-config"),
  MAIN_SOURCE_ENTRY_POINT("main-source-entrypoint"),
  TEST_SOURCE_ENTRY_POINT("test-source-entrypoint"),
  SAMPLE_CODE("sample-code"),
  PROJECT_DOCUMENTATION("project-documentation");

  private final String key;

  ArtifactKey(String key) {
    this.key = key;
  }

  public static ArtifactKey fromKey(String key) {
    return Arrays.stream(values())
        .filter(a -> a.key.equals(key))
        .findFirst()
        .orElseThrow(() -> new UnknownArtifactKeyException(key));
  }

  public String key() {
    return key;
  }

  @Override
  public String toString() {
    return key;
  }
}
