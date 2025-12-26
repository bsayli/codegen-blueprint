package io.github.blueprintplatform.codegen.bootstrap.error.exception;

public final class ProfileConfigurationException extends BootstrapException {
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
