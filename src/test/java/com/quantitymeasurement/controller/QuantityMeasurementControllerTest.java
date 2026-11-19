
package com.quantitymeasurement.controller;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class QuantityMeasurementControllerTest {

    @Autowired
    private MockMvc mockMvc;

       @Test
    void shouldConvertQuantitySuccessfully() throws Exception {
        String request = """
                {
                    "source": {
                        "value": 5,
                        "unit": "FEET"
                    },
                    "targetUnit": "INCHES"
                }
                """;

        mockMvc.perform(post("/api/measurements/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }

    @Test
    void shouldReturnBadRequestForEmptyUnit() throws Exception {
        String request = """
                {
                    "source": {
                        "value": 10,
                        "unit": ""
                    },
                    "targetUnit": "INCHES"
                }
                """;

        mockMvc.perform(post("/api/measurements/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors['source.unit']")
                        .value("Measurement unit cannot be empty"));
    }
}
