package com.javatraining.worlddata.entity;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor @ToString
public class City implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @NonNull
    @Column(nullable = false, unique = true)
    private String name;

    private Long area;
    private Long population;

    @JsonIgnore
    @ManyToOne
    private Country country;

    public boolean isCapital() {
        if (this.getCountry() == null) {
            return false;
        }

        City capital = this.getCountry().getCapital();
        return capital != null && capital.equals(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof City city)) return false;
        return id.equals(city.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
