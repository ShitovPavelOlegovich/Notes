package com.example.Notes.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "note")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty(message = "Поле с заголовком не должно быть пустым")
    @Size(min = 2, max = 100, message = "Заголовок должно быть от 2 до 100 символов")
    @Column(name = "header", length = 100)
    private String header;

    @NotEmpty(message = "Поле с текстом заметки не должно быть пустым")
    @Size(min = 5 , max = 300, message = "Текст заметки должен содержать от 5 до 300 символов")
    @Column(name = "note_text", length = 300)
    private String noteText;

    @Column(name = "date_of_created")
    private LocalDateTime dateOfCreated;

    @Column(name = "date_of_update")
    private LocalDateTime dateOfUpdate;

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person ownerPerson;

    @PrePersist
    private void init() {
        dateOfCreated = LocalDateTime.now();
    }
}
