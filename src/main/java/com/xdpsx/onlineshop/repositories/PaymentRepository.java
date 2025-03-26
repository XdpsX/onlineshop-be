package com.xdpsx.onlineshop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.xdpsx.onlineshop.entities.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
