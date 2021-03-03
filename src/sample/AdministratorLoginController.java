package sample;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class AdministratorLoginController {
    @FXML private TextField username;
    @FXML private PasswordField password;
    @FXML private CheckBox checkBox;



    @FXML private void loginProcess() throws IOException {

        String username = this.username.getText();
        String passCode = password.getText();
        if (checkBox.isSelected()) {  //if the check box is selected it means the owner is going to sign in
            Owner owner = new Owner(username, passCode); //owner object created
            if (!owner.login(owner)) {
                owner = null;//if the login unsuccessful releasing the object (making it garbage collectible)
                Alert error = new Alert(Alert.AlertType.ERROR);
                error.setHeaderText("Login unsuccessful");
                error.setContentText("Username or Password Incorrect");
                error.showAndWait();

            } else {
                AdminUIController.setOwner(owner);   //setting the owner variable
                System.out.println("test pass");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("AdminUI.fxml"));
                fxmlLoader.getController();
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Welcome");
                stage.setScene(scene);
                stage.show();
                checkBox.getScene().getWindow().hide();//closing the previous window


            }

        } else {
            StaffMember activeStaffMember = new StaffMember(username, passCode); //if the check box is not marked a staff member object will bw created
            if (!activeStaffMember.login(activeStaffMember)) {
                activeStaffMember = null; //if the login unsuccessful releasing the object (making it garbage collectible)
                Alert error = new Alert(Alert.AlertType.ERROR); //display error message
                error.setHeaderText("Login unsuccessful");
                error.setContentText("Username or Password Incorrect");
                error.showAndWait();

            } else {
                StaffUIController.setStaffMember(activeStaffMember);
                System.out.println("test pass");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("StaffUI.fxml"));//opening next window
                fxmlLoader.getController();
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = new Stage();
                stage.setTitle("Welcome");
                stage.setScene(scene);
                stage.show();
                checkBox.getScene().getWindow().hide();
            }

        }

    }

}
