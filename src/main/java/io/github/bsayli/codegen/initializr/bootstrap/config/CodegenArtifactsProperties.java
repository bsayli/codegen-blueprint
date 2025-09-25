package io.github.bsayli.codegen.initializr.bootstrap.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "codegen.artifacts")
public record CodegenArtifactsProperties(
    @Valid @NotNull @NestedConfigurationProperty ArtifactProperties gitignore,
    @Valid @NotNull @NestedConfigurationProperty ArtifactProperties mavenPom,
    @Valid @NotNull @NestedConfigurationProperty ArtifactProperties readme) {}
