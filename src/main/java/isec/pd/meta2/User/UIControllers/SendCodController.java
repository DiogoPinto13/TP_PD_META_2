package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.User.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SendCodController {
    @FXML
    public TextField cod;
    @FXML
    public Label mensagem;

    private Stage stage;
    private Scene scene;
    private static Scene preScene;



    public void initialize() {}

    public void registCod(ActionEvent actionEvent) throws IOException {

        if(isNumeric(cod.getText())){
            boolean result = Client.sendCode(cod.getText());
            if(result){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Código inválido");
                alert.setHeaderText(null);
                alert.setContentText("O código inserido é inválido!");
                alert.showAndWait();
                return;
            }
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Client/beginClient.fxml"));
            stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            mensagem.setText("Insira um código válido!");
        }
    }

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public void back(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Client/beginClient.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
