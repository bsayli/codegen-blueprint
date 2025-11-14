package io.github.bsayli.codegen.initializr.application.port.out.artifacts;

import io.github.bsayli.codegen.initializr.adapter.error.exception.UnknownArtifactKeyException;
import java.util.Arrays;

public enum ArtifactKey {
  POM("pom"),
  MAVEN_WRAPPER("maven-wrapper"),
  GITIGNORE("gitignore"),
  APPLICATION_YAML("application-yaml"),
  SOURCE_SCAFFOLDER("source-scaffolder"),
  TEST_SCAFFOLDER("test-scaffolder"),
  README("readme");

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
