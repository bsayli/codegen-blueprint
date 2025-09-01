package io.github.bsayli.codegen.initializr.projectgeneration.configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import io.github.bsayli.codegen.initializr.projectgeneration.configuration.properties.FreeMarkerTemplateProperties;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FreeMarkerTemplateConfigurationTest {

  @Autowired private FreeMarkerTemplateConfiguration configuration;

  @Test
  void testFreemarkerTemplateConfiguration_withValidProperties()
      throws FreeMarkerConfigurationException {
    Configuration freeMarkerConfiguration =
        configuration.initializeFreeMarkerTemplateConfiguration();

    assertThat(freeMarkerConfiguration.getTemplateExceptionHandler())
        .isEqualTo(TemplateExceptionHandler.RETHROW_HANDLER);

    assertThat(freeMarkerConfiguration.getDefaultEncoding()).isEqualTo("UTF-8");
  }

  @Test
  void testFreemarkerTemplateConfiguration_withInvalidExceptionHandler_throwsException() {
    FreeMarkerTemplateProperties testMockProperties =
        new FreeMarkerTemplateProperties("UTF-8", "INVALID_HANDLER", "/templates");

    FreeMarkerTemplateConfiguration freeMarkerTemplateConfigurationLocal =
        new FreeMarkerTemplateConfiguration(testMockProperties);

    assertThrows(
        FreeMarkerConfigurationException.class,
        freeMarkerTemplateConfigurationLocal::initializeFreeMarkerTemplateConfiguration);
  }
}
