package com.example.Notes.models;

import com.example.Notes.models.enums.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "person")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Поле с username не должно быть пустым")
    @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
    @Column(name = "username")
    private String username;

    @NotEmpty(message = "Поле с email не должно быть пустым")
    @Email
    @Column(name = "email", unique = true)
    private String email;

    @NotEmpty(message = "Поле с паролем не должно быть пустым")
    @Column(name = "password", length = 1000)
    private String password;

    @JsonBackReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerPerson")
    private List<Note> notes;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "person_role",
            joinColumns = @JoinColumn(name = "id"))
    @Enumerated(EnumType.STRING)
    private Set<Role> roles;

}
