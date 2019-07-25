package testfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Ui extends Application {

    public static void main(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //ServerConfig.initConfig();
        var root = FXMLLoader.<Parent>load(this.getClass().getResource("/ui.fxml"));
        primaryStage.setTitle("Test rxjavafx");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public Ui() {
    }
}
