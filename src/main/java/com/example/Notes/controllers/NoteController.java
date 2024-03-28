package com.example.Notes.controllers;

import com.example.Notes.DTO.CreateOrUpdateNoteDTO;
import com.example.Notes.DTO.NoteDTO;
import com.example.Notes.models.Note;
import com.example.Notes.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список заметок успешно получен."),
            @ApiResponse(responseCode = "404", description = "Заметки не найдены.")
    })
    public ResponseEntity<List<NoteDTO>> findAllNotes() {
        try {
            List<NoteDTO> noteDTOList =
                    noteService.getAllNotes()
                            .stream().map(this::convertToNoteDTO)
                            .collect(Collectors.toList());
            if (!noteDTOList.isEmpty()) {
                return ResponseEntity.ok(noteDTOList);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Вывод заметки с определенным id",
            description = "Позволяет вывести конкретную заметку"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Вывод заметки с указанным id."),
            @ApiResponse(responseCode = "404", description = "Заметка с указанным id не найдена.")
    })
    public ResponseEntity<NoteDTO> findOneNote(@PathVariable Long id) {
        try {
            Note note = noteService.getOneNote(id);
            NoteDTO noteDTO = convertToNoteDTO(note);
            return new ResponseEntity<>(noteDTO, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/all/versions/notes/{id}")
    @Operation(
            summary = "Вывод всех версий заметок с определенным id",
            description = "Позволяет вывести все версии заметок с определенным id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Вывод всех версий заметок с указанным id."),
            @ApiResponse(responseCode = "404", description = "Заметка с указанным id не найдена.")
    })
    public ResponseEntity<List<NoteDTO>> getNoteVersions(@PathVariable Long id) {
        try {
            List<NoteDTO> revisions = noteService.getOneNotesRevision(id)
                    .stream().map(this::convertToNoteDTO)
                    .collect(Collectors.toList());
            if (!revisions.isEmpty()) {
                return ResponseEntity.ok(revisions);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create")
    @Operation(
            summary = "Создание заметки",
            description = "Позволяет создавать заметки"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Заметка создана."),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка при создании заметки.")
            })
    public ResponseEntity<?> createNote(@RequestBody @Validated CreateOrUpdateNoteDTO createOrUpdateNoteDTO,
                                                 BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors");
        }
        try {
            noteService.createNote(convertCreateOrUpdateNote(createOrUpdateNoteDTO), principal);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating note");
        }
    }

    @PostMapping("/update/{id}")
    @Operation(
            summary = "Редактирование заметки",
            description = "Позволяет редактировать конкретную заметку"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Заметка с указанным id обновлена."),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка при обновлении заметки с указанным id..")
    })
    public ResponseEntity<?> updateNote(@RequestBody @Validated CreateOrUpdateNoteDTO createOrUpdateNoteDTO,
                                                 BindingResult bindingResult, Principal principal,
                                                 @PathVariable Long id) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors");
        }
        try {
            noteService.updateNote(id, convertCreateOrUpdateNote(createOrUpdateNoteDTO), principal);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating note");
        }

    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Удаление конкретной заметки",
            description = "Позволяет удалять конкретную заметку"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Заметка с указанным id удалена."),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка при удалении заметки с указанным id.")
    })
    public ResponseEntity<?> deleteOneNote(@PathVariable Long id, Principal principal) {
        try {
            noteService.deleteOneNote(id, principal);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting note");
        }

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
