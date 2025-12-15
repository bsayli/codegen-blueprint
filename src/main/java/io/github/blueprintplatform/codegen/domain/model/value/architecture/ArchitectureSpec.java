package io.github.blueprintplatform.codegen.domain.model.value.architecture;

import io.github.blueprintplatform.codegen.domain.model.value.layout.ProjectLayout;
import io.github.blueprintplatform.codegen.domain.model.value.sample.SampleCodeOptions;

public record ArchitectureSpec(
    ProjectLayout layout, ArchitectureGovernance governance, SampleCodeOptions sampleCodeOptions) {}
