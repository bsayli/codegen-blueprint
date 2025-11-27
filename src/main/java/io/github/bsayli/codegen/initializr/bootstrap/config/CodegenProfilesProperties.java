package io.github.bsayli.codegen.initializr.bootstrap.config;

import io.github.bsayli.codegen.initializr.adapter.out.profile.ProfileType;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.bootstrap.error.exception.ProfileConfigurationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "codegen")
public record CodegenProfilesProperties(@Valid @NotNull Map<String, ProfileProperties> profiles) {

  public ArtifactDefinition artifact(ProfileType profile, ArtifactKey artifactKey) {
    var profileProps = requireProfile(profile);
    return requireArtifact(profile, profileProps, artifactKey);
  }

  public ProfileProperties requireProfile(ProfileType profile) {
    var key = profile.key();
    var profileProps = profiles.get(key);
    if (profileProps == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_PROFILE_NOT_FOUND, key);
    }
    return profileProps;
  }

  ArtifactDefinition requireArtifact(
      ProfileType profile, ProfileProperties profileProps, ArtifactKey artifactKey) {

    ArtifactDefinition artifact = profileProps.artifacts().get(artifactKey.key());
    if (artifact == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_ARTIFACT_NOT_FOUND, artifactKey.key(), profile.key());
    }

    String basePath = profileProps.templateBasePath();

    if (basePath == null || basePath.isBlank()) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_TEMPLATE_BASE_MISSING, profile.key());
    }

    return new ArtifactDefinition(basePath, artifact.templates());
  }
}
