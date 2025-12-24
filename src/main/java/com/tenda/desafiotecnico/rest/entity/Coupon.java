package com.tenda.desafiotecnico.rest.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.tenda.desafiotecnico.rest.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "coupons")
@Getter // Lombok gera getters
@Setter // Lombok gera setters (use com cautela no domínio)
@NoArgsConstructor // Construtor sem argumentos exigido pelo JPA
@AllArgsConstructor // Construtor com todos os argumentos
@Builder
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 6)
    @NotBlank(message = "O código do cupom é obrigatório")
    @Size(min = 6, max = 6, message = "O código deve ter exatamente 6 caracteres")
    private String code;

    @Column(nullable = false)
    @NotBlank(message = "A descrição do cupom é obrigatória")
    @Size(min = 6, max = 255, message = "O descrição deve ter no máximo 255 caracteres")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    @DecimalMin(value = "0.50", message = "O valor de desconto mínimo é 0.50")
    private BigDecimal discountValue;

    @Column(nullable = false)
    @FutureOrPresent(message = "A data de expiração não pode estar no passado")
    private LocalDate expirationDate;

    @Column(nullable = false)
    private boolean published = false; // Pode ser criado como publicado

    @Column(nullable = false)
    private boolean deleted = false; // Flag para soft delete

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private boolean redeemed = false;

    public void formatAndSetCode(String rawCode) {
        if (rawCode == null) {
            this.code = null;
            return;
        }
        String cleanedCode = rawCode.replaceAll("[^a-zA-Z0-9]", "");
        if (cleanedCode.length() > 6) {
            this.code = cleanedCode.substring(0, 6);
        } else if (cleanedCode.length() < 6) {
            // Preenche com 'X' à direita
            this.code = String.format("%-6s", cleanedCode).replace(' ', 'X');
        } else {
            this.code = cleanedCode;
        }
    }

    // **MÉTODO DE NEGÓCIO: Validação na criação**
    public void validateForCreation() {
        if (expirationDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("A data de expiração não pode estar no passado.");
        }
        if (discountValue.compareTo(new BigDecimal("0.50")) < 0) {
            throw new IllegalArgumentException("O valor de desconto mínimo é 0.50.");
        }
    }

    // **MÉTODO DE NEGÓCIO: Soft Delete**
    public void delete() {
        if (this.status.equals(Status.DELETED)) {
            throw new IllegalStateException("Este cupom já foi deletado.");
        }
        this.status = Status.DELETED;
    }
}