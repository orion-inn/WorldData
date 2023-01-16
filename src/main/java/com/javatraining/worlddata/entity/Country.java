package com.javatraining.worlddata.entity;

import java.io.Serializable;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Country implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String name;

    private Long area;
    private Long population;

    @OneToOne
    private City capital;

    @ManyToOne
    private Continent continent;

    @OneToMany(mappedBy = "country")
    private Set<City> cities;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Country country)) return false;
        return id.equals(country.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
