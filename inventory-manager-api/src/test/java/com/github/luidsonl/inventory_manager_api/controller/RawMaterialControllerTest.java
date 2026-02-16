package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialDTO;
import com.github.luidsonl.inventory_manager_api.enums.MeasureUnitsType;
import com.github.luidsonl.inventory_manager_api.exception.ResourceNotFoundException;
import com.github.luidsonl.inventory_manager_api.service.RawMaterialService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RawMaterialController.class)
class RawMaterialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RawMaterialService rawMaterialService;

    @Test
    @DisplayName("Should return list of raw materials with all fields")
    void testFindAll() throws Exception {
        RawMaterialDTO dto = RawMaterialDTO.builder()
                .id(1L)
                .code("RM001")
                .name("Sugar")
                .description("Table sugar")
                .unit(MeasureUnitsType.KILOGRAM)
                .fractionable(true)
                .build();

        when(rawMaterialService.findAll()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/raw-materials"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {"id": 1, "code": "RM001", "name": "Sugar", "unit": "KILOGRAM", "fractionable": true}
                        ]
                        """));

        verify(rawMaterialService).findAll();
    }

    @Test
    @DisplayName("Should return raw material by id")
    void testFindById() throws Exception {
        RawMaterialDTO dto = RawMaterialDTO.builder().id(1L).code("WATER").name("Water").unit(MeasureUnitsType.LITER)
                .build();
        when(rawMaterialService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/raw-materials/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        {"code": "WATER", "name": "Water", "unit": "LITER"}
                        """));

        verify(rawMaterialService).findById(1L);
    }

    @Test
    @DisplayName("Should create raw material and verify payload mapping")
    void testSave() throws Exception {
        RawMaterialDTO savedDTO = RawMaterialDTO.builder().id(5L).code("SALT").name("Salt").build();
        when(rawMaterialService.save(any(RawMaterialDTO.class))).thenReturn(savedDTO);

        mockMvc.perform(post("/api/raw-materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"code\": \"SALT\", \"name\":\"Salt\", \"unit\": \"GRAM\", \"fractionable\": false}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 5, \"code\": \"SALT\", \"name\": \"Salt\"}"));

        ArgumentCaptor<RawMaterialDTO> captor = ArgumentCaptor.forClass(RawMaterialDTO.class);
        verify(rawMaterialService).save(captor.capture());

        assertThat(captor.getValue().getCode()).isEqualTo("SALT");
        assertThat(captor.getValue().getName()).isEqualTo("Salt");
        assertThat(captor.getValue().getUnit()).isEqualTo(MeasureUnitsType.GRAM);
    }

    @Test
    @DisplayName("Should create raw material even if ID is provided in payload (ID should be ignored)")
    void testSaveWithId() throws Exception {
        RawMaterialDTO savedDTO = RawMaterialDTO.builder().id(5L).name("Salt").build();
        when(rawMaterialService.save(any(RawMaterialDTO.class))).thenReturn(savedDTO);

        mockMvc.perform(post("/api/raw-materials")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 0, \"name\":\"Salt\", \"unit\": \"GRAM\", \"fractionable\": false}"))
                .andExpect(status().isCreated())
                .andExpect(content().json("{\"id\": 5, \"name\": \"Salt\"}"));

        verify(rawMaterialService).save(any(RawMaterialDTO.class));
    }

    @Test
    @DisplayName("Should update raw material and verify parameters")
    void testUpdate() throws Exception {
        RawMaterialDTO responseDTO = RawMaterialDTO.builder().id(1L).code("U_SALT").name("Updated Salt").build();
        when(rawMaterialService.update(eq(1L), any(RawMaterialDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/raw-materials/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Salt\", \"fractionable\": true}"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"code\": \"U_SALT\", \"name\": \"Updated Salt\"}"));

        verify(rawMaterialService).update(eq(1L), any(RawMaterialDTO.class));
    }

    @Test
    @DisplayName("Should delete raw material and verify service call")
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/raw-materials/1"))
                .andExpect(status().isNoContent());

        verify(rawMaterialService).deleteById(1L);
    }

    @Test
    @DisplayName("Should return 404 when raw material not found")
    void testFindByIdNotFound() throws Exception {
        when(rawMaterialService.findById(99L))
                .thenThrow(new ResourceNotFoundException("Raw Material not found with id: 99"));

        mockMvc.perform(get("/api/raw-materials/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Raw Material not found with id: 99"));

        verify(rawMaterialService).findById(99L);
    }
}
