package io.github.bsayli.codegen.initializr.domain.port.out;

import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildOptions;
import io.github.bsayli.codegen.initializr.domain.port.out.artifact.GeneratedFile;

public interface GitIgnorePort {
    GeneratedFile generate(BuildOptions buildOptions);
}