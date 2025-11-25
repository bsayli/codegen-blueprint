package io.github.bsayli.codegen.initializr.bootstrap.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.bsayli.codegen.initializr.adapter.out.profile.ProfileType;
import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import io.github.bsayli.codegen.initializr.bootstrap.error.exception.ProfileConfigurationException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("bootstrap")
@DisplayName("Unit Test: CodegenProfilesProperties")
class CodegenProfilesPropertiesTest {

  private static final ProfileType PROFILE = ProfileType.SPRINGBOOT_MAVEN_JAVA;
  private static final ArtifactKey ARTIFACT_KEY = ArtifactKey.POM;
  private static final String PROFILE_KEY = PROFILE.key();
  private static final String ARTIFACT_MAP_KEY = ARTIFACT_KEY.key();
  private static final String TEMPLATE_BASE_PATH = "springboot/maven/java/";

  private static CodegenProfilesProperties getCodegenProfilesProperties() {
    TemplateDefinition templateDefinition = new TemplateDefinition("pom.ftl", "pom.xml");

    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition(null, List.of(templateDefinition));

    ProfileProperties profileProperties =
        new ProfileProperties(
            "  ", List.of(ARTIFACT_KEY), Map.of(ARTIFACT_MAP_KEY, artifactDefinition));

    return new CodegenProfilesProperties(Map.of(PROFILE_KEY, profileProperties));
  }

  @Test
  @DisplayName(
      "artifact() should return ArtifactDefinition with profile basePath and artifact templates")
  void artifact_shouldReturnDefinitionWithProfileBasePathAndArtifactTemplates() {
    TemplateDefinition templateDefinition = new TemplateDefinition("pom.ftl", "pom.xml");

    ArtifactDefinition artifactDefinition =
        new ArtifactDefinition("artifact-specific/", List.of(templateDefinition));

    ProfileProperties profileProperties =
        new ProfileProperties(
            TEMPLATE_BASE_PATH,
            List.of(ARTIFACT_KEY),
            Map.of(ARTIFACT_MAP_KEY, artifactDefinition));

    CodegenProfilesProperties properties =
        new CodegenProfilesProperties(Map.of(PROFILE_KEY, profileProperties));

    ArtifactDefinition result = properties.artifact(PROFILE, ARTIFACT_KEY);

    assertThat(result.basePath()).isEqualTo(TEMPLATE_BASE_PATH);
    assertThat(result.templates()).isSameAs(artifactDefinition.templates());
  }

  @Test
  @DisplayName(
      "requireProfile() should throw ProfileConfigurationException when profile is missing")
  void requireProfile_shouldThrowWhenProfileMissing() {
    CodegenProfilesProperties properties = new CodegenProfilesProperties(Map.of());

    assertThatThrownBy(() -> properties.requireProfile(PROFILE))
        .isInstanceOfSatisfying(
            ProfileConfigurationException.class,
            ex -> {
              assertThat(ex.getMessageKey())
                  .isEqualTo(ProfileConfigurationException.KEY_PROFILE_NOT_FOUND);
              assertThat(ex.getArgs()).containsExactly(PROFILE_KEY);
            });
  }

  @Test
  @DisplayName("artifact() should throw ProfileConfigurationException when artifact is missing")
  void artifact_shouldThrowWhenArtifactMissing() {
    ProfileProperties profileProperties =
        new ProfileProperties(TEMPLATE_BASE_PATH, List.of(ARTIFACT_KEY), Map.of());

    CodegenProfilesProperties properties =
        new CodegenProfilesProperties(Map.of(PROFILE_KEY, profileProperties));

    assertThatThrownBy(() -> properties.artifact(PROFILE, ARTIFACT_KEY))
        .isInstanceOfSatisfying(
            ProfileConfigurationException.class,
            ex -> {
              assertThat(ex.getMessageKey())
                  .isEqualTo(ProfileConfigurationException.KEY_ARTIFACT_NOT_FOUND);
              assertThat(ex.getArgs()).containsExactly(ARTIFACT_MAP_KEY, PROFILE_KEY);
            });
  }

  @Test
  @DisplayName(
      "artifact() should throw ProfileConfigurationException when templateBasePath is blank")
  void artifact_shouldThrowWhenTemplateBasePathBlank() {
    CodegenProfilesProperties properties = getCodegenProfilesProperties();

    assertThatThrownBy(() -> properties.artifact(PROFILE, ARTIFACT_KEY))
        .isInstanceOfSatisfying(
            ProfileConfigurationException.class,
            ex -> {
              assertThat(ex.getMessageKey())
                  .isEqualTo(ProfileConfigurationException.KEY_TEMPLATE_BASE_MISSING);
              assertThat(ex.getArgs()).containsExactly(PROFILE_KEY);
            });
  }
}
