package io.github.bsayli.codegen.initializr.bootstrap.config;

import jakarta.validation.constraints.NotBlank;

public record TemplateDefinition(@NotBlank String template, @NotBlank String outputPath) {}
