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
    CommandLineRunner initDB(ContinentRepository continentRepository,
                                     CountryRepository countryRepository,
                                     CityRepository cityRepository) {
        return args -> {
            Continent continent1 = new Continent("Africa");
            Continent continent2 = new Continent("Asia");
            Continent continent3 = new Continent("Europe");

            Country country1 = new Country("Nepal");
            Country country2 = new Country("Egypt");
            Country country3 = new Country("Serbia");
            Country country4 = new Country("Russia");
            Country country5 = new Country("Nigeria");

            City city1 = new City("Belgrade");
            City city2 = new City("Nizhny Novgorod");
            City city3 = new City("Kairo");
            City city4 = new City("Novi Sad");
            City city5 = new City("Moscow");
            City city6 = new City("Katmandu");
            City city7 = new City("Lagos");

            country1.setContinent(continent2);
            country2.setContinent(continent1);
            country3.setContinent(continent3);
            country4.setContinent(continent3);
            country5.setContinent(continent1);

            city1.setCountry(country3);
            city2.setCountry(country4);
            city3.setCountry(country2);
            city4.setCountry(country3);
            city5.setCountry(country4);
            city6.setCountry(country1);
            city7.setCountry(country5);

            continentRepository.save(continent1);
            continentRepository.save(continent2);
            continentRepository.save(continent3);

            countryRepository.save(country1);
            countryRepository.save(country2);
            countryRepository.save(country3);
            countryRepository.save(country4);
            countryRepository.save(country5);

            cityRepository.save(city1);
            cityRepository.save(city2);
            cityRepository.save(city3);
            cityRepository.save(city4);
            cityRepository.save(city5);
            cityRepository.save(city6);
            cityRepository.save(city7);
        };
    }
}
