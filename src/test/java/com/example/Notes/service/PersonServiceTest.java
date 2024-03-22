package com.example.Notes.service;

import com.example.Notes.models.Person;
import com.example.Notes.repositories.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PeopleRepository peopleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private Principal principal;
    @InjectMocks
    private PersonService personService;

    @Test
    void testGetAllPerson() {
        List<Person> personList = new ArrayList<>();
        Person person = new Person();
        personList.add(person);
        when(peopleRepository.findAll()).thenReturn(personList);

        List<Person> result = personService.getAllPerson();

        assertEquals(personList, result);

        verify(peopleRepository, times(1)).findAll();
    }

    @Test
    void testGetOnePerson() {
        Long testId = 1L;
        Person person = new Person();
        when(peopleRepository.findById(testId)).thenReturn(Optional.of(person));

        Person result = personService.getOnePerson(testId);

        assertEquals(person, result);

    }

    @Test
    void testCreatePerson() {
        Person person = new Person();

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        personService.createPerson(person);

        verify(peopleRepository, times(1)).save(person);
    }

    @Test
    void testUpdatePerson() {
        Long testId = 1L;
        Person person = new Person();
        person.setId(testId);
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        personService.updatePerson(testId, person, principal);

        verify(peopleRepository, times(1)).save(person);
    }

    @Test
    void deletePerson() {
        Long testId = 1L;

        personService.deletePerson(testId);

        verify(peopleRepository, times(1)).deleteById(1L);
    }
}
