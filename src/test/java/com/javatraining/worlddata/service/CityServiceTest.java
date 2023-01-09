package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.CityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @InjectMocks
    private CityService service;

    @Mock
    private CityRepository repository;

    @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;
    @Captor private ArgumentCaptor<String> stringArgumentCaptor;
    @Captor private ArgumentCaptor<City> cityArgumentCaptor;

    private static City oldCity;
    private static City newCity;

    @BeforeAll
    static void setUpBeforeAll() {
        oldCity = new City("Belgrade");
        newCity = new City("Moscow");

        oldCity.setId(1);
        newCity.setId(2);
    }

    @BeforeEach
    void setUpBeforeEach() {
        lenient().when(repository.findAll()).thenReturn(List.of(oldCity, newCity));

        lenient().when(repository.findById(anyInt())).thenReturn(Optional.of(oldCity));
        lenient().when(repository.findById(eq(newCity.getId()))).thenReturn(Optional.empty());

        lenient().when(repository.findByName(anyString())).thenReturn(Optional.of(oldCity));
        lenient().when(repository.findByName(eq(newCity.getName()))).thenReturn(Optional.empty());

        lenient().when(repository.save(any(City.class))).thenReturn(newCity);
        lenient().doNothing().when(repository).delete(any(City.class));
    }

    @Test
    void testCreate_new_shouldSucceed() {
        assertEquals(newCity, service.create(newCity));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verify(repository).save(cityArgumentCaptor.capture());
        assertEquals(newCity.getName(), stringArgumentCaptor.getValue());
        assertEquals(newCity, cityArgumentCaptor.getValue());
    }

    @Test
    void testCreate_old_shouldFail() {
        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(oldCity));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(oldCity.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testUpdate_old_shouldSucceed() {
        assertEquals(newCity, service.update(oldCity.getId(), oldCity));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).save(cityArgumentCaptor.capture());
        assertEquals(oldCity.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCity, cityArgumentCaptor.getValue());
    }

    @Test
    void testUpdate_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.update(newCity.getId(), newCity));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newCity.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testDelete_old_shouldSucceed() {
        assertDoesNotThrow(() -> service.delete(oldCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).delete(cityArgumentCaptor.capture());
        assertEquals(oldCity.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCity, cityArgumentCaptor.getValue());
    }

    @Test
    void testDelete_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.delete(newCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetById_old_shouldSucceed() {
        assertEquals(oldCity, service.getById(oldCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(oldCity.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetById_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.getById(newCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(newCity.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByName_old_shouldSucceed() {
        assertEquals(oldCity, service.getByName(oldCity.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(oldCity.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetByName_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.getByName(newCity.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(newCity.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetAll() {
        List<City> cities = service.getAll();
        assertNotNull(cities);
        assertEquals(2, cities.size());
        assertEquals(oldCity, cities.get(0));
        assertEquals(newCity, cities.get(1));
        verify(repository).findAll();
    }
}
