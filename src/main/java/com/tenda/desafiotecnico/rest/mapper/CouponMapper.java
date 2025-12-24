package com.tenda.desafiotecnico.rest.mapper;

import org.springframework.stereotype.Component;

import com.tenda.desafiotecnico.rest.dto.CouponRequestDTO;
import com.tenda.desafiotecnico.rest.dto.CouponResponseDTO;
import com.tenda.desafiotecnico.rest.entity.Coupon;
import com.tenda.desafiotecnico.rest.enums.Status;

@Component
public class CouponMapper {

    public Coupon toEntity(CouponRequestDTO dto) {
        Coupon coupon = new Coupon();
        coupon.formatAndSetCode(dto.getCode());
        coupon.setDescription(dto.getDescription());
        coupon.setDiscountValue(dto.getDiscountValue());
        coupon.setExpirationDate(dto.getExpirationDate());
        coupon.setPublished(dto.isPublished());
        coupon.setStatus(Status.ACTIVE);
        coupon.validateForCreation();
        return coupon;
    }

    public CouponResponseDTO toResponseDTO(Coupon coupon) {
        CouponResponseDTO dto = new CouponResponseDTO();
        dto.setId(coupon.getId());
        dto.setCode(coupon.getCode());
        dto.setDescription(coupon.getDescription());
        dto.setDiscountValue(coupon.getDiscountValue());
        dto.setExpirationDate(coupon.getExpirationDate());
        dto.setPublished(coupon.isPublished());
        dto.setStatus(coupon.getStatus());
        dto.setRedeemed(coupon.isRedeemed());
        return dto;
    }
}