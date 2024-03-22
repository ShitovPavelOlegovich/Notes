package com.example.Notes.controllers;
import com.example.Notes.models.Person;
import com.example.Notes.models.enums.Role;
import com.example.Notes.service.AdminService;
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

import java.util.Collections;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;


    @Test
    void testUpdateAdmin() throws Exception {
        Person admin = new Person();
        admin.setUsername("test");
        admin.setEmail("test@mail.ru");
        admin.setPassword(String.valueOf(1));
        admin.setRoles(Collections.singleton(Role.ROLE_ADMIN));

        Person updateAdmin = new Person();
        updateAdmin.setId(1L);
        updateAdmin.setUsername("test");
        updateAdmin.setEmail("test@mail.ru");
        updateAdmin.setPassword(String.valueOf(1));
        updateAdmin.setRoles(Collections.singleton(Role.ROLE_ADMIN));

        doNothing().when(adminService).updateAdmin(1L, updateAdmin);

        mockMvc.perform(post("/admin/update/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(admin)))
                .andExpect(status().isOk());

    }


    @Test
    void testUpdatePerson() throws Exception {
        Person person = new Person();
        person.setUsername("test");
        person.setEmail("test@mail.ru");
        person.setPassword(String.valueOf(1));
        person.setRoles(Collections.singleton(Role.ROLE_ADMIN));

        Person updatePerson = new Person();
        updatePerson.setId(1L);
        updatePerson.setUsername("test");
        updatePerson.setEmail("test@mail.ru");
        updatePerson.setPassword(String.valueOf(1));
        updatePerson.setRoles(Collections.singleton(Role.ROLE_ADMIN));

        doNothing().when(adminService).updatePerson(1L, updatePerson);

        mockMvc.perform(post("/admin/update/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(person)))
                .andExpect(status().isOk());

    }

    @Test
    void testDeletePerson() throws Exception {
        Person person = new Person();
        person.setId(1L);
        doNothing().when(adminService).deleteAdmin(1L);
        mockMvc.perform(delete("/person/delete/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(person)))
                .andExpect(status().isOk());


        verify(adminService, times(1)).deleteAdmin(1L);

    }
}
