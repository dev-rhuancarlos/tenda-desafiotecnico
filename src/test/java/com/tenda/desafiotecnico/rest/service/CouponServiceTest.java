package com.tenda.desafiotecnico.rest.service;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import com.tenda.desafiotecnico.rest.entity.Coupon;
import com.tenda.desafiotecnico.rest.mapper.CouponMapper;
import com.tenda.desafiotecnico.rest.repository.CouponRepository;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private CouponMapper couponMapper;

    @InjectMocks
    private CouponService couponService;

    // @Test
    // void createCoupon_WithValidData_ShouldSucceed() {
    //     CouponRequestDTO requestDTO = new CouponRequestDTO();
    //     requestDTO.setCode("PROMO-@!");
    //     requestDTO.setDescription("Teste");
    //     requestDTO.setDiscountValue(new BigDecimal("5.00"));
    //     requestDTO.setExpirationDate(LocalDate.now().plusDays(10));

    //     Coupon mockCoupon = new Coupon();
    //     mockCoupon.setId(1L);
    //     mockCoupon.setCode("PROMO@");

    //     when(couponMapper.toEntity(requestDTO)).thenReturn(mockCoupon);
    //     when(couponRepository.existsByCodeAndDeletedFalse("PROMO@")).thenReturn(false);
    //     when(couponRepository.save(mockCoupon)).thenReturn(mockCoupon);

    //     Coupon result = couponService.createCoupon(requestDTO);

    //     assertNotNull(result);
    //     assertEquals(1L, result.getId());
    //     assertEquals("PROMO@", result.getCode());
    //     verify(couponRepository).save(mockCoupon);
    // }

    @Test
    void createCoupon_WithInvalidData_ShouldFail() {
        CouponRequestDTO requestDTO = new CouponRequestDTO();
        requestDTO.setCode("PROMO-@!");
        requestDTO.setDescription("Teste");
        requestDTO.setDiscountValue(new BigDecimal("5.00"));
        requestDTO.setExpirationDate(LocalDate.now().minusDays(10));

        Coupon mockCoupon = new Coupon();
        mockCoupon.setId(1L);
        mockCoupon.setCode("PROMO@");

        when(couponMapper.toEntity(requestDTO)).thenReturn(mockCoupon);
        verify(couponRepository).save(mockCoupon);
    }

    @Test
    void deleteCoupon_WhenCouponExists_ShouldPerformSoftDelete() {
        Long couponId = 1L;
        Coupon mockCoupon = new Coupon();
        mockCoupon.setId(couponId);
        mockCoupon.setDeleted(false);

        when(couponRepository.findByIdAndDeletedFalse(couponId)).thenReturn(java.util.Optional.of(mockCoupon));
        when(couponRepository.save(mockCoupon)).thenReturn(mockCoupon);

        couponService.deleteCoupon(couponId);

        assertTrue(mockCoupon.isDeleted());
        verify(couponRepository).save(mockCoupon);
    }

}