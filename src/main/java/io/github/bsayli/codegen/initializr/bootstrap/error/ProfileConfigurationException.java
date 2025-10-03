package io.github.bsayli.codegen.initializr.bootstrap.error;

public final class ProfileConfigurationException extends InfrastructureException {
  public static final String KEY_PROFILE_NOT_FOUND = "bootstrap.profile.not.found";
  public static final String KEY_ARTIFACT_NOT_FOUND = "bootstrap.artifact.not.found";
  public static final String KEY_TEMPLATE_BASE_MISSING = "bootstrap.template.base.missing";

  public ProfileConfigurationException(String key, Object... args) {
    super(key, args);
  }

  public ProfileConfigurationException(String key, Throwable cause, Object... args) {
    super(key, cause, args);
  }
}
