package io.github.bsayli.codegen.initializr.domain.model.value.dependency;

import io.github.bsayli.codegen.initializr.domain.error.code.ErrorCode;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.ArtifactId;
import io.github.bsayli.codegen.initializr.domain.model.value.identity.GroupId;

public record DependencyCoordinates(GroupId groupId, ArtifactId artifactId) {

  private static final ErrorCode COORDINATES_REQUIRED = () -> "dependency.coordinates.not.blank";

  public DependencyCoordinates {
    if (groupId == null || artifactId == null) {
      throw new DomainViolationException(COORDINATES_REQUIRED);
    }
  }
}
