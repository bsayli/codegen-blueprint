package ${projectPackageName}.bp.sample.controller;

import ${projectPackageName}.bp.sample.controller.dto.GreetingResponse;
import ${projectPackageName}.bp.sample.controller.mapper.GreetingResponseMapper;
import ${projectPackageName}.bp.sample.service.GreetingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inbound REST controller for Greeting BASIC sample (Layered).
 * Intent:
 * - translate HTTP requests into service calls
 * - map domain result into a transport DTO
 * - keep business rules out of the controller
 */
@RestController
@RequestMapping(path = "/api/v1/sample/greetings", produces = MediaType.APPLICATION_JSON_VALUE)
public class GreetingController {

    private final GreetingService greetingService;
    private final GreetingResponseMapper mapper;

    public GreetingController(GreetingService greetingService, GreetingResponseMapper mapper) {
        this.greetingService = greetingService;
        this.mapper = mapper;
    }

    @GetMapping("/default")
    public ResponseEntity<GreetingResponse> getDefault() {
        return ResponseEntity.ok(mapper.from(greetingService.getDefaultGreeting()));
    }

    @GetMapping
    public ResponseEntity<GreetingResponse> getPersonal(
            @RequestParam(name = "name", required = false) String name) {
        return ResponseEntity.ok(mapper.from(greetingService.getPersonalGreeting(name)));
    }
}