package io.github.bsayli.codegen.initializr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "io.github.bsayli.codegen.initializr")
public class CodegenSpringbootInitializrApplication {

  public static void main(String[] args) {
    SpringApplication.run(CodegenSpringbootInitializrApplication.class, args);
  }
}
