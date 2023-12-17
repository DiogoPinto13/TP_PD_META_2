package isec.pd.meta2.User.UIControllers;


import isec.pd.meta2.Shared.Messages;
import isec.pd.meta2.Shared.Time;
import isec.pd.meta2.User.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class criarEventoController {

    @FXML
    public DatePicker data;
    @FXML
    public Spinner<Integer> horaInicio;
    @FXML
    public Spinner<Integer> minutosInicio;
    @FXML
    public Spinner<Integer> horaFim;
    @FXML
    public Spinner<Integer> minutosFim;

    @FXML
    public TextField local;
    @FXML
    public TextField nome;

    private Stage stage;
    private Scene scene;

    public void initialize() {

    }

    public void createEvent(ActionEvent event) throws IOException {
        LocalDate dataSelecionada = data.getValue();
        int dia = dataSelecionada.getDayOfMonth();
        int mes = dataSelecionada.getMonthValue();
        int ano = dataSelecionada.getYear();

        if(nome.getText().equals("") || local.getText().equals("") || data.getValue().equals("")){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dados vazios");
            alert.setHeaderText(null);
            alert.setContentText("Por favor, preeencha todos os campos.");
            alert.showAndWait();
        }
        else{
            if(!Admin.createEvent(nome.getText(), local.getText(), new Time(ano,mes,dia, horaInicio.getValue(), minutosInicio.getValue()), new Time(ano,mes,dia, horaFim.getValue(), minutosFim.getValue())).equals(Messages.OK.toString())){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("Ocorreu um erro ao criar o evento!");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin/consultaEventosCriados.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Evento criado");
                alert.setHeaderText(null);
                alert.setContentText("O evento foi criado com sucesso!");
                alert.showAndWait();

                Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin/consultaEventosCriados.fxml"));
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            }
        }
    }

    public void voltar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin/consultaEventosCriados.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
