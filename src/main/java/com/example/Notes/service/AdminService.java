package com.example.Notes.service;


import com.example.Notes.models.Person;
import com.example.Notes.models.enums.Role;
import com.example.Notes.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AdminService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void createAdmin(Person person) {
        log.info("Регистрация пользователя c ролью администратора");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(Collections.singleton(Role.ROLE_ADMIN));
        peopleRepository.save(person);
    }

    @Transactional
    public void updateAdmin(Long id, Person person) {
        log.info("Редактирование пользователя c ролью администратора под id:" + id);
        person.setId(id);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(Collections.singleton(Role.ROLE_ADMIN));
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(Long id, Person person) {
        log.info("Редактирование пользователя с id:" + id);
        person.setId(id);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(Collections.singleton(Role.ROLE_USER));
        peopleRepository.save(person);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        log.info("Удаление пользователя c ролью администратора под id: " + id);
        peopleRepository.deleteById(id);
    }
}
