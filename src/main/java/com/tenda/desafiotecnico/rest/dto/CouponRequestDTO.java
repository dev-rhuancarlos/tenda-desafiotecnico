package com.tenda.desafiotecnico.rest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CouponRequestDTO {
    @NotBlank(message = "Código é obrigatório")
    private String code;

    @NotBlank(message = "Descrição é obrigatória")
    private String description;

    @NotNull(message = "Valor de desconto é obrigatório")
    @DecimalMin(value = "0.50", message = "Valor mínimo é 0.50")
    private BigDecimal discountValue;

    @NotNull(message = "Data de expiração é obrigatória")
    @FutureOrPresent(message = "Data não pode ser no passado")
    private LocalDate expirationDate;

    private boolean published = false;
}