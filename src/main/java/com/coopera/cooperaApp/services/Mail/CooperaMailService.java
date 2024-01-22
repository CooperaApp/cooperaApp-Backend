package com.coopera.cooperaApp.services.Mail;

import com.coopera.cooperaApp.dtos.requests.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service

public class CooperaMailService implements MailService{

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    public CooperaMailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendEmail(EmailDetails details)
    {
        try {
            SimpleMailMessage mailMessage
                    = new SimpleMailMessage();
            mailMessage.setFrom("cooperaapp010@gmail.com");
            mailMessage.setTo(details.getRecipient());
            mailMessage.setText(details.getMsgBody());
            mailMessage.setSubject(details.getSubject());
            javaMailSender.send(mailMessage);
            return "success";
        }
        catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public String mimeMessage(EmailDetails emailDetails) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(sender);
            helper.setTo(emailDetails.getRecipient());
            helper.setSubject(emailDetails.getSubject());
            helper.setText(emailDetails.getMsgBody(), true);
            javaMailSender.send(mimeMessage);
            return "success";
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
