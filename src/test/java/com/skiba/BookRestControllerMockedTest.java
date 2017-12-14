package com.skiba;

import com.skiba.api.BookEndpoint;
import com.skiba.model.Book;
import com.skiba.repository.BookRepository;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(BookEndpoint.class)
public class BookRestControllerMockedTest {

    public static final long BOOK_ID = 3l;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookRepository service;


    @Test
    public void shouldFindAll() throws Exception {
        //given
        when(service.findAll()).thenReturn(Lists.newArrayList(new Book("a", "b", "c")));

        //when
        this.mockMvc.perform(get("/api/books")).andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("a")));
    }

    @Test
    public void shouldFindOneBook() throws Exception {

        //given
        final Book book = new Book("a", "b", "c");
        book.setId(BOOK_ID);
        when(service.findOne(BOOK_ID)).thenReturn(book);

        //when
        this.mockMvc.perform(get("/api/books/" + BOOK_ID)).andDo(print())
                //then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("a")));
    }


    @Test
    public void shouldNotFindOneBook() throws Exception {

        //given
        //when
        this.mockMvc.perform(get("/api/books/1")).andDo(print())
      //then
        .andExpect(status().isNotFound());
    }


}
