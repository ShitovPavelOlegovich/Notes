package com.example.Notes.exceptions.Person;


public class PersonNotFoundException extends RuntimeException{

    public PersonNotFoundException() {
        super("Person not found");
    }
}
