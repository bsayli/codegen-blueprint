package io.github.bsayli.codegen.initializr.domain.model.value.tech.platform;

import io.github.bsayli.codegen.initializr.domain.model.value.tech.options.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.policy.tech.CompatibilityPolicy;
import io.github.bsayli.codegen.initializr.domain.policy.tech.PlatformTargetSelector;

public final class PlatformTargetFactory {
  private PlatformTargetFactory() {}

  public static PlatformTarget of(
      BuildOptions options, JavaVersion preferredJava, SpringBootVersion preferredBoot) {
    PlatformTarget t =
        PlatformTargetSelector.selectOrDefault(options, preferredJava, preferredBoot);
    CompatibilityPolicy.ensureCompatible(options, t);
    return t;
  }

  public static PlatformTarget defaultFor(BuildOptions options) {
    PlatformTarget t = PlatformTargetSelector.selectDefaultFor(options);
    CompatibilityPolicy.ensureCompatible(options, t);
    return t;
  }
}
