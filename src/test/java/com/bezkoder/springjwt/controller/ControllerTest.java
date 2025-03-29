package com.bezkoder.springjwt.controller;

import com.bezkoder.springjwt.controllers.TestController;
import com.bezkoder.springjwt.models.Books;
import com.bezkoder.springjwt.service.TestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TestController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TestService testService;

    @Test
    void testGetBookByTitle_Success() throws Exception {
        // Arrange: Prepare mock data
        Books book = new Books(1L, "Mockito Guide", "123456789", 2022, true, new HashSet<>(), new HashSet<>());
        when(testService.getBookByTitle("Mockito Guide")).thenReturn(book);

        // Act & Assert: Perform GET request and verify response
        mockMvc.perform(get("/api/test/Mockito Guide"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Mockito Guide"))
                .andExpect(jsonPath("$.isbn").value("123456789"))
                .andExpect(jsonPath("$.available").value(true));

        // Verify service interaction
        verify(testService, times(1)).getBookByTitle("Mockito Guide");
    }
}
