package io.github.bsayli.codegen.initializr.bootstrap.freemarker;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "templating")
public record FreeMarkerTemplateProperties(
    @NotBlank String encoding,
    @NotNull Handler handler,
    @NotBlank String templatePath,
    boolean cacheEnabled,
    long cacheUpdateDelayMs) {
  public enum Handler {
    RETHROW,
    DEBUG,
    HTML_DEBUG,
    IGNORE
  }
}
