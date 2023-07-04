package com.dmdev.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    private Long modelId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Term term = Term.HOURS;

    @NotNull
    @Column(precision = 10, scale = 1)
    private BigDecimal price;

}