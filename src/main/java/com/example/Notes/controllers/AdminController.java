package com.example.Notes.controllers;

import com.example.Notes.DTO.PersonDTO;
import com.example.Notes.models.Person;
import com.example.Notes.service.AdminService;
import com.example.Notes.service.PersonService;
import com.example.Notes.util.PersonEmailValidator;
import com.example.Notes.util.PersonValidatorUsername;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<HttpStatus> updateAdmin(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult, @PathVariable Long id) {
        personValidatorUsername.validate(personDTO, bindingResult);
        personEmailValidator.validate(personDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        adminService.updateAdmin(id, convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/update/person/{id}")
    @Operation(
            summary = "Редактирование конкретного пользователя",
            description = "Позволяет редактировать конкретного пользователя"
    )
    public ResponseEntity<HttpStatus> updatePerson(@RequestBody @Valid PersonDTO personDTO,
                                                   BindingResult bindingResult, @PathVariable Long id) {
        personValidatorUsername.validate(personDTO, bindingResult);
        personEmailValidator.validate(personDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        adminService.updatePerson(id, convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/person/{id}")
    @Operation(
            summary = "Удаление пользователя с определенным id",
            description = "Позволяет удалять конкретного пользователя"
    )
    public ResponseEntity<HttpStatus> deletePerson(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/delete/admin/{id}")
    @Operation(
            summary = "Удаление администратора с определенным id",
            description = "Позволяет удалять конкретного администратора"
    )
    public ResponseEntity<HttpStatus> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }
}
