package com.relatos_papel.catalogue.service;

import com.relatos_papel.catalogue.exception.ResourceNotFoundException;
import com.relatos_papel.catalogue.repository.BookJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class DeleteBooksService {

    private final BookJpaRepository bookJpaRepository;

    @Transactional
    public void deleteBook(Integer bookId) {
        if (!bookJpaRepository.existsById(bookId)) {
            throw new ResourceNotFoundException("Libro no encontrado: " + bookId);
        }
        bookJpaRepository.deleteById(bookId);
    }
}
