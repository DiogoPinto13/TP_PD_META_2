package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.Time;
import isec.pd.meta2.User.Admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

public class EditEventController {
    String designacao;

    @FXML
    public Label nome;
    @FXML
    public Label local;
    @FXML
    public DatePicker data;
    @FXML
    public Spinner<Integer> hInicio;
    @FXML
    public Spinner<Integer> hFim;
    @FXML
    public Spinner<Integer> mInicio;

    @FXML
    public Spinner<Integer> mFim;

    private Stage stage;
    private Scene scene;


    public EditEventController(String s){designacao=s;}

    public void initialize(){
        String[] info = Admin.GetInfoAboutEvent(designacao).split(",");
        nome.setText(info[0]);
        local.setText(info[1]);

        try{
            Time dataInicio = new Time(info[2]);
            Time dataFim = new Time(info[3]);
            System.out.println(dataInicio.toStringDay());
            data.setValue(LocalDate.parse(dataInicio.toStringDay()));

            hFim.getValueFactory().setValue(dataFim.getHour());
            mFim.getValueFactory().setValue(dataFim.getMinute());

            hInicio.getValueFactory().setValue(dataInicio.getHour());
            mInicio.getValueFactory().setValue(dataInicio.getMinute());
        }catch(java.text.ParseException e){
            System.out.println(e.getMessage());
        }
    }

    public void Voltar(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/consultaEventosCriados.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void EditarEvento(ActionEvent event) throws IOException {
        LocalDate dataSelecionada = data.getValue();
        int dia = dataSelecionada.getDayOfMonth();
        int mes = dataSelecionada.getMonthValue();
        int ano = dataSelecionada.getYear();

        if(Objects.equals(Admin.editEvent(nome.getText(), new Time(ano, mes, dia, hInicio.getValue(), mInicio.getValue()), new Time(ano, mes, dia, hFim.getValue(), mFim.getValue())), ErrorMessages.CREATE_EVENT_FAILED.toString())){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Ocorreu um erro");
            alert.setHeaderText(null);
            alert.setContentText("Ocorreu um erro e o evento não foi atualizado!");
            alert.showAndWait();

            Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/consultaEventosCriados.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Evento atualizado");
            alert.setHeaderText(null);
            alert.setContentText("O evento foi atualizado com sucesso!");
            alert.showAndWait();

            Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/consultaEventosCriados.fxml"));
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
    }
}
