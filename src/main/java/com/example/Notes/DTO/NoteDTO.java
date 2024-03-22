package com.example.Notes.DTO;

import com.example.Notes.models.Person;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoteDTO {

    private String header;
    private String noteText;
    private LocalDateTime dateOfCreated;
    private LocalDateTime dateOfUpdate;
    private Long idOwnerPerson;

}
