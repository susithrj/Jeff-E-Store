package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class StaffRegistrationController {
    @FXML private TextField username;
    @FXML private TextField name;
    @FXML private TextField password;
    private static Owner owner;

    public static void setOwner(Owner owner) {
        StaffRegistrationController.owner = owner;
    }

    @FXML private void process(){
        StaffMember member = new StaffMember(username.getText(),password.getText());
        member.setName(name.getText());
        if(!owner.addStaffMember(member)){
            Alert duplicationError = new Alert(Alert.AlertType.ERROR);
            duplicationError.setHeaderText("Registration unsuccessful");
            duplicationError.setContentText("A user already exists for this Email");
            duplicationError.showAndWait();
        }else {
            Alert success = new Alert(Alert.AlertType.CONFIRMATION);
            success.setHeaderText("Registration Successful");
            success.setContentText(" Staff Member added");
            success.showAndWait();
        }

    }

}
