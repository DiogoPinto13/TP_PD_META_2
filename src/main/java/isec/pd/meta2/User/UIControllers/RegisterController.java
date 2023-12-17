package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.Shared.Register;
import isec.pd.meta2.User.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    private Stage stage;
    private Scene scene;
    private static Scene preScene;

    @FXML
    private PasswordField pass;
    @FXML
    private TextField user;
    @FXML
    private TextField nidentificacao;
    @FXML
    private TextField email;


    public void initialize() {}

    public void Register(ActionEvent event) throws IOException {
        if(pass.getText() == null || user.getText() == null || nidentificacao.getText() == null || email.getText() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dados vazios");
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os campos!");
            alert.showAndWait();
            return;
        }
        Register register = new Register(user.getText(),nidentificacao.getText(),email.getText(), pass.getText());

        if(Client.setObjectRegister(register)){
            Client.setUsername(register.getUsername());
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Client/beginClient.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Ocorreu um erro inesperado!");
            alert.showAndWait();
            return;
        }

    }

    public void backLogin(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Client/login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
