package com.example.BookGallery.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Controller
public class MailController {

    @Autowired
    private JavaMailSender mailSender;


    @RequestMapping("/shipping")
    public String sendMail() {
        return "mail";
    }

    @PostMapping("/shipping")

    public String SendMail(HttpServletRequest request
                          /**@RequestParam("attachment") MultipartFile multipartFile **/
    ) throws MessagingException {

        String email = request.getParameter("email");
        String phone = request.getParameter("phone");

        String productname = request.getParameter("productname");
        //String quantity = request.getParameter("subject");
        String total= request.getParameter("body");
        String delivery= request.getParameter("delivery");


        //SimpleMailMessage message = new SimpleMailMessage();


        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);


        helper.setFrom("buzzzinga101@gmail.com");
        helper.setTo("okiyostephen@gmail.com");


        String mailSubject = email + " " + " would like to purchase";
        String mailBody = "<p><b>Product Name:</b>" + productname + "</p>";
        mailBody += "<p><b>Sender phone </b>" + phone + "</p>";
        //mailBody += "<p><b>Quantity </b>" + "" + quantity+ "</p>";
        mailBody += " <p><b>Body </b>" + "" + total + "</p>";
        mailBody += " <p><b>General Delivery Area </b>"  + "" + delivery + "</p>";


        helper.setSubject(mailSubject);
        helper.setText(mailBody, true);

       /** if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

            InputStreamSource source = new InputStreamSource() {

                @Override
                public InputStream getInputStream() throws IOException {
                    return multipartFile.getInputStream();
                }
            };


            helper.addAttachment(fileName, source);


        }
*/

        mailSender.send(message);


        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "587");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");


        return "message";


    }
}