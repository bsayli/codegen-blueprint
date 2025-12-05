package io.github.blueprintplatform.codegen.bootstrap.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ArtifactDefinition(
    String basePath, @Valid @NotNull List<TemplateDefinition> templates) {}
