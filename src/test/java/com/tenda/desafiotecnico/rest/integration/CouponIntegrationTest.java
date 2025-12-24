package com.tenda.desafiotecnico.rest.integration;

import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class CouponIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndDeleteCoupon_IntegrationTest() {
        CouponRequestDTO request = new CouponRequestDTO();
        request.setCode("INT-TET");
        request.setDescription("Cupom de integração");
        request.setDiscountValue(new BigDecimal("7.50"));
        request.setExpirationDate(LocalDate.now().plusDays(90));
        request.setPublished(false);

        ResponseEntity<String> createResponse = restTemplate.postForEntity(
                "/api/coupons", request, String.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        String responseBody = createResponse.getBody();
        assertThat(responseBody).contains("INTTET");

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/coupons/{id}",
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class,
                1L);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void createCoupon_WithPastDate_ShouldReturnBadRequest() {
        CouponRequestDTO request = new CouponRequestDTO();
        request.setCode("PAS123");
        request.setDescription("Cupom com data passada");
        request.setDiscountValue(new BigDecimal("5.00"));
        request.setExpirationDate(LocalDate.now().minusDays(1)); // Data no passado
        request.setPublished(true);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/coupons", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void createCoupon_WithInvalidDiscount_ShouldReturnBadRequest() {
        CouponRequestDTO request = new CouponRequestDTO();
        request.setCode("SMAL12");
        request.setDescription("Cupom com desconto pequeno");
        request.setDiscountValue(new BigDecimal("0.10")); // Abaixo do mínimo
        request.setExpirationDate(LocalDate.now().plusDays(30));
        request.setPublished(true);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "/api/coupons", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}