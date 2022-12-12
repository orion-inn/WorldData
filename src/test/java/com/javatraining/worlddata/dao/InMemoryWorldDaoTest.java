package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.entity.Country;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.javatraining.worlddata.helper.CountryCityCountPair;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWorldDaoTest {

    private static InMemoryWorldDao worldDao;
    private static Map<String, Country> countries;
    private static Map<Integer, City> cities;

    // Setup

    @BeforeAll
    static void setup() {

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("world-data-pu");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        TypedQuery<City> citiesQuery = null;
        try {
            entityManager.getTransaction().begin();
            citiesQuery = entityManager.createQuery("from City", City.class);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }

        if (citiesQuery != null) {
            cities = new ConcurrentHashMap<>();
            citiesQuery.getResultStream()
                    .forEach(city -> cities.put(city.getId(), city));
        }

        TypedQuery<Country> countriesQuery = null;
        try {
            entityManager.getTransaction().begin();
            countriesQuery = entityManager.createQuery("from Country", Country.class);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }

        if (countriesQuery != null) {
            countries = new ConcurrentHashMap<>();
            countriesQuery.getResultStream()
                    .forEach(country -> countries.put(country.getCode(), country));
        }

        Set<String> continents = new HashSet<>();
        for (Country country : countries.values()) {
            continents.add(country.getContinent());
        }

        worldDao = InMemoryWorldDao.getInstance();
        worldDao.setContinents(continents);
        worldDao.setCountries(countries);
        worldDao.setCities(cities);
    }

    // Test cases

    @Test
    @DisplayName("Test adding a new country")
    void testAddingNewCountry() {
        Country country = new Country("XXX", "A Country", "Europe", 1, 1.0, 1.0, 9999);
        assertNotNull(worldDao.addCountry(country));
        assertEquals(country, worldDao.getCountries().get(country.getCode()));
    }

    @Test
    @DisplayName("Test adding a country with an existing code but different other values")
    void testAddingExistingCountry() {
        Country country = new Country("RUS", "Rusija", "Europe", 146934000, 17075400.00, 276608.00, 3580);
        assertNull(worldDao.addCountry(country));
        assertNotEquals(country.getName(), worldDao.getCountries().get(country.getCode()).getName());
    }

    @Test
    @DisplayName("Test removing an existing country")
    void testRemovingExistingCountry() {
        Country country = new Country("YUG", "Yugoslavia", "Europe", 10640000, 102173.00, 17000.00, 1792);
        assertEquals(country, worldDao.removeCountry(country));
        assertNull(worldDao.getCountries().get(country.getCode()));
    }

    @Test
    @DisplayName("Test removing a non-existing country")
    void testRemovingNonExistingCountry() {
        Country country = new Country("YYY", "Y Country", "Europe", 1, 1.0, 1.0, 8888);
        assertNull(worldDao.removeCountry(country));
    }

    @Test
    @DisplayName("Test updating a country with an existing code but different other values")
    void testUpdatingExistingCountry() {
        Country country = new Country("LTU", "Litvanija", "Europe", 3698500, 65301.00, 10692.00, 2447);
        assertNotNull(worldDao.updateCountry(country));
        assertEquals(country.getName(), worldDao.getCountries().get(country.getCode()).getName());
    }

    @Test
    @DisplayName("Test updating a non-existing country")
    void testUpdatingNonExistingCountry() {
        Country country = new Country("ZZZ", "Z Country", "Europe", 1, 1.0, 1.0, 7777);
        assertNull(worldDao.updateCountry(country));
        assertNull(worldDao.getCountries().get(country.getCode()));
    }

    @Test
    @DisplayName("Test adding a new city")
    void testAddingNewCity() {
        City city = new City(9999, "X City", "SRB", 1);
        assertNotNull(worldDao.addCity(city));
        assertEquals(city, worldDao.getCities().get(city.getId()));
    }

    @Test
    @DisplayName("Test adding a city with an existing id but different other values")
    void testAddingExistingCity() {
        City city = new City(3580, "Moskva", "RUS", 8389200);
        assertNull(worldDao.addCity(city));
        assertNotEquals(city.getName(), worldDao.getCities().get(city.getId()).getName());
    }

    @Test
    @DisplayName("Test removing an existing city")
    void testRemovingExistingCity() {
        City city = new City(1792, "Beograd", "YUG", 1204000);
        assertEquals(city, worldDao.removeCity(city));
        assertNull(worldDao.getCities().get(city.getId()));
    }

    @Test
    @DisplayName("Test removing a non-existing city")
    void testRemovingNonExistingCity() {
        City city = new City(8888, "Y City", "SRB", 1);
        assertNull(worldDao.removeCity(city));
    }

    @Test
    @DisplayName("Test updating a city with an existing id but different other values")
    void testUpdatingExistingCity() {
        City city = new City(2447, "Viljnus", "LTU", 577969);
        assertNotNull(worldDao.updateCity(city));
        assertEquals(city.getName(), worldDao.getCities().get(city.getId()).getName());
    }

    @Test
    @DisplayName("Test updating a non-existing city")
    void testUpdatingNonExistingCity() {
        City city = new City(7777, "Z City", "SRB", 1);
        assertNull(worldDao.updateCity(city));
        assertNull(worldDao.getCities().get(city.getId()));
    }

    @Test
    @DisplayName("Test finding the most populated capital city")
    void testFindingHighestPopulatedCapitalCity() {
        City city = new City(2331, "Seoul", "KOR", 9981619);
        assertEquals(city, worldDao.findHighestPopulatedCapitalCity());
    }

    @Test
    @DisplayName("Test sorting countries by city count in descending order")
    void testSortingCountriesByCityCountInDescOrder() {
        List<String> expectedHead = List.of("CHN", "IND", "USA");
        List<String> expectedTail = List.of("BVT", "HMD", "IOT");
        List<CountryCityCountPair> sortedCountries = worldDao.getCountriesSortedByCityCountInDescOrder();
        List<String> actualHead = sortedCountries.stream()
                .limit(3)
                .map(countryCityCountPair -> countryCityCountPair.getCountry().getCode())
                .toList();
        List<String> actualTail = sortedCountries.stream()
                .skip(sortedCountries.size() - 3)
                .limit(3)
                .map(countryCityCountPair -> countryCityCountPair.getCountry().getCode())
                .toList();

        assertEquals(worldDao.getCountries().size(), sortedCountries.size());

        assertEquals(expectedHead.get(0), actualHead.get(0));
        assertEquals(expectedHead.get(1), actualHead.get(1));
        assertEquals(expectedHead.get(2), actualHead.get(2));

        assertEquals(expectedTail.get(0), actualTail.get(0));
        assertEquals(expectedTail.get(1), actualTail.get(1));
        assertEquals(expectedTail.get(2), actualTail.get(2));

        assertTrue(sortedCountries.get(0).getCount() >= sortedCountries.get(1).getCount());
        assertTrue(sortedCountries.get(1).getCount() >= sortedCountries.get(2).getCount());
        assertTrue(sortedCountries.get(2).getCount() >= sortedCountries.get(3).getCount());
        assertTrue(sortedCountries.get(100).getCount() >= sortedCountries.get(101).getCount());
        assertTrue(sortedCountries.get(101).getCount() >= sortedCountries.get(102).getCount());
        assertTrue(sortedCountries.get(102).getCount() >= sortedCountries.get(103).getCount());
        assertTrue(sortedCountries.get(sortedCountries.size() - 4).getCount() >= sortedCountries.get(sortedCountries.size() - 3).getCount());
        assertTrue(sortedCountries.get(sortedCountries.size() - 3).getCount() >= sortedCountries.get(sortedCountries.size() - 2).getCount());
        assertTrue(sortedCountries.get(sortedCountries.size() - 2).getCount() >= sortedCountries.get(sortedCountries.size() - 1).getCount());
    }

}