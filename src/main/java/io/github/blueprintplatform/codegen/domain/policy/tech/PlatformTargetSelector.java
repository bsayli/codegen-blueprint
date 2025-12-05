package io.github.blueprintplatform.codegen.domain.policy.tech;

import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.JavaVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootJvmTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.SpringBootVersion;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;
import java.util.List;

public final class PlatformTargetSelector {

  private PlatformTargetSelector() {}

  public static PlatformTarget select(
      TechStack techStack, JavaVersion preferredJava, SpringBootVersion preferredBoot) {

    var requested = new SpringBootJvmTarget(preferredJava, preferredBoot);
    CompatibilityPolicy.ensureCompatible(techStack, requested);
    return requested;
  }

  public static List<PlatformTarget> supportedTargetsFor() {
    return CompatibilityPolicy.allSupportedTargets();
  }
}
