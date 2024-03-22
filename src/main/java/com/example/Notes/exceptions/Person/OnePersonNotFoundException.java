package com.example.Notes.exceptions.Person;

public class OnePersonNotFoundException extends RuntimeException {

    public OnePersonNotFoundException(Long id) {
        super("Person with ID: " + id + ", not found");
    }
}
