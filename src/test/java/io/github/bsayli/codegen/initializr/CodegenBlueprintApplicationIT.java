package io.github.bsayli.codegen.initializr;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Tag("integration")
@DisplayName("Integration Test: Spring Context Bootstrapping")
class CodegenBlueprintApplicationIT {

  @Test
  @DisplayName("Spring context should load successfully")
  void contextLoads() {
    // If context fails to load, the test will fail automatically.
  }
}
