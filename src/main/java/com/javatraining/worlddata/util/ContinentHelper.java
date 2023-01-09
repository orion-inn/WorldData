package com.javatraining.worlddata.util;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Continent;
import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.repository.CityRepository;
import com.javatraining.worlddata.repository.ContinentRepository;
import com.javatraining.worlddata.repository.CountryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContinentHelper {

    @Bean
    CommandLineRunner initContinents(ContinentRepository continentRepository,
                                     CountryRepository countryRepository,
                                     CityRepository cityRepository) {
        return args -> {
            /*
            continentRepository.save(new Continent("Africa"));
            continentRepository.save(new Continent("Asia"));
            continentRepository.save(new Continent("Europe"));

            countryRepository.save(new Country("Nepal"));
            countryRepository.save(new Country("Egypt"));
            countryRepository.save(new Country("Serbia"));

            cityRepository.save(new City("Belgrade"));
            cityRepository.save(new City("Nizhny Novgorod"));
            cityRepository.save(new City("Kairo"));
            */
        };
    }
}
