package emailService;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public abstract class Email {
    protected static final String HOST = "smtp.gmail.com";
    protected static final String SENDER = "jeffstoresiit";
    protected static final String PASSWORD = "Gugsi4321";
    private Session session;
    private String title;
    private String messageBody;

    public Session getSession() {
        return session;
    }

    public String getTitle() {
        return title;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public Email(String title, String messageBody) {
        this.title = title;
        this.messageBody = messageBody;
    }

    protected MimeMessage getConnection() { //getting connection through smtp server
        Properties emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.host", HOST);
        emailProperties.put("mail.smtp.user", SENDER);
        emailProperties.put("mail.smtp.password", PASSWORD);
        emailProperties.put("mail.smtp.port", "587");
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        session = Session.getDefaultInstance(emailProperties);
        return new MimeMessage(session);
    }

    public   abstract boolean sendEmail();
}






