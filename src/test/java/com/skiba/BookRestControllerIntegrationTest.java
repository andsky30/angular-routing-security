package com.skiba;

import com.skiba.model.Book;
import com.skiba.repository.BookRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BookRestControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void shouldReturnAllBooks() {
        //given
        //when
        List body = this.restTemplate.getForObject("/api/books", List.class);

        //then
        assertThat(body).hasSize(3);
    }





    @Test
    public void shouldReteurnFirstBook() {
        //given
        //when
        ResponseEntity<Book> responseEntity = this.restTemplate.getForEntity("/api/books/1", Book.class);

        //then

        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        final Book book = responseEntity.getBody();
        assertThat(book).isNotNull();
        assertThat(book.getId()).isEqualTo(1l);
    }


    @Test
    public void shouldSaveBookAndReturnIt() {
        //given
        final String author = "Orwell";
        final String title = "1984";
        final String isbn = "333";
        Book newBook = new Book(title, author, isbn);
        //when
        ResponseEntity<Book> responseEntity = this.restTemplate.postForEntity("/api/books/", newBook, Book.class);

        //then
        assertThat(responseEntity).isNotNull();
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        final Book book = responseEntity.getBody();
        assertThat(book).isNotNull();
        assertThat(book.getAuthor()).isEqualTo(author);
        assertThat(book.getIsbn()).isEqualTo(isbn);
        assertThat(book.getTitle()).isEqualTo(title);


        final Book bookRetrievedAgainFromRepo = bookRepository.getOne(book.getId());
        assertThat(bookRetrievedAgainFromRepo).isNotNull();
    }

}