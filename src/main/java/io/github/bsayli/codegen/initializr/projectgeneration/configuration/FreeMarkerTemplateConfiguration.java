package io.github.bsayli.codegen.initializr.projectgeneration.configuration;

import io.github.bsayli.codegen.initializr.projectgeneration.configuration.properties.FreeMarkerTemplateProperties;
import freemarker.template.TemplateExceptionHandler;
import java.io.Serial;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(FreeMarkerTemplateProperties.class)
public class FreeMarkerTemplateConfiguration {

  private final FreeMarkerTemplateProperties freeMarkerProperties;

  public FreeMarkerTemplateConfiguration(FreeMarkerTemplateProperties freeMarkerProperties) {
    this.freeMarkerProperties = freeMarkerProperties;
  }

  @Bean
  freemarker.template.Configuration freemarkerTemplateConfiguration()
      throws FreeMarkerConfigurationException {
    return initializeFreeMarkerTemplateConfiguration();
  }

  public freemarker.template.Configuration initializeFreeMarkerTemplateConfiguration() {
    freemarker.template.Configuration configuration =
        new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_32);
    configuration.setDefaultEncoding(freeMarkerProperties.encoding());
    setTemplateExceptionHandler(configuration);
    configuration.setClassForTemplateLoading(this.getClass(), freeMarkerProperties.templatePath());
    return configuration;
  }

  private void setTemplateExceptionHandler(freemarker.template.Configuration configuration) {
    try {
      TemplateExceptionHandler exceptionHandler =
          switch (freeMarkerProperties.templateExceptionHandler()) {
            case "RETHROW_HANDLER" -> TemplateExceptionHandler.RETHROW_HANDLER;
            case "DEBUG_HANDLER" -> TemplateExceptionHandler.DEBUG_HANDLER;
            case "HTML_DEBUG_HANDLER" -> TemplateExceptionHandler.HTML_DEBUG_HANDLER;
            case "IGNORE_HANDLER" -> TemplateExceptionHandler.IGNORE_HANDLER;
            default ->
                throw new IllegalArgumentException(
                    "Invalid exception handler name: "
                        + freeMarkerProperties.templateExceptionHandler());
          };
      configuration.setTemplateExceptionHandler(exceptionHandler);
    } catch (IllegalArgumentException e) {
      throw new FreeMarkerConfigurationException(
          "Invalid templateExceptionHandler value: "
              + freeMarkerProperties.templateExceptionHandler(),
          e);
    }
  }
}

class FreeMarkerConfigurationException extends RuntimeException {

  @Serial private static final long serialVersionUID = 4482627787641879716L;

  public FreeMarkerConfigurationException(String message, Exception e) {
    super(message, e);
  }
}
