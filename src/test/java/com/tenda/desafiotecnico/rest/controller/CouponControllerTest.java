package com.tenda.desafiotecnico.rest.controller;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import com.tenda.desafiotecnico.rest.dto.CouponResponseDTO;
import com.tenda.desafiotecnico.rest.enums.Status;
import com.tenda.desafiotecnico.rest.service.CouponService;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponService couponService;

    @Test
    void createCoupon_WithValidData_ShouldReturnCreated() throws Exception {
        // Arrange
        CouponRequestDTO request = new CouponRequestDTO();
        request.setCode("PROMO-@!");
        request.setDescription("Cupom de teste");
        request.setDiscountValue(new BigDecimal("10.50"));
        request.setExpirationDate(LocalDate.now().plusDays(30));
        request.setPublished(true);

        CouponResponseDTO response = new CouponResponseDTO();
        response.setId(1L);
        response.setCode("PROMO@");
        response.setDescription("Cupom de teste");
        response.setDiscountValue(new BigDecimal("10.50"));
        response.setExpirationDate(LocalDate.now().plusDays(30));
        response.setPublished(true);
        response.setStatus(Status.DELETED);

        when(couponService.createCoupon(any(CouponRequestDTO.class))).thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/coupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.code").value("PROMO@"))
                .andExpect(jsonPath("$.discountValue").value(10.50));
    }

    @Test
    void deleteCoupon_WithValidId_ShouldReturnNoContent() throws Exception {
        // Arrange
        Long couponId = 1L;
        doNothing().when(couponService).deleteCoupon(couponId);

        // Act & Assert
        mockMvc.perform(delete("/api/coupons/{id}", couponId))
                .andExpect(status().isNoContent());

        verify(couponService, times(1)).deleteCoupon(couponId);
    }

    @Test
    void getCouponById_WithValidId_ShouldReturnOk() throws Exception {
        // Arrange
        Long couponId = 1L;
        CouponResponseDTO response = new CouponResponseDTO();
        response.setId(couponId);
        response.setCode("TEST01");
        response.setDescription("Test Cupon");

        when(couponService.getCouponById(couponId)).thenReturn(response);

        // Act & Assert
        mockMvc.perform(get("/api/coupons/{id}", couponId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(couponId))
                .andExpect(jsonPath("$.code").value("TEST01"));
    }
}