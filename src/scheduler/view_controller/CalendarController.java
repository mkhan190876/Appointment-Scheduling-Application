package scheduler.view_controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import scheduler.model.AlertDialog;
import scheduler.model.Appointment;
import scheduler.model.DateTime;

public class CalendarController {
  private Stage stage;
  private Connection connection;
  private final ObservableList<Appointment> calendar = FXCollections.observableArrayList();
  private String today;
  private String tomorrow;
  private final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private String reminder = "";
  private String userName = "";
  
  @FXML
  private ComboBox<String> viewComboBox;

  @FXML
  private TableView<Appointment> calendarTableView;

  @FXML
  private TableColumn<Appointment, String> startTableColumn;

  @FXML
  private TableColumn<Appointment, String> endTableColumn;

  @FXML
  private TableColumn<Appointment, String> titleTableColumn;

  @FXML
  private TableColumn<Appointment, String> typeTableColumn;

  @FXML
  private TableColumn<Appointment, String> customerTableColumn;

  @FXML
  private TableColumn<Appointment, String> consultantTableColumn;
  
  private void deleteAppointment(Appointment appointment){
     PreparedStatement preparedStatement;
    try {
      preparedStatement = connection.prepareStatement("DELETE FROM appointment WHERE appointmentid =?");
      preparedStatement.setString(1, Integer.toString(appointment.getID()));
      preparedStatement.execute();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private ResultSet getAppointmentsFromDataBase(){
    ResultSet resultSet = null;
    Statement statement;
    try {
      statement = connection.createStatement();
      resultSet = statement.executeQuery(
      "SELECT appointmentid, start, title, type, customerName, appointment.customerId, userName, appointment.userId, description, location, contact, url, end " +
      "FROM appointment, customer, user " +
      "WHERE start >='" + DateTime.makeDateUTC(today) + "' " +
      "AND start <'" + DateTime.makeDateUTC(tomorrow) + "' " +
      "AND appointment.userId = user.userid " +
      "AND appointment.customerId = customer.customerid " + 
      "ORDER BY start");
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return resultSet;
  }
  
  @FXML
  private void handleAddButton() throws IOException, ClassNotFoundException, ParseException{
    AppointmentController.showDialog(stage, connection, null, "Add Appointment", userName);
    updateCalendar(false);
  }
  
  private void handleButton(boolean delete) throws ParseException, IOException, ClassNotFoundException{
    Appointment appointment = calendarTableView.getSelectionModel().getSelectedItem(); 
    if (appointment != null) {
      if(delete){
        deleteAppointment(appointment);
      }else{
        AppointmentController.showDialog(stage, connection, appointment, "Modify Appointment", userName);
      }
      updateCalendar(false);
    } else {
      AlertDialog.noSelectionDialog("appointment");
    }
  }
  
  @FXML
  private void handleComboBox() throws ParseException {
    Calendar cal = Calendar.getInstance();
    String view = viewComboBox.getValue();
    if("Month".equals(view)){
      cal.add(Calendar.MONTH, +1);
    }else{
      cal.add(Calendar.DAY_OF_YEAR, +7);
    }
    tomorrow = DATE_FORMAT.format(cal.getTime());
    updateCalendar(false);
  }
  
  @FXML
  private void handleCustomerButton() throws IOException, ClassNotFoundException, ParseException{
    CustomerController.showDialog(stage, connection, userName);
    updateCalendar(false);
  }
  
  @FXML
  private void handleDeleteButton() throws ParseException, IOException, ClassNotFoundException {    
    handleButton(true);
  }
  
  @FXML
  private void handleModifyButton() throws IOException, ClassNotFoundException, ParseException{    
    handleButton(false);
  }
  
  @FXML
  private void handleReportButton() throws IOException{
    ReportsController.showDialog(stage, connection);
  }
  
  @FXML
  private void initialize() throws IOException, ClassNotFoundException, ParseException{

    //Lambda expressions implemented to map appointment properties to table cells
    
    startTableColumn.setCellValueFactory(cellData -> cellData.getValue().startProperty());
    endTableColumn.setCellValueFactory(cellData -> cellData.getValue().endProperty());
    titleTableColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
    typeTableColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
    customerTableColumn.setCellValueFactory(cellData -> cellData.getValue().customerNameProperty());
    consultantTableColumn.setCellValueFactory(cellData -> cellData.getValue().userNameProperty());

    
    viewComboBox.getItems().addAll("Month", "Week");
    viewComboBox.getSelectionModel().select(0);
    
    Calendar cal = Calendar.getInstance();	
    today = DATE_FORMAT.format(cal.getTime());	
    cal.add(Calendar.MONTH, +1);	
    tomorrow = DATE_FORMAT.format(cal.getTime());
    
    makeConnection();
    
    updateCalendar(true);
    
    userName = LoginController.showDialog(stage, connection);
    if("".equals(userName)){
      Platform.exit();
    }else if(!reminder.equals("")){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Appointment Reminder");
      alert.setContentText(reminder);
      alert.showAndWait();
    }
  }
  
  private void makeConnection(){
    String URL = "jdbc:mysql://52.206.157.109/U04bLJ";
    String username = "U04bLJ";
    String password = "53688195100";
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connection = DriverManager.getConnection(URL, username, password);
    } catch (ClassNotFoundException | SQLException ex) {
      ex.printStackTrace();
    }
  }
  
  private void updateCalendar(boolean init) throws ParseException{
    ResultSet resultSet = getAppointmentsFromDataBase();
    calendar.clear();
    try {
      while (resultSet.next()) {
        int appointmentID = resultSet.getInt("appointmentid");
        String start = DateTime.makeDateLocal(resultSet.getString("start"));
        String title = resultSet.getString("title");
        String type = resultSet.getString("type");
        String customerName = resultSet.getString("customerName");
        int customerID = resultSet.getInt("customerId");
        String username = resultSet.getString("userName");
        int userId = resultSet.getInt("userId");
        String description = resultSet.getString("description");
        String location = resultSet.getString("location");
        String contact = resultSet.getString("contact");
        String URL = resultSet.getString("url");
        String end = DateTime.makeDateLocal(resultSet.getString("end"));
        Appointment appointment = new Appointment(appointmentID, start, title, type, customerName, customerID, username, userId, description, location, contact, URL, end);
        calendar.add(appointment);
        if(init){
          if(reminder.equals("")){         
            if (DateTime.inFifteenMinutes(today, start)) {
              String[] parts = start.split(" ");
              reminder = "You have a meeting at " + parts[1];
            }
          }
        }
      }
      calendarTableView.setItems(calendar);
    } catch (SQLException ex) {
        Logger.getLogger(CalendarController.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public void setStage(Stage stage){
    this.stage = stage;
  }
}
