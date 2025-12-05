package io.github.blueprintplatform.codegen.domain.model.value.pkg;

import io.github.blueprintplatform.codegen.domain.policy.pkg.PackageNamePolicy;

public record PackageName(String value) {
  public PackageName {
    value = PackageNamePolicy.enforce(value);
  }
}
