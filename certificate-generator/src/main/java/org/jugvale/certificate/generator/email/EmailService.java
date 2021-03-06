package org.jugvale.certificate.generator.email;

import java.sql.Date;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jugvale.certificate.generator.model.Certificate;
import org.jugvale.certificate.generator.model.CertificateContent;
import org.jugvale.certificate.generator.model.EmailInfo;

@ApplicationScoped
public class EmailService {

    public static final String ATTENDEE_NAME_PLACEHOLDER = "attendee.name";
    public static final String CONFERENCE_NAME_PLACEHOLDER = "conference.name";
    public static final String CERTIFICATE_KEY_PLACEHOLDER = "certificate.key";

    Logger logger = Logger.getLogger(EmailService.class);

    @ConfigProperty(name = "certificate.email.subject")
    String emailSubject;

    @ConfigProperty(name = "certificate.email.body")
    String emailBody;

    @Inject
    ReactiveMailer reactiveMailer;

    public void send(CertificateContent content) {
        sendContent(content, emailSubject, emailBody);
    }

    protected void sendContent(CertificateContent content, String initialSubject, String initialBody) {
        Certificate certificate = content.certificate;
        String email = content.certificate.registration.attendee.email;

        final String subject = makeReplacements(initialSubject, certificate);
        final String body = makeReplacements(initialBody, certificate);

        logger.infov("Sending email to {0}", email);
        logger.debugv("Email content: \n Subject: {0}\nBody: {1}", subject, body);

        reactiveMailer.send(Mail.withText(email, subject, body)
                                .addAttachment("certificate.pdf",
                                               content.contentBin,
                                               "application/pdf"))
                      .subscribeAsCompletionStage()
                      .thenApply(e -> {
                          storeEmail(content, subject, body);
                          return null;
                      });
    }

    @Transactional
    public void storeEmail(CertificateContent content, String subject, String body) {
        EmailInfo emailInfo = new EmailInfo();
        emailInfo.certificateId = content.certificate.id;
        emailInfo.sentDate = new Date(System.currentTimeMillis());
        emailInfo.body = body;
        emailInfo.subject = subject;
        EmailInfo.persist(emailInfo);
    }

    private String makeReplacements(String text, Certificate certificate) {
        return text.replaceAll(ATTENDEE_NAME_PLACEHOLDER, certificate.registration.attendee.name)
                   .replaceAll(CONFERENCE_NAME_PLACEHOLDER, certificate.registration.conference.name)
                   .replaceAll(CERTIFICATE_KEY_PLACEHOLDER, certificate.generationKey);
    }

}
