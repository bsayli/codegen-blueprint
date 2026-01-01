package ${projectPackageName}.bp.sample.bootstrap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Tag("integration")
class GreetingSampleHttpIT {

    @Autowired MockMvc mvc;

    @Test
    @DisplayName("GET /api/v1/sample/greetings/default -> default greeting")
    void defaultEndpoint_returnsDefault() throws Exception {
        mvc.perform(get("/api/v1/sample/greetings/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value("Hello from hexagonal sample!"));
    }

    @Test
    @DisplayName("GET /api/v1/sample/greetings?name=John -> personalized greeting")
    void personalEndpoint_returnsPersonalized() throws Exception {
        mvc.perform(get("/api/v1/sample/greetings").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value("Hello, John!"));
    }

    @Test
    @DisplayName("GET /api/v1/sample/greetings (no name) -> falls back to default greeting")
    void personalEndpoint_withoutName_fallsBackToDefault() throws Exception {
        mvc.perform(get("/api/v1/sample/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value("Hello from hexagonal sample!"));
    }
}