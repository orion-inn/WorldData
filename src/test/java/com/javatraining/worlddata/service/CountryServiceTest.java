package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.CountryRepository;
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
class CountryServiceTest {

    @InjectMocks
    private CountryService service;

    @Mock
    private CountryRepository repository;

    @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;
    @Captor private ArgumentCaptor<String> stringArgumentCaptor;
    @Captor private ArgumentCaptor<Country> countryArgumentCaptor;

    private static Country oldCountry;
    private static Country newCountry;

    @BeforeAll
    static void setUpBeforeAll() {
        oldCountry = new Country("Yugoslavia");
        newCountry = new Country("Serbia");

        oldCountry.setId(1);
        newCountry.setId(2);
    }

    @BeforeEach
    void setUpBeforeEach() {
        lenient().when(repository.findAll()).thenReturn(List.of(oldCountry, newCountry));

        lenient().when(repository.findById(anyInt())).thenReturn(Optional.of(oldCountry));
        lenient().when(repository.findById(eq(newCountry.getId()))).thenReturn(Optional.empty());

        lenient().when(repository.findByName(anyString())).thenReturn(Optional.of(oldCountry));
        lenient().when(repository.findByName(eq(newCountry.getName()))).thenReturn(Optional.empty());

        lenient().when(repository.save(any(Country.class))).thenReturn(newCountry);
        lenient().doNothing().when(repository).delete(any(Country.class));
    }

    @Test
    void testCreate_new_shouldSucceed() {
        assertEquals(newCountry, service.create(newCountry));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verify(repository).save(countryArgumentCaptor.capture());
        assertEquals(newCountry.getName(), stringArgumentCaptor.getValue());
        assertEquals(newCountry, countryArgumentCaptor.getValue());
    }

    @Test
    void testCreate_old_shouldFail() {
        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(oldCountry));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(oldCountry.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testUpdate_old_shouldSucceed() {
        assertEquals(newCountry, service.update(oldCountry.getId(), oldCountry));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).save(countryArgumentCaptor.capture());
        assertEquals(oldCountry.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCountry, countryArgumentCaptor.getValue());
    }

    @Test
    void testUpdate_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.update(newCountry.getId(), newCountry));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newCountry.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testDelete_old_shouldSucceed() {
        assertDoesNotThrow(() -> service.delete(oldCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).delete(countryArgumentCaptor.capture());
        assertEquals(oldCountry.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCountry, countryArgumentCaptor.getValue());
    }

    @Test
    void testDelete_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.delete(newCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetById_old_shouldSucceed() {
        assertEquals(oldCountry, service.getById(oldCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(oldCountry.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetById_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.getById(newCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(newCountry.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByName_old_shouldSucceed() {
        assertEquals(oldCountry, service.getByName(oldCountry.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(oldCountry.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetByName_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.getByName(newCountry.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(newCountry.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetAll() {
        List<Country> countries = service.getAll();
        assertNotNull(countries);
        assertEquals(2, countries.size());
        assertEquals(oldCountry, countries.get(0));
        assertEquals(newCountry, countries.get(1));
        verify(repository).findAll();
    }
}