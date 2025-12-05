package io.github.blueprintplatform.codegen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "io.github.blueprintplatform.codegen")
public class CodegenBlueprintApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodegenBlueprintApplication.class, args);
  }
}
