package ${projectPackageName}.adapter.sample.greeting.in.rest;

import ${projectPackageName}.application.sample.greeting.usecase.GetGreetingQuery;
import ${projectPackageName}.application.sample.greeting.usecase.GetGreetingResult;
import ${projectPackageName}.application.sample.greeting.usecase.GetGreetingUseCase;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Inbound REST adapter for Greeting sample.
 * Responsibilities:
 *  - translate HTTP requests into queries
 *  - invoke the application layer (use cases)
 *  - translate results into HTTP responses
 */
@RestController
@RequestMapping(
        path = "/api/v1/sample/greetings",
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class GreetingController {

    private final GetGreetingUseCase getGreetingUseCase;
    private final GreetingResponseMapper responseMapper;

    public GreetingController(
            GetGreetingUseCase getGreetingUseCase,
            GreetingResponseMapper responseMapper) {
        this.getGreetingUseCase = getGreetingUseCase;
        this.responseMapper = responseMapper;
    }

    /**
     * GET /api/v1/sample/greetings/default
     *
     * Returns a default greeting from the domain.
     */
    @GetMapping("/default")
    public ResponseEntity<GreetingResponse> getDefaultGreeting() {
        GetGreetingResult result = getGreetingUseCase.getDefault();
        return ResponseEntity.ok(responseMapper.from(result));
    }

    /**
     * GET /api/v1/sample/greetings
     * Example: /api/v1/sample/greetings?name=John
     *
     * Creates a greeting personalized with a given name.
     */
    @GetMapping
    public ResponseEntity<GreetingResponse> getPersonalGreeting(
            @RequestParam(name = "name", required = false) String name) {
        GetGreetingResult result =
                getGreetingUseCase.getPersonal(new GetGreetingQuery(name));
        return ResponseEntity.ok(responseMapper.from(result));
    }
}