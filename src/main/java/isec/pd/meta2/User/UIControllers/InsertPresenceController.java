package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.Messages;
import isec.pd.meta2.User.Admin;
import javafx.application.Platform;
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

public class InsertPresenceController {
    @FXML
    public Label nome;
    @FXML
    public Label local;
    @FXML
    public Label hInicio;
    @FXML
    public Label hFim;
    @FXML
    public TextField username;

    private Stage stage;
    private Scene scene;
    String designacao;
    public InsertPresenceController(String text) {designacao=text;}


    public void initialize(){

        String a = Admin.GetInfoAboutEvent(designacao);
        if(a.equals(ErrorMessages.INVALID_EVENT_NAME))
            Platform.exit();
        String[] args = a.split(",");
        nome.setText(args[0]);
        local.setText(args[1]);
        hInicio.setText(args[2]);
        hFim.setText(args[3]);

    }

    public void inserirPresenca(ActionEvent actionEvent) throws IOException {

        if(username.getText().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nome de utilizador");
            alert.setHeaderText(null);
            alert.setContentText("Insira um nome de utilizador!");
            alert.showAndWait();
        }
        else{
            if(Admin.registerPresence(nome.getText(), username.getText()).equals(Messages.OK.toString())){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Presença registada");
                alert.setHeaderText(null);
                alert.setContentText("Uma presença foi registada neste evento!");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/consultaEventosCriados.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("Ocorreu um erro ao registar a presença!");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/consultaEventosCriados.fxml"));
                stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

            }
        }
    }

    public void Voltar(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/consultaEventosCriados.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
