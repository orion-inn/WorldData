package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;

import jakarta.persistence.*;

import java.util.List;
import java.util.function.Consumer;

public class JPAWorldDao implements WorldDao {

    private static JPAWorldDao instance;

    public static JPAWorldDao getInstance(EntityManagerFactory entityManagerFactory) {
        synchronized (JPAWorldDao.class) {
            if (instance == null) {
                instance = new JPAWorldDao(entityManagerFactory);
            }
        }

        return instance;
    }

    private EntityManagerFactory entityManagerFactory;

    private JPAWorldDao() {
        entityManagerFactory = Persistence.createEntityManagerFactory("world-data");
    }

    private JPAWorldDao(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    private void executeInsideTransaction(Consumer<EntityManager> action) {
        final EntityManager entityManager = entityManagerFactory.createEntityManager();
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            action.accept(entityManager);
            transaction.commit();
        } catch (RuntimeException e) {
            transaction.rollback();
            throw e;
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void addCity(City city) {
        executeInsideTransaction(entityManager -> entityManager.persist(city));
    }

    @Override
    public void removeCity(City city) {
        executeInsideTransaction(entityManager -> entityManager.remove(entityManager.merge(city)));
    }

    @Override
    public void updateCity(City city) {
        executeInsideTransaction(entityManager -> entityManager.merge(city));
    }

    @Override
    public City findCityById(int id) {
        return entityManagerFactory.createEntityManager().find(City.class, id);
    }

    @Override
    public List<City> findAllCities() {
        return entityManagerFactory.createEntityManager()
                .createQuery("SELECT city FROM City city", City.class).getResultList();
    }

    @Override
    public void addCountry(Country country) {
        executeInsideTransaction(entityManager -> entityManager.persist(country));
    }

    @Override
    public void removeCountry(Country country) {
        executeInsideTransaction(entityManager -> entityManager.remove(entityManager.merge(country)));
    }

    @Override
    public void updateCountry(Country country) {
        executeInsideTransaction(entityManager -> entityManager.merge(country));
    }

    @Override
    public Country findCountryByCode(String code) {
        return entityManagerFactory.createEntityManager().find(Country.class, code);
    }

    @Override
    public List<Country> findAllCountries() {
        return entityManagerFactory.createEntityManager()
                .createQuery("SELECT country FROM Country country", Country.class).getResultList();
    }
}
