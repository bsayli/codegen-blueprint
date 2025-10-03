package io.github.bsayli.codegen.initializr.bootstrap.config;

import io.github.bsayli.codegen.initializr.bootstrap.error.ProfileConfigurationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "codegen")
public record CodegenProfilesProperties(@Valid @NotNull Map<String, ProfileProperties> profiles) {

  public ArtifactProperties artifact(String profileKey, String artifactKey) {
    var profile = requireProfile(profileKey);
    var raw = requireArtifact(profileKey, profile, artifactKey);
    String fullTemplate = profile.templateBasePath() + "/" + raw.template();
    return new ArtifactProperties(raw.enabled(), fullTemplate, raw.outputPath());
  }

  private ArtifactProperties requireArtifact(
      String profileKey, ProfileProperties profile, String artifactKey) {
    var artifact = profile.artifacts().get(artifactKey);
    if (artifact == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_ARTIFACT_NOT_FOUND, artifactKey, profileKey);
    }
    return artifact;
  }

  private ProfileProperties requireProfile(String profileKey) {
    var profile = profiles.get(profileKey);
    if (profile == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_PROFILE_NOT_FOUND, profileKey);
    }
    return profile;
  }

  public record ProfileProperties(
      @NotBlank String templateBasePath,
      @Valid @NotNull Map<String, ArtifactProperties> artifacts) {}
}
