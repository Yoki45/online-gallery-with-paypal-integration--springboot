package com.example.BookGallery.controller;

import com.example.BookGallery.model.BookGallery;
import com.example.BookGallery.service.BookGalleryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {
    @Value("${uploadDir}")
    private String uploadFolder;

    @Autowired
    private BookGalleryService bookGalleryService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/")
    public String addProductPage() {
        return "index";
    }

    @GetMapping("/home")
    public String ProductPage() {
        return "index";

    }




    @PostMapping("/image/saveImageDetails")
    public
    @ResponseBody
    ResponseEntity<?> createProduct(@RequestParam("name") String name, @RequestParam("price")
            double price, @RequestParam("description") String description,
                                    Model model, HttpServletRequest request
            , final @RequestParam("image") MultipartFile file) {
        try {
            //String uploadDirectory = System.getProperty("user.dir") + uploadFolder;
            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            log.info("uploadDirectory:: " + uploadDirectory);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();
            log.info("FileName: " + file.getOriginalFilename());
            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid",
                        "Sorry! Filename contains invalid path sequence \" + fileName");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName,
                        HttpStatus.BAD_REQUEST);
            }
            String[] names = name.split(",");
            String[] descriptions = description.split(",");
            Date createDate = new Date();
            log.info("Name: " + names[0] + " " + filePath);
            log.info("description: " + descriptions[0]);
            log.info("price: " + price);
            try {
                File dir = new File(uploadDirectory);
                if (!dir.exists()) {
                    log.info("Folder Created");
                    dir.mkdirs();
                }
                // Save the file locally
                BufferedOutputStream stream = new BufferedOutputStream
                        (new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                log.info("in catch");
                e.printStackTrace();
            }
            byte[] imageData = file.getBytes();
            BookGallery bookGallery = new BookGallery();
            bookGallery.setName(names[0]);
            bookGallery.setImage(imageData);
            bookGallery.setPrice(price);
            bookGallery.setDescription(descriptions[0]);
            bookGallery.setCreateDate(createDate);
            bookGalleryService.saveBook(bookGallery);
            log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception: " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }


        @GetMapping("/book/display/{id}")
        @ResponseBody
        void showImage(@PathVariable("id") Long id,
                       HttpServletResponse response, Optional<BookGallery> bookGallery)
			throws ServletException, IOException {
            log.info("Id :: " + id);
            bookGallery = bookGalleryService.getImageById(id);
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(bookGallery.get().getImage());
            response.getOutputStream().close();
        }

        @RequestMapping("/book/bookDetails")
        String showProductDetails(@RequestParam(value= "id") Long id,
                                  Optional<BookGallery> bookGallery, Model model) {
            try {
                log.info("Id :: " + id);
                if (id != 0) {
                    bookGallery = bookGalleryService.getImageById(id);

                    log.info("products :: " + bookGallery);
                    if (bookGallery.isPresent()) {
                        model.addAttribute("id", bookGallery.get().getId());
                        model.addAttribute("description", bookGallery.get().getDescription());
                        model.addAttribute("name", bookGallery.get().getName());
                        model.addAttribute("price", bookGallery.get().getPrice());
                        return "imagedetails";
                    }
                    return "redirect:/home";
                }
                return "redirect:/home";
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/home";
            }
        }
    @GetMapping("/purchase")
    public String OrderPage( @RequestParam(value = "id")Long id,
                             Optional<BookGallery> bookGallery, Model model) {

        try {
            log.info("id:: " + id);
           if (id != 0) {
               bookGallery = bookGalleryService.getImageById(id);

                log.info("products :: " + bookGallery);
                if (bookGallery.isPresent()) {
                    model.addAttribute("id", bookGallery.get().getId());
                    //model.addAttribute("description", bookGallery.get().getDescription());
                    model.addAttribute("name", bookGallery.get().getName());
                    model.addAttribute("price", bookGallery.get().getPrice());

                    return "order";

                }

                return "redirect:/home";
            }
            return "redirect:/home";
        } catch (Exception e) {
            e.printStackTrace();

            return "redirect:/home";
        }
    }

        @GetMapping("/book/show")
        String show(Model map) {
            List<BookGallery> images = bookGalleryService.getAllActiveBook();
            map.addAttribute("images", images);
            return "images";
        }
    }




