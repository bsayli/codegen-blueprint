package io.github.bsayli.codegen.initializr.adapter.out.profile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import io.github.bsayli.codegen.initializr.adapter.error.exception.ArtifactsPortNotFoundException;
import io.github.bsayli.codegen.initializr.adapter.error.exception.UnsupportedProfileTypeException;
import io.github.bsayli.codegen.initializr.application.port.out.ProjectArtifactsPort;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
class ProfileBasedArtifactsSelectorTest {

  @Test
  @DisplayName("Should throw UnsupportedProfileTypeException when ProfileType.from() returns null")
  void shouldThrowWhenProfileUnsupported() {
    TechStack options = mock(TechStack.class);

    ProfileBasedArtifactsSelector selector = new ProfileBasedArtifactsSelector(Map.of());

    assertThatThrownBy(() -> selector.select(options))
        .isInstanceOf(UnsupportedProfileTypeException.class);
  }

  @Test
  @DisplayName("Should throw ArtifactsPortNotFoundException when no port registered for type")
  void shouldThrowWhenPortMissing() {
    TechStack opts = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    ProfileType type = ProfileType.from(opts);
    assertThat(type).isNotNull();

    ProfileBasedArtifactsSelector selector =
        new ProfileBasedArtifactsSelector(Map.of()); // empty registry

    assertThatThrownBy(() -> selector.select(opts))
        .isInstanceOf(ArtifactsPortNotFoundException.class);
  }

  @Test
  @DisplayName("Should return registered ProjectArtifactsPort for matching profile")
  void shouldReturnMatchingPort() {
    TechStack opts = new TechStack(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

    ProfileType type = ProfileType.from(opts);

    ProjectArtifactsPort port = mock(ProjectArtifactsPort.class);

    ProfileBasedArtifactsSelector selector = new ProfileBasedArtifactsSelector(Map.of(type, port));

    ProjectArtifactsPort result = selector.select(opts);

    assertThat(result).isSameAs(port);
  }
}
