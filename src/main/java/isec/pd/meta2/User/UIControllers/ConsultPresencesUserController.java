package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.EventResult;
import isec.pd.meta2.User.Admin;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class ConsultPresencesUserController {
    @FXML
    public Label nome;
    @FXML
    public Label local;
    @FXML
    public Label hInicio;
    @FXML
    public Label hFim;
    @FXML
    public VBox consultPresences;

    @FXML
    public TableColumn nome1;
    @FXML
    public TableColumn username;
    @FXML
    public TableColumn hRegisto;
    @FXML
    public TableView tbpresencas;

    private ObservableList<Eventos> dataPresences;
    String designacao;

    private Stage stage;
    private Scene scene;
    private static Scene preScene;

    public ConsultPresencesUserController(String d){designacao = d;}

    public void initialize(){
        //pra nao adicionar mais um endpoint vamos usar a função que já temos com filtro e passamos a designação do evento
        EventResult eventResultInfo = Admin.queryEvents("designacao", designacao);
        //só irá dar um único resultado
        String a = eventResultInfo.events.get(0);
        if(a.equals(ErrorMessages.INVALID_EVENT_NAME))
          Platform.exit();
        String[] args = a.split(",");
        nome.setText(args[0]);
        local.setText(args[1]);
        hInicio.setText(args[2]);
        hFim.setText(args[3]);


        EventResult eventResult = Admin.getPresencesEvent(designacao);
        if(eventResult == null){
            eventResult = new EventResult(" ");
            eventResult.setColumns(" ");
            return;
        }

        ArrayList<String> eventos = eventResult.events;


        dataPresences = FXCollections.observableArrayList();

        nome1.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        username.setCellValueFactory(new PropertyValueFactory<>("local"));
        hRegisto.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));


        for(String evento : eventos){
            String[] eventoData = evento.split(",");
            Eventos event = new Eventos();
            event.setDesignacao(eventoData[0]);
            event.setLocal(eventoData[1]);
            event.setHoraInicio(eventoData[2]);
            dataPresences.add(event);
        }
        tbpresencas.setItems(dataPresences);
    }


    public void Voltar(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin/consultaEventosCriados.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exportarCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File save...");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSVs (*.csv)","*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showSaveDialog(consultPresences.getScene().getWindow());
        if(file != null){
            try {
                exportData(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void exportData(File file) throws IOException {
        Writer writer = null;
        try {
            //File file = new File("data.csv");
            writer = new BufferedWriter(new FileWriter(file));

            for (Eventos event : dataPresences) {
                String text = event.getDesignacao() + "," + event.getLocal() + "," + event.getHoraInicio() + "," + event.getHorafim() + "\n";
                writer.write(text);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            writer.flush();
            writer.close();
        }
    }

    public void eliminarPresenca(ActionEvent actionEvent) {

    }

    public void inserirPresenca(ActionEvent event) throws IOException {

    }
}
