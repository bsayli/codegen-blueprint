package io.github.blueprintplatform.codegen.bootstrap.config;

import jakarta.validation.constraints.NotBlank;

public record SamplesProperties(
    @NotBlank String standard,
    @NotBlank String hexagonal,
    @NotBlank String basicDirName,
    @NotBlank String richDirName) {}
