package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;


public class ModifyDetailsController implements Initializable {
    @FXML private Button confirmBtn;
    @FXML private TextField value;
    @FXML private  TextField confirmValue;
    @FXML private RadioButton optionOne;
    @FXML private  RadioButton optionTwo;
    @FXML private RadioButton optionThree;
    private ToggleGroup group = new ToggleGroup();
    private static Customer activeCustomer;

    public static void setActiveCustomer(Customer activeCustomer) {
        ModifyDetailsController.activeCustomer = activeCustomer;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        optionOne.setToggleGroup(group);
        optionTwo.setToggleGroup(group);
        optionThree.setToggleGroup(group);      //initializing the variables on startup of the window
        optionThree.setUserData(3);
        optionTwo.setUserData(2);
        optionOne.setUserData(1);


    }
    @FXML private void updateProcess(){
        boolean status = false;
        int option;
        String message = "";
        String feed = value.getText();
        if(!value.getText().isEmpty()){//checking the text is empty
            if (feed.equals(confirmValue.getText())){
                if (group.getSelectedToggle() !=  null){
                    option = (int)group.selectedToggleProperty().getValue().getUserData();
                    if(option == 1){
                        boolean state[] = RegisterController.verification(feed);//password character verification method called
                        if (!state[0]){
                            message = message + "Your password should contain at least 8 Characters ";
                        }
                        if (state[1]){
                            message = message + "\nThe password should contain at least 2 non-alphabetical \ncharacters";
                        }
                        if(state[0] && !state[1]){
                            status =  activeCustomer.modifyAccountDetails(activeCustomer,feed,option);//updating the value in database
                        }
                    }else {
                        status = activeCustomer.modifyAccountDetails(activeCustomer,feed,option);
                    }
                }else {
                    message = message + "\nPlease select an option";
                }
            }else {
                message = message + "\nThe entered values are not matching";
            }
        }else {
            message = message + "\nPlease enter values";
        }


        if (status){//if updated the value successfully
            Alert success = new Alert(Alert.AlertType.INFORMATION);
            success.setHeaderText("Updated Successfully");
            success.setContentText("you request has been completed");
            success.showAndWait();

        }else {
            if (!message.equals("")) {//if the input data is not valid displaying relevant error message
                Alert emptyDetails = new Alert(Alert.AlertType.WARNING);
                emptyDetails.setTitle("Warning");
                emptyDetails.setHeaderText("Updating failed");
                emptyDetails.setContentText(message);
                emptyDetails.showAndWait();
            }else {
                Alert duplicationError = new Alert(Alert.AlertType.ERROR);
                duplicationError.setHeaderText("Update unsuccessful");
                duplicationError.setContentText("A user already exists for this Email");
                duplicationError.showAndWait();
            }

        }

    }
}
