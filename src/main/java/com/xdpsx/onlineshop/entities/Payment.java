package com.xdpsx.onlineshop.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.xdpsx.onlineshop.entities.enums.PaymentMethod;
import com.xdpsx.onlineshop.entities.enums.PaymentStatus;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime paymentDate;
}
