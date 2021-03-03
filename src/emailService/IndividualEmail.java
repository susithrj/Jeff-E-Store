package emailService;


import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class IndividualEmail extends Email {
    private String recipient;

    public IndividualEmail(String title, String messageBody, String recipient) {
        super(title, messageBody);
        this.recipient = recipient;
    }
    public IndividualEmail(String title, String messageBody) {
        super(title, messageBody);
        this.recipient = "jeffstoresiit@gmail.com";
    }



    @Override
    public boolean sendEmail() {
        MimeMessage email = this.getConnection();
        try {
            email.setFrom(new InternetAddress(SENDER));
            email.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            email.setSubject(this.getTitle());
            email.setText(this.getMessageBody());
            Transport transport = this.getSession().getTransport("smtp");
            transport.connect(HOST, SENDER, PASSWORD);
            transport.sendMessage(email, email.getAllRecipients());
            transport.close();
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

    }
}
