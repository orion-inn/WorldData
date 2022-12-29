package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public class JPAWorldDao implements WorldDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void addCity(City city) {
        entityManager.persist(city);
    }

    @Override
    public void removeCity(City city) {
        entityManager.remove(city);
    }

    @Override
    public void updateCity(City city) {
        entityManager.merge(city);
    }

    @Override
    public City findCityById(int id) {
        return entityManager.find(City.class, id);
    }

    @Override
    public List<City> findCitiesByPartialName(String name) {
        String query = "SELECT city FROM City city WHERE city.name LIKE '%" + name + "%'";
        return entityManager.createQuery(query, City.class).getResultList();
    }

    @Override
    public List<City> findCitiesWithIds(List<Integer> ids) {
        return ids.stream()
                .map(this::findCityById)
                .toList();
    }

    @Override
    public List<City> findAllCities() {
        return entityManager.createQuery("SELECT city FROM City city", City.class).getResultList();
    }

    @Override
    public void addCountry(Country country) {
        entityManager.persist(country);
    }

    @Override
    public void removeCountry(Country country) {
        entityManager.remove(country);
    }

    @Override
    public void updateCountry(Country country) {
        entityManager.merge(country);
    }

    @Override
    public Country findCountryByCode(String code) {
        return entityManager.find(Country.class, code);
    }

    @Override
    public List<Country> findCountriesByPartialName(String name) {
        String query = "SELECT country FROM Country country WHERE country.name LIKE '%" + name + "%'";
        return entityManager.createQuery(query, Country.class).getResultList();
    }

    @Override
    public List<Country> findCountriesByCodes(List<String> codes) {
        return codes.stream()
                .map(this::findCountryByCode)
                .toList();
    }

    @Override
    public List<Country> findAllCountries() {
        return entityManager.createQuery("SELECT country FROM Country country", Country.class).getResultList();
    }
}
