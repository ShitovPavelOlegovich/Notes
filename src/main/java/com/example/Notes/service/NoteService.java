package com.example.Notes.service;

import com.example.Notes.exceptions.Notes.NotesNotFoundException;
import com.example.Notes.exceptions.Notes.OneNoteNotFoundException;
import com.example.Notes.models.Note;
import com.example.Notes.models.Person;
import com.example.Notes.repositories.NotesRepository;
import com.example.Notes.repositories.PeopleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NoteService {

    private final NotesRepository notesRepository;

    private final PeopleRepository peopleRepository;


    public List<Note> getAllNotes() {
        log.info("Вывод всех заметок: ");
        List<Note> notes = notesRepository.findAll();
        if (notes.isEmpty()) {
            throw new NotesNotFoundException();
        }
        return notes;
    }

    public Note getOneNote(Long id) {
        log.info("Вывод замети под id: " + id);
        Optional<Note> note = notesRepository.findById(id);
        return note.orElseThrow(() -> new OneNoteNotFoundException(id));
    }

    public List <Note> getOneNotesRevision(Long id) {
        log.info("Вывод старых версий заметки: ");
        Revisions<Long, Note> notes = notesRepository.findRevisions(id);
        List<Note> revisions = new ArrayList<>();
        for (Revision<Long, Note> revision : notes) {
            revisions.add(revision.getEntity());
        }
        return revisions;
    }



    @Transactional
    public void createNote(Note note, Principal principal) {
        log.info("Создание заметки: " + note);
        note.setOwnerPerson(getUserByPrincipal(principal));
        note.setDateOfCreated(LocalDateTime.now());
        notesRepository.save(note);
    }

    @Transactional
    public void updateNote(Long id, Note note, Principal principal) {
        log.info("Редактирование заметки под id: " + id);
        Note note1 = notesRepository.findById(id).orElse(null);
        if(note1 != null) {
            if(note1.getOwnerPerson().getUsername().equals(principal.getName())) {
                note.setId(id);
                note.setOwnerPerson(getUserByPrincipal(principal));
                note.setDateOfCreated(note1.getDateOfCreated());
                note.setDateOfUpdate(LocalDateTime.now());
                notesRepository.save(note);
            } else {
                throw new AccessDeniedException("Нельзя изменять заметку, к которой вы не имеете прав доступа");
            }
        }
    }

    @Transactional
    public void deleteOneNote(Long id, Principal principal) {
        log.info("Удаление заметки под id: " + id);
        Optional<Note> note = notesRepository.findById(id);
        if(note.isPresent() && note.get().getOwnerPerson().getUsername().equals(principal.getName())) {
            notesRepository.deleteById(id);
        } else {
            throw new RuntimeException("Заметка не найдена или не принадлежит текущему пользователю");
        }

    }



    public Person getUserByPrincipal(Principal principal) {
        return peopleRepository.findByUsername(principal.getName()).orElse(null);
    }
}
