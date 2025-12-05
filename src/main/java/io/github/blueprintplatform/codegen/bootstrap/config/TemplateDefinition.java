package io.github.blueprintplatform.codegen.bootstrap.config;

import jakarta.validation.constraints.NotBlank;

public record TemplateDefinition(@NotBlank String template, @NotBlank String outputPath) {}
