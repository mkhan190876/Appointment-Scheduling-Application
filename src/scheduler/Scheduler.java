//Mohammed Khan C195 Software 2
package scheduler;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import scheduler.view_controller.CalendarController;

public class Scheduler extends Application {
  private Stage stage;
  private BorderPane calendarLayout;
  
  public Stage getStage() {
    return stage;
  }

  public static void main(String[] args) {
    launch(args);
  }
  
  @Override
  public void start(Stage stage) throws Exception {
    this.stage = stage;
    this.stage.setTitle("Scheduler");
    showCalendar();
  }
  
  private void showCalendar() throws IOException {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(Scheduler.class.getResource("view_controller/Calendar.fxml"));
    calendarLayout = (BorderPane) loader.load();

    CalendarController calendarController = loader.getController();
    calendarController.setStage(stage);

    Scene scene = new Scene(calendarLayout);
    stage.setScene(scene);
    stage.show();
  }
}
