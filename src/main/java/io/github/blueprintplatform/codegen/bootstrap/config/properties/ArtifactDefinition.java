package io.github.blueprintplatform.codegen.bootstrap.config.properties;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ArtifactDefinition(
    String templateBasePath, @Valid @NotNull List<TemplateDefinition> templates) {}
