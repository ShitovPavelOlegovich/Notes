package com.example.Notes.service;

import com.example.Notes.models.Note;
import com.example.Notes.models.Person;
import com.example.Notes.repositories.NotesRepository;
import com.example.Notes.repositories.PeopleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NoteServiceTest {

    @Mock
    private NotesRepository notesRepository;

    @Mock
    private Principal principal;

    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private NoteService noteService;


    @Test
    void testGetAllNotes() {
        List<Note> noteList = new ArrayList<>();
        Note note = new Note();
        noteList.add(note);

        when(notesRepository.findAll()).thenReturn(noteList);

        List<Note> result = noteService.getAllNotes();

        assertEquals(noteList, result);
    }

    @Test
    void testGetOneNote() {
        Long testId = 1L;
        Note note = new Note();

        when(notesRepository.findById(testId)).thenReturn(Optional.of(note));

        Note result = noteService.getOneNote(testId);

        assertEquals(note, result);
    }

    @Test
    void testCreateNote() {
        Note note = new Note();
        note.setOwnerPerson(noteService.getUserByPrincipal(principal));
        note.setDateOfCreated(LocalDateTime.now());

        noteService.createNote(note, principal);

        verify(notesRepository, times(1)).save(note);
    }

    @Test
    void testUpdateNote(){
        Long testId = 1L;
        Note note = new Note();
        note.setId(testId);
        note.setOwnerPerson(noteService.getUserByPrincipal(principal));
        note.setDateOfCreated(note.getDateOfCreated());
        note.setDateOfUpdate(LocalDateTime.now());

        noteService.updateNote(testId, note, principal);

        verify(notesRepository, times(1)).save(note);
    }

    @Test
    void testDeleteOneNote() {
        Long testId = 1L;
        noteService.deleteOneNote(testId, principal);
    }


}
