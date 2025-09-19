package com.example.sweetshop.controller;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.service.SweetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = SweetController.class,
           excludeAutoConfiguration = { SecurityAutoConfiguration.class })
public class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllSweets() throws Exception {
        Mockito.when(sweetService.getAllSweets()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/sweets"))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }

    @Test
    public void testAddSweet() throws Exception {
        Sweet sweet = new Sweet("1", "Ladoo", "Indian", 50.0, 100); // double
        Sweet savedSweet = new Sweet("1", "Ladoo", "Indian", 50, 100);

        Mockito.when(sweetService.addSweet(any(Sweet.class))).thenReturn(savedSweet);

        mockMvc.perform(post("/api/sweets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sweet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Ladoo"));
    }

@Test
        public void testSearchSweetsByName() throws Exception {
        Sweet sweet = new Sweet("1", "Kaju Katli", "Indian", 120.0, 50);
        List<Sweet> list = Arrays.asList(sweet);

    // Mock service response for name search
        Mockito.when(sweetService.searchByName("Kaju")).thenReturn(list);

        mockMvc.perform(get("/api/sweets/search")
                    .param("name", "Kaju")
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].name").value("Kaju Katli"))
            .andExpect(jsonPath("$[0].category").value("Indian"))
            .andExpect(jsonPath("$[0].price").value(120.0))
            .andExpect(jsonPath("$[0].quantity").value(50));
        }

}
