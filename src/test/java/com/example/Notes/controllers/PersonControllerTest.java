package com.example.Notes.controllers;

import com.example.Notes.DTO.PersonDTO;
import com.example.Notes.models.Person;
import com.example.Notes.models.enums.Role;
import com.example.Notes.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
//@WebMvcTest(controllers = PersonController.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonDTO personDTO;

    @MockBean
    private Principal principal;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PasswordEncoder passwordEncoder;


    @Test
    void testFindAllPerson() throws Exception {
        List<Person> personList = Arrays.asList(new Person(), new Person());
        given(personService.getAllPerson()).willReturn(personList);

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

    }

    @Test
    void testGetOnePerson() throws Exception {
        Long testId = 1L;
        Person person = new Person();
        given(personService.getOnePerson(testId)).willReturn(person);
        mockMvc.perform(get("/person/{id}", testId))
                .andExpect(status().isOk());

    }


    @Test
    void testUpdatePerson() throws Exception {
        Person person = new Person();
        person.setUsername("test");
        person.setEmail("test@mail.ru");
        person.setPassword(String.valueOf(1));
        person.setRoles(Collections.singleton(Role.ROLE_USER));

        Person updatePerson = new Person();
        updatePerson.setId(1L);
        updatePerson.setUsername("test");
        updatePerson.setEmail("test@mail.ru");
        updatePerson.setPassword(String.valueOf(1));
        updatePerson.setRoles(Collections.singleton(Role.ROLE_USER));

        doNothing().when(personService).updatePerson(1L, updatePerson, principal);

        mockMvc.perform(post("/person/update/{id}",1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(person)))
                .andExpect(status().isOk());

    }


}
