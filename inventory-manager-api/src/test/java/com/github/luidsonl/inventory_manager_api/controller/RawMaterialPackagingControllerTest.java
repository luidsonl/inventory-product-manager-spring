package com.github.luidsonl.inventory_manager_api.controller;

import com.github.luidsonl.inventory_manager_api.dto.RawMaterialPackagingDTO;
import com.github.luidsonl.inventory_manager_api.service.RawMaterialPackagingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RawMaterialPackagingController.class)
class RawMaterialPackagingControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private RawMaterialPackagingService packagingService;

        @Test
        @DisplayName("Should return list of packaging items")
        void testFindAll() throws Exception {
                RawMaterialPackagingDTO dto = RawMaterialPackagingDTO.builder()
                                .id(1L)
                                .name("Large Box")
                                .quantityInside(new BigDecimal("10.0"))
                                .currentStock(5)
                                .build();
                when(packagingService.findAll()).thenReturn(Collections.singletonList(dto));

                mockMvc.perform(get("/api/packaging"))
                                .andExpect(status().isOk())
                                .andExpect(content().json("""
                                                [
                                                    {"name": "Large Box", "quantityInside": 10.0}
                                                ]
                                                """));

                verify(packagingService).findAll();
        }

        @Test
        @DisplayName("Should create packaging and verify association mapping")
        void testSave() throws Exception {
                RawMaterialPackagingDTO savedDTO = RawMaterialPackagingDTO.builder().id(10L).name("Bag 1kg")
                                .rawMaterialId(2L)
                                .build();
                when(packagingService.save(any(RawMaterialPackagingDTO.class))).thenReturn(savedDTO);

                mockMvc.perform(post("/api/packaging")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"Bag 1kg\", \"rawMaterialId\": 2, \"quantityInside\": 1.0}"))
                                .andExpect(status().isCreated())
                                .andExpect(content().json("{\"id\": 10, \"rawMaterialId\": 2}"));

                ArgumentCaptor<RawMaterialPackagingDTO> captor = ArgumentCaptor.forClass(RawMaterialPackagingDTO.class);
                verify(packagingService).save(captor.capture());

                assertThat(captor.getValue().getRawMaterialId()).isEqualTo(2L);
                assertThat(captor.getValue().getQuantityInside()).isEqualByComparingTo("1.0");
        }

        @Test
        @DisplayName("Should find packaging by raw material id")
        void testFindByRawMaterialId() throws Exception {
                RawMaterialPackagingDTO dto = RawMaterialPackagingDTO.builder().id(1L).rawMaterialId(5L).build();
                when(packagingService.findByRawMaterialId(5L)).thenReturn(Collections.singletonList(dto));

                mockMvc.perform(get("/api/packaging/raw-material/5"))
                                .andExpect(status().isOk())
                                .andExpect(content().json("[{\"rawMaterialId\": 5}]"));

                verify(packagingService).findByRawMaterialId(5L);
        }

        @Test
        @DisplayName("Should delete packaging and verify service call")
        void testDelete() throws Exception {
                mockMvc.perform(delete("/api/packaging/1"))
                                .andExpect(status().isNoContent());

                verify(packagingService).deleteById(1L);
        }
}
