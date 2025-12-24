package com.tenda.desafiotecnico.rest.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import com.tenda.desafiotecnico.rest.dto.CouponResponseDTO;
import com.tenda.desafiotecnico.rest.entity.Coupon;
import com.tenda.desafiotecnico.rest.mapper.CouponMapper;
import com.tenda.desafiotecnico.rest.repository.CouponRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponServiceImpl implements CouponService {

    private final CouponRepository couponRepository;
    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public CouponResponseDTO createCoupon(CouponRequestDTO requestDTO) {
        log.info("Criando novo cupom com código: {}", requestDTO.getCode());
        
        Coupon coupon = couponMapper.toEntity(requestDTO);
        
        // Verifica duplicidade
        if (couponRepository.existsByCodeAndDeletedFalse(coupon.getCode())) {
            log.warn("Tentativa de criar cupom com código duplicado: {}", coupon.getCode());
            throw new IllegalArgumentException("Já existe um cupom ativo com este código.");
        }
        
        Coupon savedCoupon = couponRepository.save(coupon);
        log.info("Cupom criado com ID: {}", savedCoupon.getId());
        
        return couponMapper.toResponseDTO(savedCoupon);
    }

    @Override
    @Transactional
    public void deleteCoupon(Long id) {
        log.info("Solicitando soft delete do cupom ID: {}", id);
        
        Coupon coupon = couponRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> {
                    log.warn("Cupom não encontrado ou já deletado: ID {}", id);
                    return new IllegalArgumentException("Cupom não encontrado ou já deletado.");
                });
        
        coupon.delete();
        couponRepository.save(coupon);
        log.info("Cupom ID {} marcado como deletado", id);
    }

    @Override
    @Transactional(readOnly = true)
    public CouponResponseDTO getCouponById(Long id) {
        Coupon coupon = couponRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("Cupom não encontrado."));
        return couponMapper.toResponseDTO(coupon);
    }
}