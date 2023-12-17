package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.Login;
import isec.pd.meta2.User.Client;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML
    private TextField user;
    @FXML
    private PasswordField pass;
    @FXML
    private Label errorMessage;
    @FXML
    private Pane background;
    private Stage stage;
    private Scene scene;

    public LoginController(){
        //Client.prepareClient();
    }

    public void initialize() {
        background.setStyle("-fx-background-image: url('../../../../../../resources/fxml/background.png'); " +
                "-fx-background-size: cover;");
    }

    public void setStage(Stage stage){
        this.stage = stage;
    }

    public void OpenRegisto(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Client/registo.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void Login(ActionEvent event) throws IOException{

        if(user.getText()==null || pass.getText()==null)
            return;

        Login login = new Login(user.getText(),pass.getText());
        String retorno = Client.setObjectLogin(login);


        if(retorno.equals(ErrorMessages.LOGIN_NORMAL_USER.toString())){
            Client.setUsername(login.getUsername());
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Client/beginClient.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if(retorno.equals(ErrorMessages.LOGIN_ADMIN_USER.toString())){
            //vamos fechar a connection como client normal
            //InetAddress host = Client.getSocket().getLocalAddress();
            //int port = Client.getSocket().getLocalPort();
            //Client.closeConnection();

            //vamos abrir uma connection especial pro admin
            //Admin.prepareAdmin();
            //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/Client/login.fxml"));

            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin/beginAdmin.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            errorMessage.setText("Dados incorretos!");
        }
    }
}
