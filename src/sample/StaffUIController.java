package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class StaffUIController implements Initializable{
 @FXML private TableView tableView;
 @FXML private TableColumn itemId;
 @FXML private TableColumn i_quantity;
 @FXML private TableColumn i_cost;
 @FXML private TableColumn  i_size;
 @FXML private Label payment;
 @FXML private Label id;
 @FXML private Label date;
 @FXML private Label customer;
 @FXML private TextField input;
 @FXML private Label employeeName;
 private static StaffMember staffMember;

    public static void setStaffMember(StaffMember staffMember) {
        StaffUIController.staffMember = staffMember;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
         tableView.setPlaceholder(new Label("select a order display"));
         employeeName.setText(staffMember.getName());

    }
    private void displayOrderDescription(int id){
        ObservableList<OrderItem> descriptionList = FXCollections.observableArrayList();
        String sqlCode = "SELECT * FROM orderitem WHERE o_id = ?";

        ResultSet result = null;
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode);
            stm.setInt(1, id);
            result = stm.executeQuery();
            if (result != null){
                while (result.next()){
                    descriptionList.add(new OrderItem(new Item(result.getString("i_code")),
                            result.getInt("i_quantity"),result.getString("i_size"),result.getDouble("cost")));
                }
                tableView.setItems(descriptionList);
                i_cost.setCellValueFactory(new PropertyValueFactory<OrderItem,Double>("price"));
                i_quantity.setCellValueFactory(new PropertyValueFactory<OrderItem,String>("quantity"));
                i_size.setCellValueFactory(new PropertyValueFactory<OrderItem,String>("size"));
                itemId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderItem,String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem,String> param) {//getting data from a inner object from a object in the observation list
                        return new SimpleStringProperty(param.getValue().getItem().getItemCode());
                    }
                });
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


    }
    @FXML private void searchById(){
        try{
            int value = Integer.parseInt(input.getText());
            String[] data =  staffMember.checkOrder(value);
            if(data != null){
                id.setText(data[0]);
                payment.setText(data[3]);
                date.setText(data[1]);
                customer.setText(data[2]);
                displayOrderDescription(value);
            }else {
                Alert error = new Alert(Alert.AlertType.INFORMATION);
                error.setHeaderText("Process failed");
                error.setContentText("Order not found");
                error.showAndWait();
            }

        }catch (NumberFormatException e){//if id is not entered as a integer display error message
            Alert error = new Alert(Alert.AlertType.INFORMATION);
            error.setHeaderText("Process failed");
            error.setContentText("Please enter a number");
            error.showAndWait();
        }


    }
    @FXML private void logoutProcess() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 400);
        Stage stage = new Stage();
        stage.setTitle("Welcome To Jeff's Store");
        stage.setScene(scene);
        stage.show();
        id.getScene().getWindow().hide();

    }
}
