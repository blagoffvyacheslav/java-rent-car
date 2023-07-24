package com.dmdev.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "driverLicenses"})
@EqualsAndHashCode(exclude = {"user", "driverLicenses"})
@Builder
public class UserDetails implements BaseEntity<Long>  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String lastname;

    @NotNull
    @Column(nullable = false)
    private String address;

    @NotNull
    @Column(nullable = false)
    private String passportNumber;

    @NotNull
    @Column(nullable = false)
    private String phone;

    @NotNull
    @Column(nullable = false)
    private LocalDate birthday;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDate registrationDate = LocalDate.now();

    @Builder.Default
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DriverLicense> driverLicenses = new HashSet<>();

    public void setDriverLicense(DriverLicense driverLicense) {
        driverLicenses.add(driverLicense);
        driverLicense.setUserDetails(this);
    }

    public void setUser(User user) {
        user.setUserDetails(this);
        this.user = user;
        this.id = user.getId();
    }

}