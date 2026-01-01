package ${projectPackageName}.bp.sample.bootstrap;

import ${projectPackageName}.bp.sample.adapter.in.rest.mapper.GreetingResponseMapper;
import ${projectPackageName}.bp.sample.adapter.out.logging.LoggingGreetingAuditAdapter;
import ${projectPackageName}.bp.sample.application.port.in.GetGreetingPort;
import ${projectPackageName}.bp.sample.application.usecase.GetGreetingHandler;
import ${projectPackageName}.bp.sample.domain.port.out.GreetingAuditPort;
import ${projectPackageName}.bp.sample.domain.service.GreetingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bootstrap wiring for the Greeting BASIC sample.
 * Principles:
 * - Beans are exposed by their *port* types (interfaces)
 * - Implementation classes stay framework-agnostic
 * - Bean names reflect concrete roles
 */
@Configuration
public class GreetingSampleConfig {

    @Bean
    GreetingService greetingService() {
        return new GreetingService();
    }

    /**
     * Outbound audit port wiring.
     * Return type = port interface (Hexagonal rule)
     * Bean name = concrete role (LoggingGreetingAuditAdapter)
     */
    @Bean
    GreetingAuditPort loggingGreetingAuditAdapter() {
        return new LoggingGreetingAuditAdapter();
    }

    /**
     * Use case wiring.
     * Return type = use case interface
     * Bean name = concrete handler implementation
     */
    @Bean
    GetGreetingPort getGreetingHandler(
            GreetingService greetingService,
            GreetingAuditPort loggingGreetingAuditAdapter) {
        return new GetGreetingHandler(greetingService, loggingGreetingAuditAdapter);
    }

    /**
     * REST adapter helper (mapper), kept free of Spring annotations.
     */
    @Bean
    GreetingResponseMapper greetingResponseMapper() {
        return new GreetingResponseMapper();
    }
}