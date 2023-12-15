package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.Shared.EventResult;
import isec.pd.meta2.User.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class PresencesController {
    @FXML
    public VBox vbox;
    @FXML
    public TableView tbPresenca;
    @FXML
    public TableColumn tbDesignacao;
    @FXML
    public TableColumn tbLocal;
    @FXML
    public TableColumn tbInicio;
    @FXML
    public TableColumn tbFim;
    @FXML
    public ComboBox CBFiltros;
    @FXML
    public Button pesquisa;
    @FXML
    public TextField filtro;

    private Stage stage;
    private Scene scene;
    private static Scene preScene;

    private ObservableList<Eventos> dataEventos;

    public void initialize() {

        ObservableList<String> options = FXCollections.observableArrayList("Designação","Local", "Inicio", "Fim", "Todos");
        CBFiltros.setItems(options);
        CBFiltros.getSelectionModel().selectLast();

        Image imageDecline = new Image(getClass().getResourceAsStream("resources/lupa2.gif"));
        ImageView visualizadorImagem = new ImageView(imageDecline);
        visualizadorImagem.setFitWidth(20);
        visualizadorImagem.setFitHeight(15);
        pesquisa.setStyle("-fx-background-color: #ffff; -fx-border-color: gray; -fx-border-width: 1px; -fx-border-radius: 5px;");
        pesquisa.setGraphic(visualizadorImagem);


        EventResult eventResult = Client.getPresences(Client.getUsername());
        preencheTabela(eventResult);

    }

    public void back(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("../../../../../../resources/fxml/Client/beginClient.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void ExportCSV(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File save...");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSVs (*.csv)","*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showSaveDialog(vbox.getScene().getWindow());
        if(file != null){
            try {
                exportData(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void exportData(File file) throws Exception {
        Writer writer = null;
        try {
            //File file = new File("data.csv");
            writer = new BufferedWriter(new FileWriter(file));

            for (Eventos event : dataEventos) {
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

    public void pesquisa(ActionEvent actionEvent) {
        String selectedType = CBFiltros.getSelectionModel().getSelectedItem().toString();

        if(selectedType.equals("Todos")){
            dataEventos.clear();
            EventResult eventResult = Client.getPresences(Client.getUsername());
            preencheTabela(eventResult);
        }
        else {
            if(!filtro.getText().equals("")) {
                if (selectedType.equals("Designação")) {
                    dataEventos.clear();
                    EventResult eventResult = Client.queryEvents("designacao", filtro.getText(), Client.getUsername());
                    preencheTabela(eventResult);
                } else if (selectedType.equals("Local")) {
                    dataEventos.clear();
                    EventResult eventResult = Client.queryEvents("place", filtro.getText(), Client.getUsername());
                    preencheTabela(eventResult);
                } else if (selectedType.equals("Inicio")) {
                    dataEventos.clear();
                    EventResult eventResult = Client.queryEvents("horaInicio", filtro.getText(), Client.getUsername());
                    preencheTabela(eventResult);
                } else if (selectedType.equals("Fim")) {
                    dataEventos.clear();
                    EventResult eventResult = Client.queryEvents("horaFim", filtro.getText(), Client.getUsername());
                    preencheTabela(eventResult);
                }
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Pesquisa");
                alert.setHeaderText(null);
                alert.setContentText("Indique o que pesquisar");
                alert.showAndWait();
            }
        }

    }
    public void preencheTabela(EventResult eventResult){
        if (eventResult == null) {
            eventResult = new EventResult(" ");
            eventResult.setColumns(" ");
            return;
        }

        ArrayList<String> eventosNovos = eventResult.events;
        dataEventos = FXCollections.observableArrayList();

        tbDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        tbLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        tbInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        tbFim.setCellValueFactory(new PropertyValueFactory<>("horafim"));


        for (String evento : eventosNovos) {
            String[] eventoData = evento.split(",");
            Eventos event = new Eventos();

            event.setDesignacao(eventoData[1]);
            event.setLocal(eventoData[2]);
            event.setHoraInicio(eventoData[3]);
            event.setHoraFim(eventoData[4]);
            dataEventos.add(event);
        }

        tbPresenca.setItems(dataEventos);

    }
}
