package io.github.bsayli.codegen.initializr.bootstrap.config;

import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public record ProfileProperties(
    @NotBlank String templateBasePath,
    @Valid @NotNull List<ArtifactKey> orderedArtifactKeys,
    @Valid @NotNull Map<String, ArtifactDefinition> artifacts) {}
