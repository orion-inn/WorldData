package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.Country;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.CountryRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

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
        oldCountry.setId(1);

        newCountry = new Country("Serbia");
        newCountry.setId(2);
    }

    @Test
    void testCreateNewShouldSucceed() {
        when(repository.findByName(eq(newCountry.getName()))).thenReturn(Optional.empty());
        when(repository.save(any(Country.class))).thenReturn(newCountry);

        assertEquals(newCountry, service.create(newCountry));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verify(repository).save(countryArgumentCaptor.capture());
        assertEquals(newCountry.getName(), stringArgumentCaptor.getValue());
        assertEquals(newCountry, countryArgumentCaptor.getValue());
    }

    @Test
    void testCreateOldShouldFail() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(oldCountry));

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(oldCountry));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(oldCountry.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testUpdateOldShouldSucceed() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(oldCountry));
        when(repository.save(any(Country.class))).thenReturn(newCountry);

        assertEquals(newCountry, service.update(oldCountry.getId(), oldCountry));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).save(countryArgumentCaptor.capture());
        assertEquals(oldCountry.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCountry, countryArgumentCaptor.getValue());
    }

    @Test
    void testUpdateNewShouldFail() {
        when(repository.findById(eq(newCountry.getId()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.update(newCountry.getId(), newCountry));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newCountry.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testDeleteOldShouldSucceed() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(oldCountry));

        assertDoesNotThrow(() -> service.delete(oldCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).delete(countryArgumentCaptor.capture());
        assertEquals(oldCountry.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldCountry, countryArgumentCaptor.getValue());
    }

    @Test
    void testDeleteNewShouldFail() {
        when(repository.findById(eq(newCountry.getId()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(newCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newCountry.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByIdOldShouldSucceed() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(oldCountry));

        assertEquals(oldCountry, service.getById(oldCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(oldCountry.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByIdNewShouldFail() {
        when(repository.findById(eq(newCountry.getId()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getById(newCountry.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(newCountry.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByNameOldShouldSucceed() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(oldCountry));

        assertEquals(oldCountry, service.getByName(oldCountry.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(oldCountry.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetByNameNewShouldFail() {
        when(repository.findByName(eq(newCountry.getName()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getByName(newCountry.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(newCountry.getName(), stringArgumentCaptor.getValue());
    }
}
