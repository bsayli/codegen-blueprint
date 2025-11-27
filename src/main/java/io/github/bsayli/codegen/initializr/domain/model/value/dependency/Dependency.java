package io.github.bsayli.codegen.initializr.domain.model.value.dependency;

import io.github.bsayli.codegen.initializr.domain.error.code.ErrorCode;
import io.github.bsayli.codegen.initializr.domain.error.exception.DomainViolationException;

public record Dependency(
    DependencyCoordinates coordinates, DependencyVersion version, DependencyScope scope) {

  private static final ErrorCode COORDINATES_REQUIRED = () -> "dependency.coordinates.not.blank";

  public Dependency {
    if (coordinates == null) {
      throw new DomainViolationException(COORDINATES_REQUIRED);
    }
  }

  public boolean isDefaultScope() {
    return scope == null || scope == DependencyScope.COMPILE;
  }
}
