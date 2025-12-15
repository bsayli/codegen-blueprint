package io.github.blueprintplatform.codegen.bootstrap.config;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.bootstrap.error.exception.ProfileConfigurationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "codegen")
public record CodegenProfilesProperties(@Valid @NotNull Map<String, ProfileProperties> profiles) {

  public ArtifactDefinition artifact(String profileKey, ArtifactKey artifactKey) {
    var profileProps = requireProfile(profileKey);
    return requireArtifact(profileKey, profileProps, artifactKey);
  }

  public ProfileProperties requireProfile(String profileKey) {
    var profileProps = profiles.get(profileKey);
    if (profileProps == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_PROFILE_NOT_FOUND, profileKey);
    }
    return profileProps;
  }

  ArtifactDefinition requireArtifact(
      String profileKey, ProfileProperties profileProps, ArtifactKey artifactKey) {

    ArtifactDefinition artifact = profileProps.artifacts().get(artifactKey.key());
    if (artifact == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_ARTIFACT_NOT_FOUND, artifactKey.key(), profileKey);
    }

    String basePath = profileProps.templateBasePath();

    if (basePath == null || basePath.isBlank()) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_TEMPLATE_BASE_MISSING, profileKey);
    }

    return new ArtifactDefinition(basePath, artifact.templates());
  }
}
