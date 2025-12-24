package com.tenda.desafiotecnico.rest.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.tenda.desafiotecnico.rest.entity.Coupon;
import com.tenda.desafiotecnico.rest.enums.Status;

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
                .status(Status.ACTIVE)
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
                .code("EXISS1")
                .description("Testestes")
                .discountValue(new BigDecimal("1.00"))
                .expirationDate(LocalDate.now().plusDays(1))
                .deleted(false)
                .status(Status.ACTIVE)
                .build();
        couponRepository.save(coupon);

        // Act & Assert
        assertThat(couponRepository.existsByCodeAndDeletedFalse("EXISS1")).isTrue();
    }

    @Test
    void findByIdAndDeletedFalse_WhenCouponDeleted_ShouldReturnEmpty() {
        // Arrange
        Coupon coupon = Coupon.builder()
                .code("DELTED")
                .description("Deleted coupon")
                .discountValue(new BigDecimal("2.00"))
                .expirationDate(LocalDate.now().plusDays(1))
                .deleted(true)
                .status(Status.ACTIVE)
                .build();
        Coupon saved = couponRepository.save(coupon);

        // Act & Assert
        assertThat(couponRepository.findByIdAndDeletedFalse(saved.getId())).isEmpty();
    }
}