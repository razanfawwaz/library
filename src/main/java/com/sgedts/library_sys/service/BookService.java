package com.sgedts.library_sys.service;

import com.sgedts.library_sys.model.Book;
import com.sgedts.library_sys.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sgedts.library_sys.bean.BookBean;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    public Book addBook(BookBean bookBean){
        Book book = new Book();
        book.setTitle(bookBean.getTitle());
        book.setAuthor(bookBean.getAuthor());
        book.setQuantity(bookBean.getQuantity());
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }

    public Book getBookById(Long id){
        return bookRepository.findById(id).orElse(null);
    }

    public Book updateBook(Long id, BookBean bookBean){
        Book book = bookRepository.findById(id).orElse(null);
        if(book != null){
            book.setTitle(bookBean.getTitle());
            book.setAuthor(bookBean.getAuthor());
            book.setQuantity(bookBean.getQuantity());
            return bookRepository.save(book);
        }
        return null;
    }

    public void deleteBook(Long id){
        bookRepository.deleteById(id);
    }

}

