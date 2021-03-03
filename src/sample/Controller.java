package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;


public class Controller {
    @FXML private Button registerBtn;
    @FXML private TextField email;
    @FXML private PasswordField password;

    @FXML private void openAdministratorLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("AdminLogin.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Admin Login");
        stage.setScene(scene);
        stage.show();
        registerBtn.getScene().getWindow().hide();//closing the previous window
    }
    @FXML  private void  openRegisterwindow (ActionEvent evt) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("Register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        Stage stage = new Stage();
        stage.setTitle("Registration");
        stage.setScene(scene);
        stage.show();
        registerBtn.getScene().getWindow().hide();

    }
    private void openShopingWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("ShopMainUI.fxml"));
        fxmlLoader.getController();
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("Jeff stores");
        stage.setScene(scene);
        stage.show();

    }
    @FXML private void loginProcess() throws IOException {
        String username = email.getText();
        String passcode = password.getText();
        Customer loginCustomer = new Customer(username,passcode);
        if (!loginCustomer.login(loginCustomer)){
            loginCustomer = null;//if the customer is not valid making the object garbage collectible
            Alert error = new Alert(Alert.AlertType.ERROR);//display error message if not valid
            error.setHeaderText("Login unsuccessful");
            error.setContentText("Username or Password Incorrect");
            error.showAndWait();

        }else {
            //System.out.println("logged in");
            openShopingWindow();
            registerBtn.getScene().getWindow().hide();
        }
    }
}
