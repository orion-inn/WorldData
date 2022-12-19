package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.City;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class JPAWorldDaoTest {

    private static EntityManagerFactory entityManagerFactoryMock;
    private static WorldDao worldDao;
    private static City city;

    @BeforeAll
    static void setUp() {
        entityManagerFactoryMock = mock(EntityManagerFactory.class);
        worldDao = JPAWorldDao.getInstance(entityManagerFactoryMock);
        city = new City(9999, "New City", "XYZ", 1);
    }

    @Test
    void testAddCity() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        EntityTransaction entityTransactionMock = mock(EntityTransaction.class);

        when(entityManagerFactoryMock.createEntityManager()).thenReturn(entityManagerMock);
        when(entityManagerMock.getTransaction()).thenReturn(entityTransactionMock);

        worldDao.addCity(city);

        verify(entityManagerMock).persist(city);
        verify(entityTransactionMock).begin();
        verify(entityTransactionMock).commit();
        verify(entityManagerMock).close();
    }

    @Test
    void testRemoveCity() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        EntityTransaction entityTransactionMock = mock(EntityTransaction.class);

        when(entityManagerFactoryMock.createEntityManager()).thenReturn(entityManagerMock);
        when(entityManagerMock.getTransaction()).thenReturn(entityTransactionMock);
        when(entityManagerMock.merge(city)).thenReturn(city);

        worldDao.removeCity(city);

        verify(entityManagerMock).remove(city);
        verify(entityTransactionMock).begin();
        verify(entityTransactionMock).commit();
        verify(entityManagerMock).close();
    }

    @Test
    void testUpdateCity() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        EntityTransaction entityTransactionMock = mock(EntityTransaction.class);

        when(entityManagerFactoryMock.createEntityManager()).thenReturn(entityManagerMock);
        when(entityManagerMock.getTransaction()).thenReturn(entityTransactionMock);

        worldDao.updateCity(city);

        verify(entityManagerMock).merge(city);
        verify(entityTransactionMock).begin();
        verify(entityTransactionMock).commit();
        verify(entityManagerMock).close();
    }

    @Test
    void testFindCityById() {
        City expected = mock(City.class);
        EntityManager entityManagerMock = mock(EntityManager.class);
        when(entityManagerFactoryMock.createEntityManager()).thenReturn(entityManagerMock);
        when(entityManagerMock.find(City.class, 1)).thenReturn(expected);

        City actual = worldDao.findCityById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void testFindAllCities() {
        EntityManager entityManagerMock = mock(EntityManager.class);
        EntityTransaction entityTransactionMock = mock(EntityTransaction.class);
        TypedQuery<City> query = (TypedQuery<City>) mock(TypedQuery.class);

        when(entityManagerFactoryMock.createEntityManager()).thenReturn(entityManagerMock);
        when(entityManagerMock.getTransaction()).thenReturn(entityTransactionMock);

        List<City> expected = new ArrayList<>();

        when(entityManagerMock.createQuery("SELECT city FROM City city", City.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(expected);

        List<City> actual = worldDao.findAllCities();

        Assertions.assertEquals(expected, actual);
    }

}