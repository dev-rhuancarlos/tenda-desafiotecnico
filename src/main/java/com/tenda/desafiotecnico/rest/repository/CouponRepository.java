package com.tenda.desafiotecnico.rest.repository;
import com.tenda.desafiotecnico.rest.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {
    Optional<Coupon> findByIdAndDeletedFalse(Long id);

    boolean existsByCodeAndDeletedFalse(String code);
}
