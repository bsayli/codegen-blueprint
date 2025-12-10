package ${projectPackageName};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot entrypoint for the generated service.
 */
@SpringBootApplication
public class ${className} {

public static void main(String[] args) {
  SpringApplication.run(${className}.class, args);
}
}