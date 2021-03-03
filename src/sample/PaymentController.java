package sample;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class PaymentController implements Initializable{
    @FXML private Label amount;
    @FXML private TextField textOne;
    @FXML private TextField textTwo;
    @FXML private TextField textThree;
    @FXML private TextField textFour;
    private static  Customer paymentCustomer;

    public static void setPaymentCustomer(Customer paymentCustomer) {
        PaymentController.paymentCustomer = paymentCustomer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    amount.setText(String.valueOf(ShopGuiController.getTotalPrice()));
    }

    @FXML private void paymentProcess(){
        if(!(textFour.getText().isEmpty() || textOne.getText().isEmpty() || textThree.getText().isEmpty() || textTwo.getText().isEmpty())){
            paymentCustomer.makePayment(paymentCustomer);//if the form is filled make payment
        }else {
            Alert emptyDetails = new Alert(Alert.AlertType.WARNING); //else display error message
            emptyDetails.setTitle("Warning");
            emptyDetails.setHeaderText("payment  failed");
            emptyDetails.setContentText("please fill the form correctly");
            emptyDetails.showAndWait();
        }

    }
}

