package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.Continent;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.ContinentRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContinentServiceTest {

    @InjectMocks
    private ContinentService service;

    @Mock
    private ContinentRepository repository;

    @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;
    @Captor private ArgumentCaptor<String> stringArgumentCaptor;
    @Captor private ArgumentCaptor<Continent> continentArgumentCaptor;

    private static Continent oldContinent;
    private static Continent newContinent;

    @BeforeAll
    static void setUpBeforeAll() {
        oldContinent = new Continent("Europe");
        oldContinent.setId(1);
        oldContinent.setCountries(new HashSet<>());

        newContinent = new Continent("Australia");
        newContinent.setId(2);
        newContinent.setCountries(new HashSet<>());
    }

    @Test
    void testCreateNewShouldSucceed() {
        when(repository.existsByName(eq(newContinent.getName()))).thenReturn(false);
        when(repository.save(any(Continent.class))).thenReturn(newContinent);

        assertEquals(newContinent, service.create(newContinent));
        verify(repository).existsByName(stringArgumentCaptor.capture());
        verify(repository).save(continentArgumentCaptor.capture());
        assertEquals(newContinent.getName(), stringArgumentCaptor.getValue());
        assertEquals(newContinent, continentArgumentCaptor.getValue());
    }

    @Test
    void testCreateOldShouldFail() {
        when(repository.existsByName(anyString())).thenReturn(true);

        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(oldContinent));
        verify(repository).existsByName(stringArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(oldContinent.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testUpdateOldShouldSucceed() {
        when(repository.existsById(anyInt())).thenReturn(true);
        when(repository.save(any(Continent.class))).thenReturn(newContinent);

        assertEquals(newContinent, service.update(oldContinent.getId(), oldContinent));
        verify(repository).existsById(integerArgumentCaptor.capture());
        verify(repository).save(continentArgumentCaptor.capture());
        assertEquals(oldContinent.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldContinent, continentArgumentCaptor.getValue());
    }

    @Test
    void testUpdateNewShouldFail() {
        when(repository.existsById(eq(newContinent.getId()))).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.update(newContinent.getId(), newContinent));
        verify(repository).existsById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newContinent.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testDeleteOldShouldSucceed() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(oldContinent));

        assertDoesNotThrow(() -> service.delete(oldContinent.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).delete(continentArgumentCaptor.capture());
        assertEquals(oldContinent.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldContinent, continentArgumentCaptor.getValue());
    }

    @Test
    void testDeleteNewShouldFail() {
        when(repository.findById(eq(newContinent.getId()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(newContinent.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newContinent.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByIdOldShouldSucceed() {
        when(repository.findById(anyInt())).thenReturn(Optional.of(oldContinent));

        assertEquals(oldContinent, service.getById(oldContinent.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(oldContinent.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByIdNewShouldFail() {
       when(repository.findById(eq(newContinent.getId()))).thenReturn(Optional.empty());

       assertThrows(ResourceNotFoundException.class, () -> service.getById(newContinent.getId()));
       verify(repository).findById(integerArgumentCaptor.capture());
       assertEquals(newContinent.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByNameOldShouldSucceed() {
        when(repository.findByName(anyString())).thenReturn(Optional.of(oldContinent));

        assertEquals(oldContinent, service.getByName(oldContinent.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(oldContinent.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetByNameNewShouldFail() {
        when(repository.findByName(eq(newContinent.getName()))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getByName(newContinent.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(newContinent.getName(), stringArgumentCaptor.getValue());
    }
}