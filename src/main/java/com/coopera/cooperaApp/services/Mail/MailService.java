package com.coopera.cooperaApp.services.Mail;

import com.coopera.cooperaApp.dtos.requests.EmailDetails;

public interface MailService {
    String sendEmail(EmailDetails emailDetails);

    String mimeMessage(EmailDetails emailDetails);

}
