package jhay.auth.common.event;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jhay.auth.domain.service.token.TokenValidationServiceImpl;
import jhay.auth.domain.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ForgotPasswordEventListener implements
        ApplicationListener<ForgotPasswordEvent> {
    private final TokenValidationServiceImpl tokenValidationService;
    private final JavaMailSender mailSender;
    @Override
    public void onApplicationEvent(ForgotPasswordEvent event) {
        // 1. Get the newly registered user
        User user = event.getUser();
        //2. Develop a token for the user
        String passwordToken = UUID.randomUUID().toString();
        // 3. Save the token in the database
        tokenValidationService.saveVerificationToken(passwordToken,user);
        // 4. Build the verification link for the user
        String url = event.getApplicationUrl()+"/verify-reset-password?token="+passwordToken;
        // 5. Actually send the mail
        try {
            sendVerificationEmail(url,user);
        } catch (UnsupportedEncodingException | MessagingException e) {
            throw new RuntimeException(e);
        }
    }
    public void sendVerificationEmail(String url, User theUser) throws UnsupportedEncodingException, MessagingException {
        String subject = "Reset Password";
        String senderName = "Pagora Inc.";
        String mailContent = "<p> Hi, " + theUser.getFirstName() + ", </p>" +
                "<p>Please, follow the link below to reset your password. </p>" +
                "<a href=\"" + url + "\"> Reset Password</a>" +
                "<p> Thank you <br>" + senderName;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom("auth@pagora.com",senderName);
        helper.setSubject(subject);
        helper.setTo(theUser.getEmail());
        helper.setText(mailContent,true);
        mailSender.send(mimeMessage);
    }
}
