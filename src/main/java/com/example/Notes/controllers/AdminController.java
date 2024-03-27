package com.example.Notes.controllers;

import com.example.Notes.DTO.PersonDTO;
import com.example.Notes.models.Person;
import com.example.Notes.service.AdminService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@ApiIgnore
@SecurityRequirement(name = "BasicAuth")
@Tag(name="Администраторы ", description="Работа с администраторами")
public class AdminController {

    private final PersonService personService;

    private final AdminService adminService;

    private final ModelMapper modelMapper;

    private final PersonValidatorUsername personValidatorUsername;

    private final PersonEmailValidator personEmailValidator;


    @PostMapping("/update/{id}")
    @Operation(
            summary = "Редактирование конкретного администратора",
            description = "Позволяет редактировать конкретного администратора"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Редактирование администратора с указанным id."),
            @ApiResponse(responseCode = "400", description = "Ошибка. Произошла ошибка при редактирование администратора с указанным id .")
    })
    public ResponseEntity<?> updateAdmin(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult, @PathVariable Long id) {
        personValidatorUsername.validate(personDTO, bindingResult);
        personEmailValidator.validate(personDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors");
        }
        try {
            adminService.updateAdmin(id, convertToPerson(personDTO));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating admin");
        }

    }

    @PostMapping("/update/person/{id}")
    @Operation(
            summary = "Редактирование конкретного пользователя",
            description = "Позволяет редактировать конкретного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Данные пользователя с указанным id обновлены."),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка. Данные пользователя с указанным id не обновлены. ")
    })
    public ResponseEntity<?> updatePerson(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult, @PathVariable Long id) {
        personValidatorUsername.validate(personDTO, bindingResult);
        personEmailValidator.validate(personDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body("Validation errors");
        }
        try {
            adminService.updatePerson(id, convertToPerson(personDTO));
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating person");
        }

    }

    @DeleteMapping("/delete/person/{id}")
    @Operation(
            summary = "Удаление пользователя с определенным id",
            description = "Позволяет удалять конкретного пользователя"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Данный пользователь с указанным id удален."),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка. Данный пользователь с указанным id не удален. ")
    })
    public ResponseEntity<?> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePerson(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting person");
        }

    }

    @DeleteMapping("/delete/admin/{id}")
    @Operation(
            summary = "Удаление администратора с определенным id",
            description = "Позволяет удалять конкретного администратора"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный запрос. Данный администратор с указанным id удален."),
            @ApiResponse(responseCode = "400", description = "Произошла ошибка. Данный администратор с указанным id не удален. ")
    })
    public ResponseEntity<?> deleteAdmin(@PathVariable Long id) {
        try {
            adminService.deleteAdmin(id);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting admin");
        }
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
