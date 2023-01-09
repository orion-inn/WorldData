package com.javatraining.worlddata.service;

import com.javatraining.worlddata.entity.Continent;
import com.javatraining.worlddata.exception.ResourceAlreadyExistsException;
import com.javatraining.worlddata.exception.ResourceNotFoundException;
import com.javatraining.worlddata.repository.ContinentRepository;
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
        newContinent = new Continent("Australia");

        oldContinent.setId(1);
        newContinent.setId(2);
    }

    @BeforeEach
    void setUpBeforeEach() {
        lenient().when(repository.findAll()).thenReturn(List.of(oldContinent, newContinent));

        lenient().when(repository.findById(anyInt())).thenReturn(Optional.of(oldContinent));
        lenient().when(repository.findById(eq(newContinent.getId()))).thenReturn(Optional.empty());

        lenient().when(repository.findByName(anyString())).thenReturn(Optional.of(oldContinent));
        lenient().when(repository.findByName(eq(newContinent.getName()))).thenReturn(Optional.empty());

        lenient().when(repository.save(any(Continent.class))).thenReturn(newContinent);
        lenient().doNothing().when(repository).delete(any(Continent.class));
    }

    @Test
    void testCreate_new_shouldSucceed() {
        assertEquals(newContinent, service.create(newContinent));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verify(repository).save(continentArgumentCaptor.capture());
        assertEquals(newContinent.getName(), stringArgumentCaptor.getValue());
        assertEquals(newContinent, continentArgumentCaptor.getValue());
    }

    @Test
    void testCreate_old_shouldFail() {
        assertThrows(ResourceAlreadyExistsException.class, () -> service.create(oldContinent));
        verify(repository).findByName(stringArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(oldContinent.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testUpdate_old_shouldSucceed() {
        assertEquals(newContinent, service.update(oldContinent.getId(), oldContinent));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).save(continentArgumentCaptor.capture());
        assertEquals(oldContinent.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldContinent, continentArgumentCaptor.getValue());
    }

    @Test
    void testUpdate_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.update(newContinent.getId(), newContinent));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
        assertEquals(newContinent.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testDelete_old_shouldSucceed() {
        assertDoesNotThrow(() -> service.delete(oldContinent.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verify(repository).delete(continentArgumentCaptor.capture());
        assertEquals(oldContinent.getId(), integerArgumentCaptor.getValue());
        assertEquals(oldContinent, continentArgumentCaptor.getValue());
    }

    @Test
    void testDelete_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.delete(newContinent.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void testGetById_old_shouldSucceed() {
        assertEquals(oldContinent, service.getById(oldContinent.getId()));
        verify(repository).findById(integerArgumentCaptor.capture());
        assertEquals(oldContinent.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetById_new_shouldFail() {
       assertThrows(ResourceNotFoundException.class, () -> service.getById(newContinent.getId()));
       verify(repository).findById(integerArgumentCaptor.capture());
       assertEquals(newContinent.getId(), integerArgumentCaptor.getValue());
    }

    @Test
    void testGetByName_old_shouldSucceed() {
        assertEquals(oldContinent, service.getByName(oldContinent.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(oldContinent.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetByName_new_shouldFail() {
        assertThrows(ResourceNotFoundException.class, () -> service.getByName(newContinent.getName()));
        verify(repository).findByName(stringArgumentCaptor.capture());
        assertEquals(newContinent.getName(), stringArgumentCaptor.getValue());
    }

    @Test
    void testGetAll() {
        List<Continent> continents = service.getAll();
        assertNotNull(continents);
        assertEquals(2, continents.size());
        assertEquals(oldContinent, continents.get(0));
        assertEquals(newContinent, continents.get(1));
        verify(repository).findAll();
    }
}