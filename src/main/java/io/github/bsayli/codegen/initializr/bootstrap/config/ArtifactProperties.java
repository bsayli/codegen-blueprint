package io.github.bsayli.codegen.initializr.bootstrap.config;

import jakarta.validation.constraints.NotBlank;

public record ArtifactProperties(
    boolean enabled, @NotBlank String template, @NotBlank String outputPath) {}
