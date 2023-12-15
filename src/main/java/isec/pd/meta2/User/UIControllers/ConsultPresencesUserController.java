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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
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



        String a = Admin.GetInfoAboutEvent(designacao);
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
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Admin/consultaEventosCriados.fxml"));
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


        int i = tbpresencas.getSelectionModel().getSelectedIndex();

        if(i != -1) {
            Eventos e = (Eventos) tbpresencas.getItems().get(i);

            if (!Objects.equals(Admin.EliminatePresenceinEvent(nome.getText(), e.getHoraInicio() ), ErrorMessages.INVALID_REQUEST.toString())) {

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Presença eliminada");
                alert.setHeaderText(null);
                alert.setContentText("Uma presença foi eliminada neste evento!");
                alert.showAndWait();


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
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Erro");
                alert.setHeaderText(null);
                alert.setContentText("Ocorreu um erro ao eliminar a presença!");
                alert.showAndWait();
            }
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selecione uma presença");
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma linha da tabela!");
            alert.showAndWait();
        }
    }

    public void inserirPresenca(ActionEvent event) throws IOException {


        Parent root = null;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../../../../resources/fxml/Admin/inserirPresenca.fxml"));

            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == InsertPresenceController.class) {
                    return new InsertPresenceController(nome.getText());
                } else {
                    try {
                        return controllerClass.newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        preScene = stage.getScene();

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
}
