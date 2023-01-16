package com.javatraining.worlddata.entity;

import java.io.Serializable;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class Continent implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String name;

    private Long area;
    private Long population;

    @JsonIgnore
    @OneToMany(mappedBy = "continent")
    private Set<Country> countries;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Continent continent)) return false;
        return id.equals(continent.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
