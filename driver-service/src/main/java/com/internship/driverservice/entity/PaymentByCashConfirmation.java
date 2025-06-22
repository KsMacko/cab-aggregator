package com.internship.driverservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cash_confirmation")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentByCashConfirmation {
    @Id
    private Long id;
    private BigDecimal amount;
    private Long passengerId;
    @OneToOne(fetch = FetchType.EAGER)
    @MapsId
    @JoinColumn(name = "id")
    private Notification notification;
}
