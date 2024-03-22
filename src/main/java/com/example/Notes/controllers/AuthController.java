package com.example.Notes.controllers;

import com.example.Notes.DTO.PersonDTO;
import com.example.Notes.DTO.UserLoginRequest;
import com.example.Notes.models.Person;
import com.example.Notes.service.AdminService;
import com.example.Notes.service.PersonService;
import com.example.Notes.util.PersonEmailValidator;
import com.example.Notes.util.PersonValidatorUsername;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;



import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/")
@Tag(name="Аутентификация и регистрация ", description="Работа с аутентификацией и регистрацией")
public class AuthController {

    private final PersonService personService;

    private final ModelMapper modelMapper;

    private final PersonValidatorUsername personValidatorUsername;

    private final PersonEmailValidator personEmailValidator;

    private final AdminService adminService;

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    @Operation(
            summary = "Аутентификация пользователя",
            description = "Позволяет пройти аутентификацию пользователю"
    )
    public ResponseEntity<?> authPerson(@RequestBody UserLoginRequest userLoginRequest) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userLoginRequest.getUsername(), userLoginRequest.getPassword());
        try{
            authenticationManager.authenticate(token);
            SecurityContext sc = SecurityContextHolder.getContext();
            sc.setAuthentication(token);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Неверные данные");
        }

        System.out.println(token);
        return ResponseEntity.ok("OK");

    }

    @PostMapping("/registration/person")
    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет пройти регистрацию пользователю"
    )
    public ResponseEntity<HttpStatus> registrationPerson(@RequestBody @Valid PersonDTO personDTO,
                                                         BindingResult bindingResult) {
        personValidatorUsername.validate(personDTO, bindingResult);
        personEmailValidator.validate(personDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        personService.createPerson(convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/registration/admin")
    @Operation(
            summary = "Регистрация администратора",
            description = "Позволяет пройти регистрацию администратору"
    )
    public ResponseEntity<HttpStatus> registrationAdmin(@RequestBody @Valid PersonDTO personDTO,
                                                         BindingResult bindingResult) {
        personValidatorUsername.validate(personDTO, bindingResult);
        personEmailValidator.validate(personDTO, bindingResult);
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        adminService.createAdmin(convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }
    private Person convertToPerson(PersonDTO personDTO) {
        return modelMapper.map(personDTO, Person.class);
    }

}
