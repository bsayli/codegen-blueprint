package ${projectPackageName}.config.sample;

import ${projectPackageName}.controller.sample.mapper.GreetingResponseMapper;
import ${projectPackageName}.domain.sample.service.GreetingDomainService;
import ${projectPackageName}.repository.sample.GreetingAuditRepository;
import ${projectPackageName}.repository.sample.LoggingGreetingAuditRepository;
import ${projectPackageName}.service.sample.GreetingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring wiring for the Greeting BASIC sample (Layered).
 * Intent:
 * - keep sample components free of Spring annotations
 * - wire them here explicitly
 * - keep responsibilities clear: domain service, use-case service, audit implementation, mapper
 */
@Configuration
public class GreetingSampleConfig {

    @Bean
    GreetingDomainService greetingDomainService() {
        return new GreetingDomainService();
    }

    @Bean
    GreetingAuditRepository greetingAuditRepository() {
        return new LoggingGreetingAuditRepository();
    }

    @Bean
    GreetingService greetingService(GreetingDomainService greetingDomainService, GreetingAuditRepository greetingAuditRepository) {
        return new GreetingService(greetingDomainService, greetingAuditRepository);
    }

    @Bean
    GreetingResponseMapper greetingResponseMapper() {
        return new GreetingResponseMapper();
    }
}