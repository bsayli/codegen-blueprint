package ${projectPackageName}.bp.sample.config;

import ${projectPackageName}.bp.sample.controller.mapper.GreetingResponseMapper;
import ${projectPackageName}.bp.sample.domain.service.GreetingDomainService;
import ${projectPackageName}.bp.sample.repository.GreetingAuditRepository;
import ${projectPackageName}.bp.sample.repository.LoggingGreetingAuditRepository;
import ${projectPackageName}.bp.sample.service.GreetingService;
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