package io.github.bsayli.codegen.initializr.projectgeneration.model;

import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.BuildTool;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.Framework;
import io.github.bsayli.codegen.initializr.projectgeneration.model.techstack.Language;

public record ProjectType(Framework framework, BuildTool buildTool, Language language) {}
