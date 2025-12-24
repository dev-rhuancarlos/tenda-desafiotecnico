package com.tenda.desafiotecnico.rest.service;

import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import com.tenda.desafiotecnico.rest.dto.CouponResponseDTO;

public interface CouponService {
    CouponResponseDTO createCoupon(CouponRequestDTO requestDTO);
    void deleteCoupon(Long id);
    CouponResponseDTO getCouponById(Long id);
}