package ${projectPackageName}.config.sample;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Autowired
    MockMvc mvc;

    @Test
    @DisplayName("GET /api/v1/sample/greetings/default -> default greeting")
    void defaultEndpoint_returnsDefault() throws Exception {
        mvc.perform(get("/api/v1/sample/greetings/default"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value("Hello from layered sample!"));
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
    @DisplayName("GET /api/v1/sample/greetings (no name) -> fallback default")
    void personalEndpoint_withoutName_fallsBack() throws Exception {
        mvc.perform(get("/api/v1/sample/greetings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.text").value("Hello from layered sample!"));
    }
}