package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import scheduler.Scheduler;
import scheduler.model.AlertDialog;
import scheduler.model.Customer;

public class CustomerController {
  
  private Stage stage;
  private Connection connection;
  private String userName = "";
  private final ObservableList<Customer> customers = FXCollections.observableArrayList();

  @FXML
  private TableView<Customer> tableView;

  @FXML
  private TableColumn<Customer, String> nameTableColumn;

  @FXML
  private TableColumn<Customer, String> addressTableColumn;

  @FXML
  private TableColumn<Customer, String> address2TableColumn;

  @FXML
  private TableColumn<Customer, String> cityTableColumn;

  @FXML
  private TableColumn<Customer, String> postalCodeTableColumn;

  @FXML
  private TableColumn<Customer, String> phoneTableColumn;

  @FXML
  private TableColumn<Customer, String> countryTableColumn;



  private void deleteCustomer(Customer customer){
     PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement("DELETE FROM customer WHERE customerid =?");
      preparedStatement.setString(1, Integer.toString(customer.getID()));
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  @FXML
  private void handleAddButton() throws IOException, ClassNotFoundException{
    ModifyCustomerController.showDialog(stage, connection, null, "Add Customer", userName);
    updateCustomers();
  }

  private void handleButton(boolean delete) throws ClassNotFoundException, IOException{
    Customer customer = tableView.getSelectionModel().getSelectedItem(); 
    if (customer != null) {
      if(delete){
        deleteCustomer(customer);
      }else{
        ModifyCustomerController.showDialog(stage, connection, customer, "Modify Customer", userName);
      }
      updateCustomers();
    } else {
      AlertDialog.noSelectionDialog("customer");
    }
  }
  
  @FXML
  private void handleDeleteButton() throws ClassNotFoundException, IOException {
    handleButton(true);
  }
  
  @FXML
  private void handleModifyButton() throws IOException, ClassNotFoundException{
    handleButton(false);
  }
  
  @FXML
  private void initialize() throws IOException, ClassNotFoundException{
      
    //Lambda expressions implemented to map customer properties to table cells
    
    nameTableColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    addressTableColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
    address2TableColumn.setCellValueFactory(cellData -> cellData.getValue().address2Property());
    cityTableColumn.setCellValueFactory(cellData -> cellData.getValue().cityProperty());
    postalCodeTableColumn.setCellValueFactory(cellData -> cellData.getValue().postalCodeProperty());
    phoneTableColumn.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
    countryTableColumn.setCellValueFactory(cellData -> cellData.getValue().countryProperty());
  }
  
  private void updateCustomers() throws ClassNotFoundException{
    ResultSet resultSet = getCustomersFromDataBase();
    customers.clear();
    try {
      while (resultSet.next()) {
        int customerID = resultSet.getInt("customerid");
        String name = resultSet.getString("customerName");
        int addressID = resultSet.getInt("addressId");
        String address = resultSet.getString("address");
        String address2 = resultSet.getString("address2");
        String city = resultSet.getString("city");
        String postalCode = resultSet.getString("postalCode");
        String phone = resultSet.getString("phone");
        String country = resultSet.getString("country");
        Customer customer = new Customer(customerID, name, addressID, address, address2, city, postalCode, phone, country);
        customers.add(customer);
      }
      tableView.setItems(customers);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public ResultSet getCustomersFromDataBase() throws ClassNotFoundException {
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(
      "SELECT customerid, customerName, customer.addressId, address, address2, postalCode, phone, city, country " +
      "FROM customer, address, city, country " +
      "WHERE customer.addressId = address.addressid " +
      "AND address.cityId = city.cityid " +
      "AND city.countryId = country.countryid");
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return resultSet;
  }

  public void setConnection(Connection connection) {
    this.connection = connection;
  }

  public void setStage(Stage stage) {
    this.stage = stage;
  }
  
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  public static void showDialog(Stage primaryStage, Connection connection, String userName) throws IOException, ClassNotFoundException{
    
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Customer.fxml"));
    AnchorPane page = (AnchorPane) loader.load();

    Stage stage = new Stage();
    stage.setTitle("Customer");
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.initOwner(primaryStage);
    Scene scene = new Scene(page);
    stage.setScene(scene);
    
    CustomerController customerController = loader.getController();
    customerController.setStage(stage);
    customerController.setConnection(connection);
    customerController.setUserName(userName);
    customerController.updateCustomers();
    
    stage.showAndWait();
  }
  
}
