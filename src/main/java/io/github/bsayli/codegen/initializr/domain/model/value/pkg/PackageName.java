package io.github.bsayli.codegen.initializr.domain.model.value.pkg;

import io.github.bsayli.codegen.initializr.domain.policy.pkg.PackageNamePolicy;

public record PackageName(String value) {
  public PackageName {
    value = PackageNamePolicy.enforce(value);
  }
}
