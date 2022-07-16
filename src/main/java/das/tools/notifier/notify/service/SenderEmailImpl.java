package das.tools.notifier.notify.service;

import das.tools.notifier.notify.entitys.request.Request;
import das.tools.notifier.notify.entitys.response.Response;
import das.tools.notifier.notify.entitys.response.ResponseStatus;
import das.tools.notifier.notify.exceptions.WrongRequestParameterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Component("email")
@Slf4j
public class SenderEmailImpl implements Sender {
    Pattern EMAIL_PATTERN = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9]))\\.){3}(?:(2(5[0-5]|[0-4][0-9])|1[0-9][0-9]|[1-9]?[0-9])|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");

    private final JavaMailSender emailSender;

    @Value("${spring.mail.password}")
    private String password;
    @Value("${eml.from.addr}")
    private String from;
    @Value("${eml.to.addr}")
    private String to;

    public SenderEmailImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @PostConstruct
    private void getValidToAddress() {
        to = getValidEmailAddressesList(to);
    }

    @Override
    public Response send(Request request) throws WrongRequestParameterException {
        Response res;
        String message = request.getMessage() ;//!= null ? request.getMessage() : "" ;
        if (log.isDebugEnabled()) log.debug("Try to send message {}", Utils.linearizedString(message));
        String chatId = request.getChatId() != null ? request.getChatId() : "";
        chatId = getValidEmailAddressesList(chatId);
        String addressTo = !"".equals(chatId) && isValidEmail(chatId) ? chatId : to;
        String subject = Utils.getNotNullStringOrDefault(request.getSubject(), "Message");
        if ("".equals(addressTo)) {
            throw new WrongRequestParameterException("SendTo e-mail address is empty");
        }
        String file = request.getFile();
        if (!"".equals(file)) {
            if (log.isDebugEnabled()) log.debug("Sending file '{}' in message", file);
            MimeMessagePreparator preparator = getPreparator(addressTo, message, file, subject);
            try {
                emailSender.send(preparator);
            } catch (MailException e) {
                log.error("Error sending inline E-Mail", e);
            }
        } else {
            if (log.isDebugEnabled()) log.debug("Sending plane text message");
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(addressTo);
            email.setFrom(from);
            email.setSubject(subject);
            email.setText(message);
            try {
                emailSender.send(email);
            } catch (MailException e) {
                log.error("Error sending plain E-Mail: {}", e.getMessage(), e);
            }
        }
        StringBuilder comment = new StringBuilder();
        comment.append(String.format("E-Mail was sent to '%s'", addressTo));
        if (!"".equals(file)) {
            comment.append(String.format(" with file '%s'", file));
        }
        res = Response.builder()
                .status(ResponseStatus.OK)
                .comment(comment.toString())
                .build();
        if (log.isDebugEnabled()) log.debug("resp={}", Utils.linearizedString(res));
        return res;
    }

    private MimeMessagePreparator getPreparator(String to, String message, String file, String subject) {
        return new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                mimeMessage.setRecipients(Message.RecipientType.TO, to);
                mimeMessage.setFrom(from);
                mimeMessage.setSubject(subject);
                MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                File f = new File(file);
                FileSystemResource res = new FileSystemResource(f);
                if (isImageFile(file)) {
                    if (log.isDebugEnabled()) log.debug("Sending image '{}' inline", file);
                    helper.setText(String.format("<html><body>%s<br/><img src='cid:identifier1234'></body></html>", message), true);
                    helper.addInline(f.getName(), res);
                } else {
                    if (log.isDebugEnabled()) log.debug("Sending file '{}' as attachment", file);
                    mimeMessage.setText(message);
                    helper.addAttachment(f.getName(), res);
                }
            }
        };
    }

    /**
     * Checks if addresses are correct
     * @param addresses semicolon-separated list of e-mails
     * @return semicolon-separated list of correct e-mails
     */
    private String getValidEmailAddressesList(String addresses) {
        StringBuilder res = new StringBuilder();
        List<String> lst = Arrays.asList(addresses.split(";"));
        System.out.println(lst);
        for (String address : lst) {
            if(isValidEmail(address)) {
                res.append(address).append(";");
            }
        }
        return res.toString();
    }

    private boolean isValidEmail(String emailAddress) {
        boolean matches = EMAIL_PATTERN.matcher(emailAddress).matches();
        if (log.isDebugEnabled()) log.debug("Checking validity of e-mail '{}' is '{}'", emailAddress, matches);
        return matches;
    }

    private boolean isImageFile(String fileName) {
        String mimeType = Utils.getMimeType(fileName);
        return mimeType.startsWith("image");
    }
}
