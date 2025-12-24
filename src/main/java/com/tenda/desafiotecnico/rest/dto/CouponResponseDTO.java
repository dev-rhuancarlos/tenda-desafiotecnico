package com.tenda.desafiotecnico.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tenda.desafiotecnico.rest.enums.Status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CouponResponseDTO {
    private Long id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate expirationDate;
    
    private boolean published;
    private Status status;
    private boolean redeemed;
    
}