package sample;

import Implementations.SearchOrderList;
import Implementations.SortOrderList;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AdminUIController implements Initializable {
    @FXML private TextField inputId;
    @FXML private DatePicker datePicker;
    @FXML private TableView table;
    @FXML private TableColumn o_id;
    @FXML private TableColumn o_cost;
    @FXML private TableColumn o_date;
    @FXML private TableColumn c_name;
    @FXML private TextField customerEmail;
    @FXML private TableView itemDescription;
    @FXML private TableColumn itemId;
    @FXML private TableColumn i_quantity;
    @FXML private TableColumn i_cost;
    @FXML private TableColumn  i_size;
    @FXML private Label orderId;

    private static ArrayList<Order> ordersList = new ArrayList<>();
    private static Owner owner;

    public static void setOwner(Owner owner) {
        AdminUIController.owner = owner;
    }
    @FXML private void addStaffMemberProcess() throws IOException {
        StaffRegistrationController.setOwner(owner);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("StaffRegisteration.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 400);
        Stage stage = new Stage();
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }

    @FXML private void searchByDate(){
        ordersList.clear();//clearing the list before search to add items again
        String date = datePicker.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));//getting value from date picker
        owner.viewLog(date,ordersList);
        displayData();

    }
    private void displayData(){
        ObservableList<Order> dataList = FXCollections.observableArrayList();
        for(int i = 0;i < ordersList.size();i++){
            dataList.add(ordersList.get(i)) ;
        }
        table.setItems(dataList);
        o_id.setCellValueFactory(new PropertyValueFactory<Order,Integer>("orderId"));
        o_date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order,String> param) {//getting data from a inner object from a object in the observation list
                return new SimpleStringProperty( new SimpleDateFormat("dd/MM/yyyy").format(param.getValue().getOrderDate()));
            }
        });
        o_cost.setCellValueFactory((new PropertyValueFactory<Order,Double>("totalAmount")));
        c_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order,String> param) {
                return new SimpleStringProperty(param.getValue().getCustomer().getEmail());
            }
        });
    }
    private void displayData(int index){
        ObservableList<Order> dataList = FXCollections.observableArrayList();
        dataList.add(ordersList.get(index)) ;
        table.setItems(dataList);
        o_id.setCellValueFactory(new PropertyValueFactory<Order,Integer>("orderId"));
        o_date.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order,String> param) {
                return new SimpleStringProperty( new SimpleDateFormat("dd/MM/yyyy").format(param.getValue().getOrderDate()));
            }
        });
        o_cost.setCellValueFactory((new PropertyValueFactory<Order,Double>("totalAmount")));
        c_name.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Order,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Order,String> param) {
                return new SimpleStringProperty(param.getValue().getCustomer().getEmail());
            }
        });
    }
    @FXML private void customerSearchProcess(){
        ordersList.clear();
        String email = customerEmail.getText();
        owner.getCustomerReport(email,ordersList);
        displayData();

    }
    @FXML private void getAllOrders(){
        ordersList.clear();
        ResultSet result = DatabaseConnecter.getData("order_","*");
        try {
            if (result !=null) {//if the data from database is not empty adding data for the order list
                for (int i = 0; result.next(); i++) {
                    try {
                        ordersList.add(i, new Order(result.getInt(1),new SimpleDateFormat("dd/MM/yyyy").parse(result.getString(2)),
                                new Customer(result.getString(4)), result.getDouble(3)));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        displayData();
    }
    @FXML private void searchByOrder(){
        try {
            int id = Integer.parseInt(inputId.getText());
            displayData(owner.checkOrder(ordersList,id));
            orderId.setText(String.valueOf(id));
            displayOrderDescription(id);

        }catch (NumberFormatException e){//if the user entered a non - integer value handling the exception
            Alert error = new Alert(Alert.AlertType.ERROR);
            error.setHeaderText("Error");
            error.setContentText("Please enter a number");
            error.showAndWait();

        }

    }
    @FXML private void logoutProcess() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));//opening home screen after logout
        Scene scene = new Scene(fxmlLoader.load(), 650, 400);
        Stage stage = new Stage();
        stage.setTitle("Welcome To Jeff's Store");
        stage.setScene(scene);
        stage.show();
        ordersList = null; //making the object garbage collectible
        inputId.getScene().getWindow().hide();
    }
     private void displayOrderDescription(int id){
        ObservableList<OrderItem> descriptionList = FXCollections.observableArrayList();
        String sqlCode = "SELECT * FROM orderitem WHERE o_id = ?";//sql code to execute

        ResultSet result = null;
        try {
            Connection conn = DatabaseConnecter.connectToDatabase();
            PreparedStatement stm = conn.prepareStatement(sqlCode);
            stm.setInt(1, id);
            result = stm.executeQuery();
            if (result != null){//if result is not empty adding data yo list
                while (result.next()){
                    descriptionList.add(new OrderItem(new Item(result.getString("i_code")),
                            result.getInt("i_quantity"),result.getString("i_size"),result.getDouble("cost")));
                }
                itemDescription.setItems(descriptionList);
                i_cost.setCellValueFactory(new PropertyValueFactory<OrderItem,Double>("price"));//defining the column
                i_quantity.setCellValueFactory(new PropertyValueFactory<OrderItem,String>("quantity"));
                i_size.setCellValueFactory(new PropertyValueFactory<OrderItem,String>("size"));
                itemId.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<OrderItem,String>, ObservableValue<String>>() {
                    @Override
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<OrderItem,String> param) {
                        return new SimpleStringProperty(param.getValue().getItem().getItemCode()); //getting the itemcode  from the object as string
                    }
                });
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        table.setPlaceholder(new Label("No Orders found"));//Label of the table if table is empty
        getAllOrders();//display all orders in table

            }

        }



