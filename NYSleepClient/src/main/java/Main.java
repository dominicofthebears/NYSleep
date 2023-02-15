import controllers.Controller;
import controllers.MainPageController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {

    public void start(Stage primaryStage) {
        try {
            Controller.initializeRegistry();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainPageUnregistered.fxml"));
            Parent root = loader.load();
            Screen screen = Screen.getPrimary();
            primaryStage.setTitle("NY Sleep");
            Rectangle2D bounds = screen.getVisualBounds();
            Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
            primaryStage.setScene(scene);
            primaryStage.show();

            MainPageController controller = loader.getController();
            controller.setStage(primaryStage);

        }catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        }

    }


    public static void main(String[] args) {
        launch(args);
    }
}

