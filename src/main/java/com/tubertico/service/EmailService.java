package com.tubertico.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String senderEmail;

    @Value("${app.base-url}")
    private String baseUrl;

    public void enviarCorreoRecuperacion(String correoDestino, String nombre, String token)
            throws MessagingException, UnsupportedEncodingException {

        String asunto = "Recuperación de contraseña - Tubertico";
        String enlaceRecuperacion = baseUrl + "/reset-password?token=" + token;

        String contenido = "<p>Hola <strong>" + nombre + "</strong>,</p>"
                + "<p>Has solicitado restablecer tu contraseña. Haz clic en el siguiente enlace:</p>"
                + "<p><a href=\"" + enlaceRecuperacion + "\">Restablecer contraseña</a></p>"
                + "<br><p>Si no solicitaste esto, podés ignorar este mensaje.</p>"
                + "<p>Saludos,<br><strong>Equipo de Soporte Tubertico</strong></p>";

        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
        helper.setTo(correoDestino);
        helper.setSubject(asunto);
        helper.setText(contenido, true);
        helper.setFrom(new InternetAddress(senderEmail, "Soporte Tubertico"));

        mailSender.send(mensaje);
    }
}
