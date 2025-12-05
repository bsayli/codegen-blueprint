package io.github.blueprintplatform.codegen.domain.model.value.tech.platform;

import io.github.blueprintplatform.codegen.domain.error.code.ErrorCode;
import io.github.blueprintplatform.codegen.domain.error.exception.DomainViolationException;

public record SpringBootJvmTarget(JavaVersion java, SpringBootVersion springBoot)
    implements PlatformTarget {

  private static final ErrorCode TARGET_REQUIRED = () -> "platform.target.not.blank";

  public SpringBootJvmTarget {
    if (java == null || springBoot == null) {
      throw new DomainViolationException(TARGET_REQUIRED);
    }
  }
}
