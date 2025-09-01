package io.github.bsayli.codegen.initializr.projectgeneration.naming;

import org.springframework.stereotype.Component;

@Component
public class NameConverter {

  /**
   * Converts a raw project name into Java-friendly PascalCase. Examples: "codegen-demo" ->
   * "CodegenDemo" "my_service.core" -> "MyServiceCore" "123-metric" -> "App123Metric"
   */
  public String toPascalCase(String raw) {
    if (raw == null || raw.isBlank()) return "";
    String[] parts = raw.split("[^A-Za-z0-9]+");
    StringBuilder sb = new StringBuilder();
    for (String p : parts) {
      if (p.isBlank()) continue;
      String lower = p.toLowerCase();
      sb.append(Character.toUpperCase(lower.charAt(0)));
      if (lower.length() > 1) {
        sb.append(lower.substring(1));
      }
    }
    String result = sb.toString();
    if (!result.isEmpty() && Character.isDigit(result.charAt(0))) {
      result = "App" + result;
    }
    return result;
  }
}
