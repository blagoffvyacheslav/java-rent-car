package com.dmdev.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "model")
@EqualsAndHashCode(exclude = "model")
@Builder
public class CarRate implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Term term = Term.HOURS;

    @NotNull
    @Column(precision = 10, scale = 1)
    private BigDecimal price;

}