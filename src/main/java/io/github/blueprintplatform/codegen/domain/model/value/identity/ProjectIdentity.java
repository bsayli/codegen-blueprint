package io.github.blueprintplatform.codegen.domain.model.value.identity;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;

public record ProjectIdentity(GroupId groupId, ArtifactId artifactId) {

  private static final ErrorCode IDENTITY_REQUIRED = () -> "project.identity.not.blank";

  public ProjectIdentity {
    if (groupId == null || artifactId == null) {
      throw new DomainViolationException(IDENTITY_REQUIRED);
    }
  }
}
