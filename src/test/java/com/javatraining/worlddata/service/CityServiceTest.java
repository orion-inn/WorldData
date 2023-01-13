package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.City;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.CityRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
        oldCity.setId(1);

        newCity = new City("Moscow");
        newCity.setId(2);
    }

    @Test
    void testCreateNewShouldSucceed() {
        when(repository.existsByName(eq(newCity.getName()))).thenReturn(false);
        when(repository.save(any(City.class))).thenReturn(newCity);

        assertEquals(newCity, service.create(newCity));
        verify(repository).existsByName(stringArgumentCaptor.capture());
        verify(repository).save(cityArgumentCaptor.capture());
        assertEquals(newCity.getName(), stringArgumentCaptor.getValue());
        assertEquals(newCity, cityArgumentCaptor.getValue());
    }

    @Test
    void testCreateOldShouldFail() {
        when(repository.existsByName(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(oldCity));
        verify(repository).existsByName(stringArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(oldCity.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testUpdateOldShouldSucceed() {
        when(repository.existsById(anyInt())).thenReturn(true);
        when(repository.save(any(City.class))).thenReturn(newCity);

        assertEquals(newCity, service.update(oldCity.getId(), oldCity));
        verify(repository).existsById(integerArgumentCaptor.capture());
        verify(repository).save(cityArgumentCaptor.capture());
        assertEquals(oldCity.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCity, cityArgumentCaptor.getValue());
    }

    @Test
    void testUpdateNewShouldFail() {
        when(repository.existsById(eq(newCity.getId()))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.update(newCity.getId(), newCity));
        verify(repository).existsById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newCity.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testDeleteOldShouldSucceed() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(oldCity));

        assertDoesNotThrow(() -> service.delete(oldCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).delete(cityArgumentCaptor.capture());
        assertEquals(oldCity.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCity, cityArgumentCaptor.getValue());
    }

    @Test
    void testDeleteNewShouldFail() {
        when(repository.findById(eq(newCity.getId()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(newCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newCity.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByIdOldShouldSucceed() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(oldCity));

        assertEquals(oldCity, service.getById(oldCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(oldCity.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByIdNewShouldFail() {
        when(repository.findById(eq(newCity.getId()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(newCity.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(newCity.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByNameOldShouldSucceed() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(oldCity));

        assertEquals(oldCity, service.getByName(oldCity.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(oldCity.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetByNameNewShouldFail() {
        when(repository.findByName(eq(newCity.getName()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getByName(newCity.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(newCity.getName(), stringArgumentCaptor.getValue());
    }
}
