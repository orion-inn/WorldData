package com.javatraining.worlddata.dao;

import com.javatraining.worlddata.entity.City;
import jakarta.persistence.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JPAWorldDaoTest {

    @Mock private EntityManagerFactory entityManagerFactoryMock;
    @Mock private EntityManager entityManagerMock;
    @Mock private EntityTransaction entityTransactionMock;
    @Mock private TypedQuery<City> queryMock;

    @InjectMocks private JPAWorldDao worldDao;

    @Captor private ArgumentCaptor<City> cityArgumentCaptor;
    @Captor private ArgumentCaptor<Class<City>> cityClassArgumentCaptor;
    @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;
    @Captor private ArgumentCaptor<String> stringArgumentCaptor;

    private static City city;

    @BeforeAll
    static void setUpBeforeAll() {
        city = new City(9999, "New City", "XYZ", 1);
    }

    @BeforeEach
    void setUpBeforeEach() {
        lenient().when(entityManagerFactoryMock.createEntityManager()).thenReturn(entityManagerMock);
        lenient().when(entityManagerMock.getTransaction()).thenReturn(entityTransactionMock);
        lenient().when(entityManagerMock.merge(any(City.class))).thenReturn(city);
        lenient().when(entityManagerMock.find(eq(City.class), anyInt())).thenReturn(city);
        lenient().when(entityManagerMock.createQuery(anyString(), eq(City.class))).thenReturn(queryMock);
    }

    @Test
    void testAddCity() {
        worldDao.addCity(city);
        verify(entityManagerMock).persist(any(City.class));
        verify(entityTransactionMock).begin();
        verify(entityTransactionMock).commit();
        verify(entityManagerMock).close();

        verify(entityManagerMock).persist(cityArgumentCaptor.capture());
        assertEquals(city, cityArgumentCaptor.getValue());
    }

    @Test
    void testRemoveCity() {
        worldDao.removeCity(city);
        verify(entityManagerMock).remove(any(City.class));
        verify(entityTransactionMock).begin();
        verify(entityTransactionMock).commit();
        verify(entityManagerMock).close();

        verify(entityManagerMock).remove(cityArgumentCaptor.capture());
        assertEquals(city, cityArgumentCaptor.getValue());
    }

    @Test
    void testUpdateCity() {
        worldDao.updateCity(city);
        verify(entityManagerMock).merge(any(City.class));
        verify(entityTransactionMock).begin();
        verify(entityTransactionMock).commit();
        verify(entityManagerMock).close();

        verify(entityManagerMock).merge(cityArgumentCaptor.capture());
        assertEquals(city, cityArgumentCaptor.getValue());
    }

    @Test
    void testFindCityById() {
        assertEquals(city, worldDao.findCityById(1));
        verify(entityManagerMock).find(eq(City.class), anyInt());

        verify(entityManagerMock).find(cityClassArgumentCaptor.capture(),
                                       integerArgumentCaptor.capture());
        assertEquals(City.class, cityClassArgumentCaptor.getValue());
        assertEquals(1, integerArgumentCaptor.getValue());
    }

    @Test
    void testFindAllCities() {
        assertEquals(queryMock.getResultList(), worldDao.findAllCities());
        verify(entityManagerMock).createQuery(anyString(), eq(City.class));

        verify(entityManagerMock).createQuery(stringArgumentCaptor.capture(),
                                            cityClassArgumentCaptor.capture());
        assertEquals("SELECT city FROM City city", stringArgumentCaptor.getValue());
        assertEquals(City.class, cityClassArgumentCaptor.getValue());
    }

}