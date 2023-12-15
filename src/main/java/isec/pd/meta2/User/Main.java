package isec.pd.meta2.User;


import isec.pd.meta2.User.UIControllers.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Main extends Application {
    public static final String noServerErrorTitle = "Server not Found or Offline";
    public static final String noServerErrorDescription = "The requested Server could not be found or is offline.";
    public static final String requestTimeoutErrorTitle = "Request Timeout";
    public static final String requestTimeoutErrorDescription = "The request to the server timed out.";

    public static void main(String[] args) {
        launch();
    }

    public static void fatalErrorNotification(String ... args){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(args[0]);
        alert.setContentText(args[1]);
        alert.showAndWait();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Client/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        LoginController controller = fxmlLoader.getController();
        controller.setStage(stage);
        stage.setTitle("Sistema Registo de Presenças");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}