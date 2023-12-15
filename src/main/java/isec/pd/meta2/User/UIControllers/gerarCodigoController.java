package isec.pd.meta2.User.UIControllers;


import isec.pd.meta2.Shared.EventResult;
import isec.pd.meta2.User.Admin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

import static isec.pd.meta2.User.UIControllers.SendCodController.isNumeric;


public class gerarCodigoController {
    private Stage stage;
    private Scene scene;
    @FXML
    public ComboBox evento;
    @FXML
    public TextField duracao;
    @FXML
    public Label code;
    private ObservableList<Eventos> dataEventos;


    public void initialize() {

        EventResult eventResult = Admin.getEvents(Admin.getUsername());
        if(eventResult == null){
            eventResult = new EventResult(" ");
            return;
        }
        ArrayList<String> eventos = eventResult.events;
        ObservableList<String> values = FXCollections.observableArrayList();

        dataEventos = FXCollections.observableArrayList();
        /*for(String evento : eventos){
            String[] eventoData = evento.split(",");
            Eventos event = new Eventos();
            event.setDesignacao(eventoData[1]);
            event.setLocal(eventoData[2]);
            event.setHoraInicio(eventoData[3]);
            event.setHoraFim(eventoData[4]);
            dataEventos.add(event);
        }*/

        for(int i=0; i<eventos.size() ;i++) {
            String[] eventoData = eventos.get(i).split(",");
            values.add(eventoData[1]);
        }


        evento.setItems(values);

    }

    public void Voltar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/beginAdmin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void GerarCod(ActionEvent event) {

        if(duracao.getText() != null && isNumeric(duracao.getText())) {
            evento.getValue();
            String codigoObtido = Admin.generatePresenceCode(evento.getValue().toString(), Integer.parseInt(duracao.getText()));
            if (codigoObtido != null)
                code.setText(codigoObtido);
            else
                code.setText("Erro ao gerar o código!");
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.setContentText("Insira uma duração válida!");
            alert.showAndWait();
        }

    }
}
