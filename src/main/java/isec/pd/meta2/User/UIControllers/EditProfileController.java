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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class EditProfileController {
    @FXML
    private TextField nome;
    @FXML
    private PasswordField pass;
    @FXML
    private Label email;
    @FXML
    private TextField nidentificacao;

    private Stage stage;
    private Scene scene;


    public void initialize() {
        String data = Client.getProfileData(Client.getUsername());
        String[] aData = data.split(",");

        nidentificacao.setText(aData[0]);
        email.setText(aData[1]);
        pass.setText(aData[2]);
        nome.setText(aData[3]);
    }


    public void saveData(ActionEvent actionEvent) throws IOException {

        if(nidentificacao.getText() == null || pass.getText() == null || nome.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aviso!");
            alert.setHeaderText(null);
            alert.setContentText("Preencha todos os dados do seu perfil!");
            alert.showAndWait();
        }
        else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(email.getText()).append(",");
            stringBuilder.append(nome.getText()).append(",");
            stringBuilder.append(nidentificacao.getText()).append(",");
            stringBuilder.append(pass.getText());
            System.out.println(stringBuilder.toString());

            if (Client.editProfile(stringBuilder.toString())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERRO!");
                alert.setHeaderText(null);
                alert.setContentText("Ocorreu um erro ao atualizar o seu perfil!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("ERRO!");
                alert.setHeaderText(null);
                alert.setContentText("Perfil atualizado!");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Client/beginClient.fxml"));
                stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Client/beginClient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
