package com.example.Notes.exceptions.Notes;

public class OneNoteNotFoundException extends RuntimeException{

    public OneNoteNotFoundException(Long id) {
        super("Note with ID: " + id + ", not found");
    }
}
