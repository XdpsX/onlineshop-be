package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
