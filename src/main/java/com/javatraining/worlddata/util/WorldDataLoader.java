package com.javatraining.worlddata.util;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Continent;
import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.service.CityService;
import com.javatraining.worlddata.service.ContinentService;
import com.javatraining.worlddata.service.CountryService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WorldDataLoader {

    @Bean
    RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

    @Bean
    public CommandLineRunner run(RestTemplate restTemplate,
                                 ContinentService continentService,
                                 CountryService countryService,
                                 CityService cityService) {
        return args -> {
            final String url = "https://restcountries.com/v3.1/all?fields=name,area,population,capital,continents";

            ResponseEntity<ResponseCountry[]> response = restTemplate.getForEntity(url, ResponseCountry[].class);

            if (response.getStatusCode().isError() || !response.hasBody()) {
                throw new RuntimeException("Problem with restcounries API");
            }

            for (ResponseCountry responseCountry : response.getBody()) {
                Country country = new Country(responseCountry.getName().getCommon());
                country.setArea(responseCountry.getArea());
                country.setPopulation(responseCountry.getPopulation());

                if (responseCountry.getContinents().length > 0) {
                    Continent continent = new Continent(responseCountry.getContinents()[0]);
                    try {
                        continent = continentService.create(continent);
                    } catch (ResourceAlreadyExistsException ignored) {
                        continent = continentService.getByName(continent.getName());
                    }
                    country.setContinent(continent);
                }

                if (responseCountry.getCapital().length > 0) {
                    City city = new City(responseCountry.getCapital()[0]);
                    try {
                        city = cityService.create(city);
                    } catch (ResourceAlreadyExistsException ignored) {
                        city = new City(city.getName() + " (" + country.getName() + ")");
                        city = cityService.create(city);
                    }
                    country.setCapital(city);
                    country = countryService.create(country);
                    city.setCountry(country);
                    cityService.update(city.getId(), city);
                }
            }
        };
    }
}
