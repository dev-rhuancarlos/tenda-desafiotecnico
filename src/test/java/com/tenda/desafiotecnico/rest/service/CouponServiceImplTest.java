package com.tenda.desafiotecnico.rest.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import com.tenda.desafiotecnico.rest.dto.CouponResponseDTO;
import com.tenda.desafiotecnico.rest.entity.Coupon;
import com.tenda.desafiotecnico.rest.enums.Status;
import com.tenda.desafiotecnico.rest.mapper.CouponMapper;
import com.tenda.desafiotecnico.rest.repository.CouponRepository;

@ExtendWith(MockitoExtension.class)
class CouponServiceImplTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper couponMapper;

    @InjectMocks
    private CouponServiceImpl couponService;

    private CouponRequestDTO validRequest;
    private Coupon validCoupon;
    private CouponResponseDTO validResponse;

    @BeforeEach
    void setUp() {
        validRequest = new CouponRequestDTO();
        validRequest.setCode("ABC-123");
        validRequest.setDescription("Test Description");
        validRequest.setDiscountValue(new BigDecimal("15.75"));
        validRequest.setExpirationDate(LocalDate.now().plusDays(60));
        validRequest.setPublished(true);

        validCoupon = Coupon.builder()
                .id(1L)
                .code("ABC123")
                .description("Test Description")
                .discountValue(new BigDecimal("15.75"))
                .expirationDate(LocalDate.now().plusDays(60))
                .published(true)
                .deleted(false)
                .status(Status.ACTIVE)
                .build();

        validResponse = new CouponResponseDTO();
        validResponse.setId(1L);
        validResponse.setCode("ABC123");
        validResponse.setDescription("Test Description");
        validResponse.setDiscountValue(new BigDecimal("15.75"));
        validResponse.setExpirationDate(LocalDate.now().plusDays(60));
        validResponse.setPublished(true);
        validResponse.setStatus(Status.ACTIVE);
        validResponse.setRedeemed(false);
    }

    @Test
    void createCoupon_WithValidData_ShouldReturnCouponResponse() {
        // Arrange
        when(couponMapper.toEntity(validRequest)).thenReturn(validCoupon);
        when(couponRepository.existsByCodeAndDeletedFalse("ABC123")).thenReturn(false);
        when(couponRepository.save(validCoupon)).thenReturn(validCoupon);
        when(couponMapper.toResponseDTO(validCoupon)).thenReturn(validResponse);

        // Act
        CouponResponseDTO result = couponService.createCoupon(validRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ABC123", result.getCode());
        verify(couponRepository).save(validCoupon);
    }

    @Test
    void createCoupon_WithDuplicateCode_ShouldThrowException() {
        // Arrange
        when(couponMapper.toEntity(validRequest)).thenReturn(validCoupon);
        when(couponRepository.existsByCodeAndDeletedFalse("ABC123")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> couponService.createCoupon(validRequest)
        );
        
        assertEquals("Já existe um cupom ativo com este código.", exception.getMessage());
        verify(couponRepository, never()).save(any());
    }

    @Test
    void deleteCoupon_WithExistingCoupon_ShouldPerformSoftDelete() {
        // Arrange
        Long couponId = 1L;
        when(couponRepository.findByIdAndDeletedFalse(couponId))
                .thenReturn(Optional.of(validCoupon));
        when(couponRepository.save(validCoupon)).thenReturn(validCoupon);

        // Act
        couponService.deleteCoupon(couponId);

        // Assert
        assertEquals(Status.DELETED,validCoupon.getStatus());
        verify(couponRepository).save(validCoupon);
    }

    @Test
    void deleteCoupon_WithAlreadyDeletedCoupon_ShouldThrowException() {
        // Arrange
        Long couponId = 1L;
        when(couponRepository.findByIdAndDeletedFalse(couponId))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> couponService.deleteCoupon(couponId)
        );
        
        assertEquals("Cupom não encontrado ou já deletado.", exception.getMessage());
        verify(couponRepository, never()).save(any());
    }

    @Test
    void getCouponById_WithExistingCoupon_ShouldReturnCoupon() {
        // Arrange
        Long couponId = 1L;
        when(couponRepository.findByIdAndDeletedFalse(couponId))
                .thenReturn(Optional.of(validCoupon));
        when(couponMapper.toResponseDTO(validCoupon)).thenReturn(validResponse);

        // Act
        CouponResponseDTO result = couponService.getCouponById(couponId);

        // Assert
        assertNotNull(result);
        assertEquals(couponId, result.getId());
    }

    @Test
    void getCouponById_WithNonExistingCoupon_ShouldThrowException() {
        // Arrange
        Long couponId = 999L;
        when(couponRepository.findByIdAndDeletedFalse(couponId))
                .thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> couponService.getCouponById(couponId)
        );
        
        assertEquals("Cupom não encontrado.", exception.getMessage());
    }
}