package com.example.BookGallery.service;


import com.example.BookGallery.model.BookGallery;
import com.example.BookGallery.repository.BookGalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookGalleryService {

    @Autowired
    private BookGalleryRepository bookGalleryRepository;

    public void saveBook(BookGallery bookGallery) {
        bookGalleryRepository.save(bookGallery);
    }

    public List<BookGallery> getAllActiveBook() {
        return bookGalleryRepository.findAll();
    }

    public Optional<BookGallery> getImageById(Long id) {
        return bookGalleryRepository.findById(id);
    }

}
