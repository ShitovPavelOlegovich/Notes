package com.example.Notes.controllers;

import com.example.Notes.DTO.CreateOrUpdateNoteDTO;
import com.example.Notes.DTO.NoteDTO;
import com.example.Notes.models.Note;
import com.example.Notes.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/note")
@ApiIgnore
@SecurityRequirement(name = "BasicAuth")
@Tag(name="Заметки", description="Работа с заметками")
public class NoteController {

    private final NoteService noteService;

    private final ModelMapper modelMapper;


    @GetMapping()
    @Operation(
            summary = "Вывод всех заметок",
            description = "Позволяет вывести все заметки системы"
    )
    public ResponseEntity<List<NoteDTO>> findAllNotes() {
        List<NoteDTO> noteDTOList =
                noteService.getAllNotes()
                        .stream().map(this::convertToNoteDTO)
                        .collect(Collectors.toList());
        return new ResponseEntity<>(noteDTOList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Вывод заметки с определенным id",
            description = "Позволяет вывести конкретную заметку"
    )
    public ResponseEntity<NoteDTO> findOneNote(@PathVariable Long id) {
        Note note = noteService.getOneNote(id);
        NoteDTO noteDTO = convertToNoteDTO(note);
        return new ResponseEntity<>(noteDTO, HttpStatus.OK);
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание заметки",
            description = "Позволяет создавать заметки"
    )
    public ResponseEntity<?> createNote(@RequestBody @Validated CreateOrUpdateNoteDTO createOrUpdateNoteDTO,
                                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        noteService.createNote(convertCreateOrUpdateNote(createOrUpdateNoteDTO), principal);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    @Operation(
            summary = "Редактирование заметки",
            description = "Позволяет редактировать конкретную заметку"
    )
    public ResponseEntity<HttpStatus> updateNote(@RequestBody @Validated CreateOrUpdateNoteDTO createOrUpdateNoteDTO,
                                                 BindingResult bindingResult, Principal principal,
                                                 @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        noteService.updateNote(id, convertCreateOrUpdateNote(createOrUpdateNoteDTO), principal);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Удаление конкретной заметки",
            description = "Позволяет удалять конкретную заметку"
    )
    public ResponseEntity<HttpStatus> deleteOneNote(@PathVariable Long id, Principal principal) {
        noteService.deleteOneNote(id, principal);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private Note convertToNote(NoteDTO noteDTO) {
        return modelMapper.map(noteDTO, Note.class);
    }

    private Note convertCreateOrUpdateNote(CreateOrUpdateNoteDTO createOrUpdateNoteDTO) {
        return modelMapper.map(createOrUpdateNoteDTO, Note.class);
    }

    private NoteDTO convertToNoteDTO(Note note) {
        return modelMapper.map(note, NoteDTO.class);
    }
}
