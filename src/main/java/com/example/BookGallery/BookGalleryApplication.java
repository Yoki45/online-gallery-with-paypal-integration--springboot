package com.example.BookGallery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class BookGalleryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookGalleryApplication.class, args);
	}

}
