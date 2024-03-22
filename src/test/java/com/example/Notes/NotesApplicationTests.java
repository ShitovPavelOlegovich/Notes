package com.example.Notes;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class NotesApplicationTests {

	@Test
	void contextLoads() {
		NotesApplication notesApplication = new NotesApplication();
		// Проверяем, что экземпляр класса создается без ошибок
		assertNotNull(notesApplication);
	}

}
