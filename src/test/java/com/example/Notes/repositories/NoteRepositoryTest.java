package com.example.Notes.repositories;

import com.example.Notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NoteRepositoryTest {

    @Mock
    private NotesRepository notesRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void testFindNoteByOwnerPerson() {

    }
}
