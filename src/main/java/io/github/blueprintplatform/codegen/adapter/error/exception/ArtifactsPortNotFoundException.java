package io.github.blueprintplatform.codegen.adapter.error.exception;

import io.github.blueprintplatform.codegen.adapter.out.profile.ProfileType;

@SuppressWarnings("java:S110")
public final class ArtifactsPortNotFoundException extends AdapterException {

  private static final String KEY = "adapter.artifacts.port.not.found";
  private final ProfileType profileType;

  public ArtifactsPortNotFoundException(ProfileType profileType) {
    super(KEY, profileType.name());
    this.profileType = profileType;
  }

  public ProfileType getProfileType() {
    return profileType;
  }
}
