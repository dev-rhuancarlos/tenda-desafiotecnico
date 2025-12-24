package com.tenda.desafiotecnico.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import com.tenda.desafiotecnico.rest.dto.CouponResponseDTO;
import com.tenda.desafiotecnico.rest.service.CouponService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/coupons")
@RequiredArgsConstructor
@Tag(name = "Cupons", description = "API para gerenciamento de cupons de desconto")
public class CouponController {

    private final CouponService couponService;

    @PostMapping
    @Operation(summary = "Cria um novo cupom", description = "Cadastra um cupom com todas as validações de negócio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso",
                     content = @Content(schema = @Schema(implementation = CouponResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou regras violadas"),
        @ApiResponse(responseCode = "409", description = "Código do cupom já existe")
    })
    public ResponseEntity<CouponResponseDTO> createCoupon(
            @Valid @RequestBody CouponRequestDTO couponRequestDTO) {
        CouponResponseDTO createdCoupon = couponService.createCoupon(couponRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Exclui um cupom", description = "Realiza soft delete (exclusão lógica) do cupom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cupom excluído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
        @ApiResponse(responseCode = "400", description = "Cupom já está excluído")
    })
    public ResponseEntity<Void> deleteCoupon(
            @Parameter(description = "ID do cupom a ser excluído", required = true, example = "1")
            @PathVariable Long id) {
        couponService.deleteCoupon(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um cupom por ID", description = "Retorna os detalhes de um cupom específico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupom encontrado"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    public ResponseEntity<CouponResponseDTO> getCouponById(
            @Parameter(description = "ID do cupom", required = true, example = "1")
            @PathVariable Long id) {
        CouponResponseDTO coupon = couponService.getCouponById(id);
        return ResponseEntity.ok(coupon);
    }

}