package io.github.blueprintplatform.codegen.domain.model.value.tech;

import io.github.blueprintplatform.codegen.domain.model.value.tech.platform.PlatformTarget;
import io.github.blueprintplatform.codegen.domain.model.value.tech.stack.TechStack;

public record PlatformSpec(TechStack techStack, PlatformTarget platformTarget) {}
