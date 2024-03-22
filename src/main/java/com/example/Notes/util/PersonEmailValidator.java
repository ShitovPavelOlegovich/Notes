package com.example.Notes.util;

import com.example.Notes.DTO.PersonDTO;
import com.example.Notes.models.Person;
import com.example.Notes.repositories.PeopleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class PersonEmailValidator implements Validator {

    private final PeopleRepository peopleRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        PersonDTO personDTO = (PersonDTO) target;
        if(peopleRepository.findByEmail(personDTO.getEmail()).isPresent()) {
            errors.rejectValue("email", "", "Пользователь с данным email уже существует!");
        }
    }
}
