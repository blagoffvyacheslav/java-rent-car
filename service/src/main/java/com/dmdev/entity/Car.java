package com.dmdev.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"model", "orders"})
@EqualsAndHashCode(of = "serialNumber")
@Builder
public class Car implements BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private Model model;

    @NotNull
    @Column(nullable = false, unique = true)
    private String serialNumber;

    @NotNull
    private Boolean isNew = Boolean.FALSE;

    private String image;

    @Builder.Default
    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL)
    private List<Order> orders = new ArrayList<>();

    public void setModel(Model model) {
        this.model = model;
        this.model.getCars().add(this);
    }

}