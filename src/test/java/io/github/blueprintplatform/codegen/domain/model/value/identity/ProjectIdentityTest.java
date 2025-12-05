package io.github.blueprintplatform.codegen.domain.model.value.identity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("unit")
@Tag("domain")
class ProjectIdentityTest {

  @Test
  @DisplayName("valid groupId and artifactId should be accepted")
  void validIdentity_shouldBeAccepted() {
    GroupId groupId = new GroupId("io.github.blueprintplatform");
    ArtifactId artifactId = new ArtifactId("demo-app");

    ProjectIdentity identity = new ProjectIdentity(groupId, artifactId);

    assertThat(identity.groupId()).isSameAs(groupId);
    assertThat(identity.artifactId()).isSameAs(artifactId);
  }

  @Test
  @DisplayName("null groupId should fail IDENTITY_REQUIRED")
  void nullGroupId_shouldFailIdentityRequired() {
    ArtifactId artifactId = new ArtifactId("demo-app");

    assertThatThrownBy(() -> new ProjectIdentity(null, artifactId))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.identity.not.blank");
            });
  }

  @Test
  @DisplayName("null artifactId should fail IDENTITY_REQUIRED")
  void nullArtifactId_shouldFailIdentityRequired() {
    GroupId groupId = new GroupId("io.github.blueprintplatform");

    assertThatThrownBy(() -> new ProjectIdentity(groupId, null))
        .isInstanceOf(DomainViolationException.class)
        .satisfies(
            ex -> {
              DomainViolationException dve = (DomainViolationException) ex;
              assertThat(dve.getMessageKey()).isEqualTo("project.identity.not.blank");
            });
  }
}
