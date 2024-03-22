package com.example.Notes.exceptions.Notes;

public class NotesNotFoundException extends RuntimeException{

    public NotesNotFoundException() {
        super("Notes not found");
    }
}
