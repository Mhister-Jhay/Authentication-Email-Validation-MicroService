package jhay.auth.common.event.registrationEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jhay.auth.domain.model.User;
import jhay.auth.common.security.token.TokenValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements
        ApplicationListener<RegistrationCompleteEvent> {
    private final TokenValidationServiceImpl tokenValidationService;
    private final JavaMailSender mailSender;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        // 1. Get the newly registered user
        User user = event.getUser();
        //2. Develop a token for the user
        String verifyToken = UUID.randomUUID().toString();
        // 3. Save the token in the database
        tokenValidationService.saveVerificationToken(verifyToken,user);
        // 4. Build the verification link for the user
        String url = event.getApplicationUrl()+"/register/verify?token="+verifyToken;
        // 5. Actually send the mail
        try {
            sendVerificationEmail(url,user);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendVerificationEmail(String url, User theUser) throws UnsupportedEncodingException, MessagingException {
        String subject = "Email Verification";
        String senderName = "Pagora Inc.";
        String mailContent = "<p> Hi, " + theUser.getFirstName() + ", </p>" +
                "<p>Thank you for registering with us, " + "" +
                "Please, follow the link below to complete your registration. </p>" +
                "<a href=\"" + url + "\"> Verify your email to activate your account</a>" +
                "<p> Thank you <br>" + senderName;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("registration@pagora.com",senderName);
        helper.setSubject(subject);
        helper.setTo(theUser.getEmail());
        helper.setText(mailContent,true);
        mailSender.send(mimeMessage);
    }
}
