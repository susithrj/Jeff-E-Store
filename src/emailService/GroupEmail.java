package emailService;

import Implementations.LinkedList;
import sample.DatabaseConnecter;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GroupEmail extends Email {
  private InternetAddress recipients[];
  private LinkedList groupRecipients = new LinkedList();

    public GroupEmail(String messageBody) {
        super("News Letter - Jeff's", messageBody);

    }

    @Override
    public boolean sendEmail() {
        MimeMessage email = getConnection();
        try {
            email.setFrom(new InternetAddress(SENDER));
            getRecipients();
            for (int i = 0 ;i < recipients.length;i++){//adding recipients to email
                email.addRecipient(MimeMessage.RecipientType.TO,recipients[i]);
            }
            email.setSubject(this.getTitle());
            email.setText(this.getMessageBody());
            Transport transport = this.getSession().getTransport("smtp");
            transport.connect(HOST, SENDER, PASSWORD);
            transport.sendMessage(email, email.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return false;
    }
    private void getRecipients(){

        ResultSet result = DatabaseConnecter.getData("customer","c_email");
        if(result != null){
            try{
                while (result.next()) {
                    groupRecipients.addElement(result.getString(1));//getting data through linked list(unknown number of customers)
                }
                recipients = new InternetAddress[groupRecipients.getNum()];
                for (int i = 0;groupRecipients.hasNext();i++){
                    recipients[i] = new InternetAddress(groupRecipients.getData());//creating internet addresses
                    groupRecipients.iterrate();
                }

            }catch (SQLException e){
                System.out.println("Database Error");
            } catch (AddressException e) {
                e.printStackTrace();
            }

        }
    }
    public static String getMessage() throws SQLException {
        String message = String.format("%-20s %-12s  \n","Item name","Price starting at") + "\n";
        ResultSet resultSet = DatabaseConnecter.getData("item","*");
        if(resultSet != null){//getting  data from data base and generating message body
            while (resultSet.next()){
                message = message + String.format("%-20s %-12s  \n",resultSet.getString(2),resultSet.getDouble(3));

            }
        }
        return message;
    }
}
