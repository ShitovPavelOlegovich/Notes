package com.example.Notes.controllers;

import com.example.Notes.models.Note;
import com.example.Notes.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @MockBean
    private Principal principal;

    @Test
    void testFindAllNotes() throws Exception{
        List<Note> noteList = Arrays.asList(new Note(), new Note());
        given(noteService.getAllNotes()).willReturn(noteList);

        mockMvc.perform(get("/note"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void testFindOneNote() throws Exception {
        Note note = new Note();
        note.setId(1L);

        given(noteService.getOneNote(1L)).willReturn(note);

        mockMvc.perform(get("/note/{id}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateNote() throws Exception {
        Note note = new Note();
        note.setHeader("test");
        note.setNoteText("testTest");
        note.setOwnerPerson(noteService.getUserByPrincipal(principal));

        mockMvc.perform(post("/note/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(note)))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateNote() throws Exception{
        Note note = new Note();
        note.setHeader("test");
        note.setNoteText("testTest");
        note.setOwnerPerson(noteService.getUserByPrincipal(principal));

        Note updateNote = new Note();
        updateNote.setId(1L);
        updateNote.setHeader("test");
        updateNote.setNoteText("testTest");
        updateNote.setOwnerPerson(noteService.getUserByPrincipal(principal));

        doNothing().when(noteService).updateNote(1L, updateNote, principal);

        mockMvc.perform(post("/note/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(note)))
                .andExpect(status().isOk());
    }


    @Test
    void testDeleteOneNote() throws Exception {
        Note note = new Note();
        note.setId(1L);
        note.setOwnerPerson(noteService.getUserByPrincipal(principal));
        doNothing().when(noteService).deleteOneNote(1L, principal);

        mockMvc.perform(delete("/note/delete/{id}", 1L))
                .andExpect(status().isOk());

        verify(noteService, times(1)).deleteOneNote(1L, principal);
    }
}
