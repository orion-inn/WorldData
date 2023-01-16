package com.javatraining.worlddata.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter @Setter
public class ResponseCountry {

    private ResponseName name;
    private Long area;
    private Long population;
    private String[] capital;
    private String[] continents;

}
