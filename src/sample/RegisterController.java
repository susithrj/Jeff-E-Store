package sample;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class RegisterController {
    @FXML
    private TextField email;
    @FXML
    private TextField name;
    @FXML
    private TextField address;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmedPassword;


    @FXML
    private void registrationProcess() throws SQLException, IOException {
        String message = "";
        String enteredPassword = password.getText();
        if (email.getText().isEmpty() || name.getText().isEmpty() || address.getText().isEmpty()) {
            message = "Email,Name,Address can not be empty";//if fields are empty error message
        }else if (enteredPassword.equals(confirmedPassword.getText())) {
            boolean state[] = verification(enteredPassword);//verifying password is strong enough
            if (state[0]) {
                if (state[1]) {
                    message = message + "\nThe password should contain at least 2 non-alphabetical \ncharacters";

                } else {
                    Customer newCustomer = new Customer(name.getText(), email.getText(), password.getText(), address.getText());
                    boolean status = newCustomer.getRegistered(newCustomer);//registering customer in data base
                    if (status) {
                        Alert success = new Alert(Alert.AlertType.CONFIRMATION);
                        success.setHeaderText("Registration Successful");
                        success.setContentText(" Do you want to Login?");
                        Optional<ButtonType> result = success.showAndWait();
                        if (result.get() == ButtonType.OK) {
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
                            Scene scene = new Scene(fxmlLoader.load(), 650, 400);
                            Stage stage = new Stage();
                            stage.setTitle("Registration");
                            stage.setScene(scene);
                            stage.show();
                            email.getScene().getWindow().hide();
                        } else {
                            Platform.exit();
                        }

                    } else {
                        Alert duplicationError = new Alert(Alert.AlertType.ERROR);
                        duplicationError.setHeaderText("Registration unsuccessful");
                        duplicationError.setContentText("A user already exists for this Email");
                        duplicationError.showAndWait();
                    }
                }
            } else {
                message = message + "\nThe password should contain at least 8 characters";
            }

        }else {
                    message = message + "\nThe passwords you enter are mismatching";

                }

        if (!message.equals("")) {
            Alert emptyDetails = new Alert(Alert.AlertType.WARNING);
            emptyDetails.setTitle("Warning");
            emptyDetails.setHeaderText("Registration failed");
            emptyDetails.setContentText(message);
            emptyDetails.showAndWait();
        }
    }

    public static boolean[] verification(String enteredPassword) {
        int num = 0;
        boolean status[] = {false, false};
        if (enteredPassword.length() >= 8) {//if password length is bellow 8 password is not valid
            status[0] = true;
        }
        for (int i = 0; i < enteredPassword.length() && num < 2; i++) {
            if (!Character.isAlphabetic(enteredPassword.charAt(i))) {//if there is no at least two non - alphabetic letters password not valid
                num++;
            }
        }
        if (num < 2) {
            status[1] = true;
        }
        return status;
    }
}
