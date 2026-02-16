package com.github.luidsonl.inventory_manager_api;

import com.github.luidsonl.inventory_manager_api.controller.ProductController;
import com.github.luidsonl.inventory_manager_api.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ErrorHandlingTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    @DisplayName("Should return 404 Not Found for non-existent endpoint")
    void shouldReturn404ForNonExistentEndpoint() throws Exception {
        mockMvc.perform(get("/api/non-existent"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Resource not found: api/non-existent"));
    }

    @Test
    @DisplayName("Should return 405 Method Not Allowed for unsupported method")
    void shouldReturn405ForUnsupportedMethod() throws Exception {
        mockMvc.perform(post("/api/products/1"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(405))
                .andExpect(jsonPath("$.error").value("Method Not Allowed"));
    }
}
