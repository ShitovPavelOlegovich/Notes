package com.example.Notes.controllers;



import com.example.Notes.DTO.PersonDTO;
import com.example.Notes.DTO.PersonFullInfoDTO;
import com.example.Notes.models.Person;
import com.example.Notes.service.NoteService;
import com.example.Notes.service.PersonService;
import com.example.Notes.util.PersonEmailValidator;
import com.example.Notes.util.PersonValidatorUsername;
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
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

//@SecurityScheme(
//        name = "BasicAuth",
//        type = SecuritySchemeType.HTTP,
//        scheme = "basic"
//)
@RestController
@RequestMapping("/person")
@RequiredArgsConstructor
@ApiIgnore
@SecurityRequirement(name = "BasicAuth")
@Tag(name="Пользователи", description="Работа с пользователями")
public class PersonController {

    private final PersonService personService;

    private final PersonValidatorUsername personValidatorUsername;

    private final PersonEmailValidator personEmailValidator;

    private final NoteService noteService;

    private final ModelMapper modelMapper;

    @GetMapping()
    @Operation(
            summary = "Вывод всех пользователей",
            description = "Позволяет вывести всех пользователей системы"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список пользователей успешно получен."),
            @ApiResponse(responseCode = "404", description = "Пользователи не найдены.")
    })
    public ResponseEntity<List<PersonFullInfoDTO>> findAllPerson() {
        List<PersonFullInfoDTO> personDTOS =
                personService.getAllPerson()
                        .stream().map(this::convertToPersonFullInfo)
                        .collect(Collectors.toList());
        return new ResponseEntity<>(personDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Вывод пользователя с определенным id",
            description = "Позволяет вывести конкретного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Возвращает информацию о пользователе."),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным id не найден.")
    })
    public ResponseEntity<PersonFullInfoDTO> getOnePerson(@PathVariable Long id) {
        Person person = personService.getOnePerson(id);
        PersonFullInfoDTO personFullInfoDTO = convertToPersonFullInfo(person);
        return new ResponseEntity<>(personFullInfoDTO, HttpStatus.OK);
    }

    @GetMapping("/note/owner/person/{id}")
    @Operation(
            summary = "Вывод заметок принадлежащие конкретному пользователю",
            description = "Позволяет вывести заметки принадлежащие конкретному пользователю"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Возвращает заметки пользователя."),
            @ApiResponse(responseCode = "404", description = "Заметки у пользователя отсутствуют.")
    })
    public ResponseEntity<?> getNoteByPersonId(@PathVariable Long id) {
        try {
            Person person = personService.getOnePerson(id);
            personService.getNoteByPerson(person.getId());
            return new ResponseEntity<>(person.getNotes(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error find note by person");
        }

    }

    @PostMapping("/update/{id}")
    @Operation(
            summary = "Редактирование пользователя с определенным id.",
            description = "Позволяет редактировать профиль конкретного пользователя."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Ваши данные обновлены."),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка. Ваши данные не обновлены. ")
    })
    public ResponseEntity<?> updatePerson(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult, @PathVariable Long id, Principal principal) {
        personValidatorUsername.validate(personDTO, bindingResult);
        personEmailValidator.validate(personDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors");
        }
        try {
            personService.updatePerson(id,convertToPerson(personDTO), principal);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating person");
        }

    }


    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

    private PersonFullInfoDTO convertToPersonFullInfo(Person person) {
        return modelMapper.map(person, PersonFullInfoDTO.class);
    }
    private PersonDTO convertToPersonDTO(Person person) {
        return modelMapper.map(person, PersonDTO.class);
    }
}
