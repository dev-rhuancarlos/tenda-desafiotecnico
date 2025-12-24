package com.tenda.desafiotecnico.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tenda.desafiotecnico.rest.entity.Coupon;

@DataJpaTest
@ActiveProfiles("test")
class CouponRepositoryTest {

    @Autowired
    private CouponRepository couponRepository;

    @Test
    void saveCoupon_ShouldPersistData() {
        // Arrange
        Coupon coupon = Coupon.builder()
                .code("ABCDEF")
                .description("Cupom de teste")
                .discountValue(new BigDecimal("5.00"))
                .expirationDate(LocalDate.now().plusDays(30))
                .published(true)
                .deleted(false)
                .build();

        // Act
        Coupon saved = couponRepository.save(coupon);

        // Assert
        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCode()).isEqualTo("ABCDEF");
        assertThat(saved.getDescription()).isEqualTo("Cupom de teste");
    }

    @Test
    void existsByCodeAndDeletedFalse_WhenCodeExists_ShouldReturnTrue() {
        // Arrange
        Coupon coupon = Coupon.builder()
                .code("EXISTS1")
                .description("Test")
                .discountValue(new BigDecimal("1.00"))
                .expirationDate(LocalDate.now().plusDays(1))
                .deleted(false)
                .build();
        couponRepository.save(coupon);

        // Act & Assert
        assertThat(couponRepository.existsByCodeAndDeletedFalse("EXISTS1")).isTrue();
    }

    @Test
    void findByIdAndDeletedFalse_WhenCouponDeleted_ShouldReturnEmpty() {
        // Arrange
        Coupon coupon = Coupon.builder()
                .code("DELETED")
                .description("Deleted coupon")
                .discountValue(new BigDecimal("2.00"))
                .expirationDate(LocalDate.now().plusDays(1))
                .deleted(true)
                .build();
        Coupon saved = couponRepository.save(coupon);

        // Act & Assert
        assertThat(couponRepository.findByIdAndDeletedFalse(saved.getId())).isEmpty();
    }
}