package io.github.blueprintplatform.codegen.bootstrap.config.registry;

import io.github.blueprintplatform.codegen.application.port.out.artifact.ArtifactKey;
import io.github.blueprintplatform.codegen.bootstrap.config.properties.ArtifactDefinition;
import io.github.blueprintplatform.codegen.bootstrap.config.properties.CodegenProfilesProperties;
import io.github.blueprintplatform.codegen.bootstrap.config.properties.ProfileProperties;
import io.github.blueprintplatform.codegen.bootstrap.error.exception.ProfileConfigurationException;
import org.springframework.stereotype.Component;

@Component
public class CodegenProfilesRegistry {

  private final CodegenProfilesProperties properties;

  public CodegenProfilesRegistry(CodegenProfilesProperties properties) {
    this.properties = properties;
  }

  public ProfileProperties requireProfile(String profileKey) {
    var profileProps = properties.profiles().get(profileKey);
    if (profileProps == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_PROFILE_NOT_FOUND, profileKey);
    }
    return profileProps;
  }

  public ArtifactDefinition requireArtifact(String profileKey, ArtifactKey artifactKey) {
    var profileProps = requireProfile(profileKey);

    var artifact = profileProps.artifacts().get(artifactKey.key());
    if (artifact == null) {
      throw new ProfileConfigurationException(
          ProfileConfigurationException.KEY_ARTIFACT_NOT_FOUND, artifactKey.key(), profileKey);
    }

    if (!artifact.templates().isEmpty()) {
      String basePath = artifact.templateBasePath();
      if (basePath == null || basePath.isBlank()) {
        throw new ProfileConfigurationException(
            ProfileConfigurationException.KEY_TEMPLATE_BASE_MISSING, artifactKey.key(), profileKey);
      }
    }

    return artifact;
  }
}
