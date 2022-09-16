package com.example.BookGallery.repository;

import com.example.BookGallery.model.BookGallery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookGalleryRepository extends JpaRepository<BookGallery,Long> {
}
