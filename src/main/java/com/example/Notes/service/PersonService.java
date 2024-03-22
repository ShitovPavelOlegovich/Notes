package com.example.Notes.service;

import com.example.Notes.exceptions.Person.OnePersonNotFoundException;
import com.example.Notes.exceptions.Person.PersonNotFoundException;
import com.example.Notes.models.Note;
import com.example.Notes.models.Person;
import com.example.Notes.models.enums.Role;
import com.example.Notes.repositories.NotesRepository;
import com.example.Notes.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PersonService {

    private final PeopleRepository peopleRepository;

    private final PasswordEncoder passwordEncoder;

    private final NotesRepository notesRepository;

    public List<Person> getAllPerson() {
        log.info("Вывод всех пользователей:");
        List<Person> personList = peopleRepository.findAll();
        if (personList.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return personList;
    }

    public Person getOnePerson(Long id) {
        log.info("Вывод пользователей с конкретный id:" + id);
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElseThrow(() -> new OnePersonNotFoundException(id));
    }

    @Transactional
    public void createPerson(Person person) {
        log.info("Регистрация пользователя");
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setRoles(Collections.singleton(Role.ROLE_USER));
        peopleRepository.save(person);
    }

    @Transactional
    public void updatePerson(Long id, Person person, Principal principal) {
        Optional <Person> loggedInPerson = peopleRepository.findByUsername(principal.getName());
        if (loggedInPerson.isPresent() && loggedInPerson.get().getId().equals(id)) {
            log.info("Редактирование профиля пользователя с id:" + id);
            person.setId(id);
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            person.setRoles(Collections.singleton(Role.ROLE_USER));
            peopleRepository.save(person);
        } else {
            try {
                throw new AccessDeniedException("Вы не имеете прав для редактирования данного профиля");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Transactional
    public void deletePerson(Long id) {
        log.info("Удаление пользователя под id: " + id);
        peopleRepository.deleteById(id);
    }

    public List<Note> getNoteByPerson(Long id) {
        log.info("Поиск заметок: ");
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            Person person1 = person.get();
            return notesRepository.findNoteByOwnerPerson(person1);
        }
        return null;
    }

}
