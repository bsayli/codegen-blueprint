package io.github.bsayli.codegen.initializr.bootstrap.config;

import io.github.bsayli.codegen.initializr.application.port.out.artifact.ArtifactKey;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class ArtifactKeyConverter implements Converter<String, ArtifactKey> {
  @Override
  public ArtifactKey convert(@NonNull String source) {
    return ArtifactKey.fromKey(source);
  }
}
