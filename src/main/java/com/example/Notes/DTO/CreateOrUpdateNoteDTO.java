package com.example.Notes.DTO;

import lombok.Data;

@Data
public class CreateOrUpdateNoteDTO {

    private String header;
    private String noteText;
}
