package com.javatraining.worlddata;

import com.javatraining.worlddata.dao.WorldDao;
import com.javatraining.worlddata.dao.JPAWorldDao;
import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.helper.WorldDataDB;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("world-data");
        WorldDao worldDao = JPAWorldDao.getInstance(entityManagerFactory);

        // Add cities
        WorldDataDB.getAllCities().values().forEach(worldDao::addCity);
        System.out.println("Total number of cities is " + worldDao.findAllCities().size());

        // Add countries
        WorldDataDB.getAllCountries().values().forEach(worldDao::addCountry);
        System.out.println("Total number of countries is " + worldDao.findAllCountries().size());

        // Update a city
        City cityToUpdate;
        System.out.println("Before update: " + (cityToUpdate = worldDao.findCityById(12)));
        cityToUpdate.setName("Breda 1");
        worldDao.updateCity(cityToUpdate);
        System.out.println("After update: " + (cityToUpdate = worldDao.findCityById(12)));

        // Update a country
        Country countryToUpdate;
        System.out.println("Before update: " + (countryToUpdate = worldDao.findCountryByCode("MKD")));
        countryToUpdate.setName("North Macedonia");
        worldDao.updateCountry(countryToUpdate);
        System.out.println("After update: " + (countryToUpdate = worldDao.findCountryByCode("MKD")));

        // Remove a city
        City cityToRemove;
        System.out.println("Before: " + (cityToRemove = worldDao.findCityById(1)));
        worldDao.removeCity(cityToRemove);
        System.out.println("After: " + (cityToRemove = worldDao.findCityById(1)));

        // Remove a country
        Country countryToRemove;
        System.out.println("Before: " + (countryToRemove = worldDao.findCountryByCode("YUG")));
        worldDao.removeCountry(countryToRemove);
        System.out.println("After: " + (countryToRemove = worldDao.findCountryByCode("YUG")));

        // Find the most populated cities in the world
        System.out.println("The most populated cities in the world are:");
        worldDao.findAllCities().stream()
                .sorted(Comparator.comparingInt(City::getPopulation).reversed())
                .limit(10)
                .forEach(System.out::println);
    }
}
