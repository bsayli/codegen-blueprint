package io.github.blueprintplatform.codegen.adapter.in.cli.springboot.dependency;

import io.github.blueprintplatform.codegen.adapter.error.exception.InvalidDependencyAliasException;

public enum SpringBootDependencyAlias {
  WEB(Constants.ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-web"),
  DATA_JPA(Constants.ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-data-jpa"),
  VALIDATION(Constants.ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-validation"),
  ACTUATOR(Constants.ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-actuator"),
  SECURITY(Constants.ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-starter-security"),
  DEVTOOLS(Constants.ORG_SPRINGFRAMEWORK_BOOT, "spring-boot-devtools");

  private final String groupId;
  private final String artifactId;

  SpringBootDependencyAlias(String groupId, String artifactId) {
    this.groupId = groupId;
    this.artifactId = artifactId;
  }

  public String groupId() {
    return groupId;
  }

  public String artifactId() {
    return artifactId;
  }

  public static SpringBootDependencyAlias fromKey(String raw) {
    if (raw == null || raw.isBlank()) {
      throw new InvalidDependencyAliasException(String.valueOf(raw));
    }

    String normalized = raw.trim();

    for (SpringBootDependencyAlias alias : values()) {
      if (alias.name().equalsIgnoreCase(normalized)) {
        return alias;
      }
    }
    throw new InvalidDependencyAliasException(raw);
  }

  private static class Constants {
    public static final String ORG_SPRINGFRAMEWORK_BOOT = "org.springframework.boot";
  }
}