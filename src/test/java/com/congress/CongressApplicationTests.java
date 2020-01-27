package com.congress;

import com.congress.entity.Congress;
import com.congress.repository.CongressRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@JsonTest
@AutoConfigureMockMvc
public class CongressApplicationTests {
    @Autowired
    CongressRepository congressRepository;
    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;

    @Test
    void contextLoads() {
    }

    void congressCreation() throws Exception {
        Congress congress = new Congress();
        congress.setName("Yolo");
        congressRepository.save(congress);
        this.mockMvc.perform(get("/api/congress/1")).andExpect(content().json(objectMapper.writeValueAsString(congress)));
    }
}
