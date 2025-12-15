package io.github.blueprintplatform.codegen.domain.model.value.architecture;

public record ArchitectureGovernance(EnforcementMode mode) {

  public ArchitectureGovernance {
    if (mode == null) {
      mode = EnforcementMode.NONE;
    }
  }

  public static ArchitectureGovernance none() {
    return new ArchitectureGovernance(EnforcementMode.NONE);
  }

  public static ArchitectureGovernance basic() {
    return new ArchitectureGovernance(EnforcementMode.BASIC);
  }

  public static ArchitectureGovernance strict() {
    return new ArchitectureGovernance(EnforcementMode.STRICT);
  }

  public boolean isEnabled() {
    return mode.isEnabled();
  }

  public boolean isStrict() {
    return mode == EnforcementMode.STRICT;
  }

  public boolean isBasic() {
    return mode == EnforcementMode.BASIC;
  }
}
