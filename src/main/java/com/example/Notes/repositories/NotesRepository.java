package com.example.Notes.repositories;

import com.example.Notes.models.Note;
import com.example.Notes.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotesRepository extends JpaRepository<Note, Long> {
    List<Note> findNoteByOwnerPerson(Person person1);

}
