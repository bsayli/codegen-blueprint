package io.github.bsayli.codegen.initializr.bootstrap.freemarker;

import static freemarker.template.Configuration.VERSION_2_3_34;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.Version;
import org.springframework.context.annotation.Bean;

// @org.springframework.context.annotation.Configuration
// @EnableConfigurationProperties(FreeMarkerTemplateProperties.class)
public class FreeMarkerTemplateConfiguration {

  public static final String NUMBER_FORMAT_COMPUTER = "computer";

  private static final Version FM_VER = VERSION_2_3_34;

  private final FreeMarkerTemplateProperties props;

  public FreeMarkerTemplateConfiguration(FreeMarkerTemplateProperties props) {
    this.props = props;
  }

  @Bean
  Configuration freemarkerConfiguration() {
    Configuration cfg = new Configuration(FM_VER);
    cfg.setDefaultEncoding(props.encoding());
    cfg.setOutputEncoding(props.encoding());
    cfg.setClassForTemplateLoading(getClass(), props.templatePath());
    cfg.setTemplateExceptionHandler(toHandler(props.handler()));
    cfg.setLogTemplateExceptions(false);
    cfg.setWrapUncheckedExceptions(true);
    cfg.setLocalizedLookup(false);
    cfg.setNumberFormat(NUMBER_FORMAT_COMPUTER);
    cfg.setFallbackOnNullLoopVariable(false);

    cfg.setTemplateUpdateDelayMilliseconds(props.cacheEnabled() ? props.cacheUpdateDelayMs() : 0L);

    return cfg;
  }

  private TemplateExceptionHandler toHandler(FreeMarkerTemplateProperties.Handler h) {
    return switch (h) {
      case RETHROW -> TemplateExceptionHandler.RETHROW_HANDLER;
      case DEBUG -> TemplateExceptionHandler.DEBUG_HANDLER;
      case HTML_DEBUG -> TemplateExceptionHandler.HTML_DEBUG_HANDLER;
      case IGNORE -> TemplateExceptionHandler.IGNORE_HANDLER;
    };
  }
}
