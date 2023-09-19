package com.dmdev.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "order")
@EqualsAndHashCode(exclude = "order", callSuper = false)
@Builder
public class Damage extends AuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    private String description;

    @NotNull
    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    public void setOrder(Order order) {
        this.order = order;
        this.order.getDamages().add(this);
    }
}