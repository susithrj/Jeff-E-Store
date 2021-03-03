package sample;


import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;

public class EmailInquiryController {
    @FXML private TextArea text;
    private static Customer activeCustomer;

    public static void setActiveCustomer(Customer activeCustomer) {
        EmailInquiryController.activeCustomer = activeCustomer;
    }

    @FXML private void sendInquiry(){
       String body =  text.getText();//get the text from text area
       activeCustomer.emailInquiry(activeCustomer,body);//calling the send inquiry method in customer
        Alert information = new Alert(Alert.AlertType.INFORMATION);
        information.setHeaderText("Successful");
        information.setContentText("Your response have been recorded");
        information.showAndWait();
    }
}
