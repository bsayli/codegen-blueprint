package io.github.bsayli.codegen.initializr.adapter.out.profile;

import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.BuildTool;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Framework;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.Language;
import io.github.bsayli.codegen.initializr.domain.model.value.tech.stack.TechStack;

public enum ProfileType {
  SPRINGBOOT_MAVEN_JAVA(Framework.SPRING_BOOT, BuildTool.MAVEN, Language.JAVA);

  private final Framework framework;
  private final BuildTool buildTool;
  private final Language language;

  ProfileType(Framework framework, BuildTool buildTool, Language language) {
    this.framework = framework;
    this.buildTool = buildTool;
    this.language = language;
  }

  public static ProfileType from(TechStack o) {
    for (ProfileType p : values()) {
      if (p.framework == o.framework()
          && p.buildTool == o.buildTool()
          && p.language == o.language()) {
        return p;
      }
    }
    return null;
  }

  private static String slug(Enum<?> e) {
    return e.name().toLowerCase().replace("_", "");
  }

  public String key() {
    return slug(framework) + "-" + slug(buildTool) + "-" + slug(language);
  }
}
