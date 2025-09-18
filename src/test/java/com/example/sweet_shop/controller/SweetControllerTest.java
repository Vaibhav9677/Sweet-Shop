package com.example.sweetshop.controller;

import com.example.sweetshop.model.Sweet;
import com.example.sweetshop.service.SweetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = SweetController.class,
           excludeAutoConfiguration = { SecurityAutoConfiguration.class })
public class SweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SweetService sweetService;

    @Test
    public void testGetAllSweets() throws Exception {
        when(sweetService.getAllSweets()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/sweets"))
               .andExpect(status().isOk())
               .andExpect(content().json("[]"));
    }
}
