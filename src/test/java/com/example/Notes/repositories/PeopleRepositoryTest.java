package com.example.Notes.repositories;

import com.example.Notes.models.Person;
import com.example.Notes.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PeopleRepositoryTest {

    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    void testFindByUsername() {
        Person person = new Person();
        person.setUsername("testuser");

        when(peopleRepository.findByUsername(anyString())).thenReturn(Optional.of(person));

        Optional<Person> foundUser = peopleRepository.findByUsername("testuser");
        assertTrue(foundUser.isPresent());
        assertEquals(person, foundUser.get());

    }

    @Test
    void testFindByEmail() {
        Person person = new Person();
        person.setEmail("test@mail.ru");

        when(peopleRepository.findByEmail(anyString())).thenReturn(Optional.of(person));

        Optional<Person> foundPerson = peopleRepository.findByEmail("test@mail.ru");
        assertTrue(foundPerson.isPresent());
        assertEquals(person, foundPerson.get());
    }
}
