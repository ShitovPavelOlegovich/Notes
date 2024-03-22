package com.example.Notes.controllers;

import com.example.Notes.DTO.PersonDTO;
import com.example.Notes.models.Person;
import com.example.Notes.models.enums.Role;
import com.example.Notes.service.AdminService;
import com.example.Notes.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

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
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AdminService adminService;


    @Test
    void testRegistrationPerson() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setUsername("test");
        person.setEmail("test@mail.ru");
        person.setPassword(String.valueOf(1));
        person.setRoles(Collections.singleton(Role.ROLE_USER));


        mockMvc.perform(post("/api/auth/registration/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(person)))
                .andExpect(status().isOk());


    }

    @Test
    void testRegistrationAdmin() throws Exception {
        Person person = new Person();
        person.setId(1L);
        person.setUsername("test");
        person.setEmail("test@mail.ru");
        person.setPassword(String.valueOf(1));
        person.setRoles(Collections.singleton(Role.ROLE_ADMIN));



        mockMvc.perform(post("/api/auth/registration/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(person)))
                .andExpect(status().isOk());


    }

}
