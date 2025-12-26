package ${projectPackageName}.adapter.sample.in.rest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import ${projectPackageName}.adapter.sample.in.rest.dto.GreetingResponse;
import ${projectPackageName}.adapter.sample.in.rest.mapper.GreetingResponseMapper;
import ${projectPackageName}.application.sample.port.in.GetGreetingPort;
import ${projectPackageName}.application.sample.port.in.model.GetGreetingResult;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GreetingController.class)
@Tag("integration")
class GreetingControllerWebMvcIT {

    @Autowired MockMvc mvc;

    @MockitoBean GetGreetingPort getGreetingPort;
    @MockitoBean GreetingResponseMapper responseMapper;

    @Test
    @DisplayName("GET /api/v1/sample/greetings/default -> 200 + body")
    void default_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        GetGreetingResult result = GetGreetingResult.of(id, "Hello");

        when(getGreetingPort.getDefault()).thenReturn(result);
        when(responseMapper.from(result)).thenReturn(new GreetingResponse(id.toString(), "Hello"));

        mvc.perform(get("/api/v1/sample/greetings/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.text").value("Hello"));
    }

    @Test
    @DisplayName("GET /api/v1/sample/greetings?name=John -> 200 + body")
    void personal_returns200() throws Exception {
        UUID id = UUID.randomUUID();
        GetGreetingResult result = GetGreetingResult.of(id, "Hello, John!");

        when(getGreetingPort.getPersonal(any())).thenReturn(result);
        when(responseMapper.from(result))
                .thenReturn(new GreetingResponse(id.toString(), "Hello, John!"));

        mvc.perform(get("/api/v1/sample/greetings").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.text").value("Hello, John!"));
    }
}