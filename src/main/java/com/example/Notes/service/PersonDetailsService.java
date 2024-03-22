package com.example.Notes.service;

import com.example.Notes.models.Person;
import com.example.Notes.repositories.PeopleRepository;
import com.example.Notes.security.PersonDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class PersonDetailsService implements UserDetailsService {


    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Проверка наличия входящего логина");
        Optional<Person> person = peopleRepository.findByUsername(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        return new PersonDetails(person.get());
    }

    public Person loadPersonByUsername(String username){
        log.info("Проверка наличия входящего логина");
        Optional<Person> person = peopleRepository.findByUsername(username);
        if (person.isEmpty()) {
            throw new UsernameNotFoundException("Username not found");
        }
        return person.get();
    }
}
