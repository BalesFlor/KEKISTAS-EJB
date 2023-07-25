package com.correo;

import java.util.Properties;
import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;

public class EnvioDeCorreo {

    public void transfer_to_email(String correoDestino, String msj, String asunto) {
        String correoEnvia = "secretariautec3@gmail.com";
        String contrasenia = "pfjoubginlmylnps";

        Properties objPEC = new Properties();

        objPEC.put("mail.smtp.host", "smtp.gmail.com");
        objPEC.setProperty("mail.smtp.starttls.enable", "true");
        objPEC.put("mail.smtp.port", "587");
        objPEC.setProperty("mail.smtp.port", "587");
        objPEC.put("mail.smtp.user", correoEnvia);
        objPEC.setProperty("mail.smtp.auth", "true");

        Session sesion = Session.getDefaultInstance(objPEC);
        MimeMessage mail = new MimeMessage(sesion);

        try {
            mail.setFrom(new InternetAddress(correoEnvia));
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(correoDestino));
            mail.setSubject(asunto);
            mail.setText(msj);

            Transport transporte = sesion.getTransport("smtp");
            transporte.connect(correoEnvia, contrasenia);
            transporte.sendMessage(mail, mail.getRecipients(Message.RecipientType.TO));
            transporte.close();

            JOptionPane.showMessageDialog(null, "El correo se ha enviado exitosamente");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error en el envío del correo...\n" + ex);
        }
    }
}
