package scheduler.model;

import javafx.scene.control.Alert;

public class AlertDialog {
  public static void errorDialog(String errorMessage){
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Invalid Fields");
    alert.setHeaderText("Please correct the invalid fields");
    alert.setContentText(errorMessage);
    alert.showAndWait();
  }
  
  public static void noSelectionDialog(String text){
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("No Selection");
    alert.setHeaderText("No " + text +" selected");
    alert.setContentText("Please select a " + text + " in the table.");
    alert.showAndWait();
  }
}
