package com.javatraining.worlddata;

import com.javatraining.worlddata.dao.WorldDao;
import com.javatraining.worlddata.dao.JPAWorldDao;
import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Comparator;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("world-data");
        WorldDao worldDao = JPAWorldDao.getInstance(entityManagerFactory);

        System.out.println("Total number of countries is " + worldDao.findAllCountries().size());
        System.out.println("Total number of cities is " + worldDao.findAllCities().size());
        System.out.println();


        System.out.println("The most populated countries in the world are:");
        worldDao.findAllCountries().stream()
                        .sorted(Comparator.comparingInt(Country::getPopulation).reversed())
                        .limit(10)
                        .forEach(System.out::println);
        System.out.println();

        System.out.println("The most populated cities in the world are:");
        worldDao.findAllCities().stream()
                .sorted(Comparator.comparingInt(City::getPopulation).reversed())
                .limit(10)
                .forEach(System.out::println);
        System.out.println();
    }
}
