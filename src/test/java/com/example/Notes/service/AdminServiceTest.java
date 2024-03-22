package com.example.Notes.service;


import com.example.Notes.models.Person;
import com.example.Notes.repositories.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private PeopleRepository peopleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    @Test
    void testCreateAdmin() {
        Person person = new Person();

        person.setPassword(passwordEncoder.encode(person.getPassword()));
        adminService.createAdmin(person);

        verify(peopleRepository, times(1)).save(person);
    }

    @Test
    void testUpdateAdmin() {
        Long testId = 1L;
        Person person = new Person();
        person.setId(testId);
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        adminService.updateAdmin(testId, person);

        verify(peopleRepository, times(1)).save(person);
    }


    @Test
    void testUpdatePerson() {
        Long testId = 1L;
        Person person = new Person();
        person.setId(testId);
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        adminService.updatePerson(testId, person);

        verify(peopleRepository, times(1)).save(person);
    }


    @Test
    void testDeleteAdmin() {
        Long testId = 1L;

        adminService.deleteAdmin(testId);

        verify(peopleRepository, times(1)).deleteById(testId);
    }
}
