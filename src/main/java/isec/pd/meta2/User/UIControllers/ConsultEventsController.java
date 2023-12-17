package isec.pd.meta2.User.UIControllers;

import isec.pd.meta2.Shared.*;
import isec.pd.meta2.User.Admin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.*;
import java.util.ArrayList;

public class ConsultEventsController {
    private Stage stage;
    private Scene scene;
    @FXML
    public VBox consultEvents;
    @FXML
    public TableView tbEvento;
    @FXML
    public TableColumn tbDesignacao;
    @FXML
    public TableColumn tbLocal;
    @FXML
    public TableColumn tbInicio;
    @FXML
    public TableColumn tbFim;
    @FXML
    public Button pesquisa;
    @FXML
    public TextField filtro;
    @FXML
    public ComboBox<String> filtros;
    private static Scene preScene;
    private ObservableList<Eventos> dataEventos;

    public void initialize(){
        update();
    }
    public void update(){

        ObservableList<String> options = FXCollections.observableArrayList("Designação","Local", "Inicio", "Fim", "Todos");
        filtros.setItems(options);
        filtros.getSelectionModel().selectLast();


        Image imageDecline = new Image(getClass().getResourceAsStream("/fxml/lupa2.gif"));
        //Image imagem = new Image(getClass().getResourceAsStream("resources/lupa1.png")); // 1
        ImageView visualizadorImagem = new ImageView(imageDecline); // 2
        visualizadorImagem.setFitWidth(20); // 3
        visualizadorImagem.setFitHeight(15);
        pesquisa.setStyle("-fx-background-color: #ffff; -fx-border-color: gray; -fx-border-width: 1px; -fx-border-radius: 5px;");
        pesquisa.setGraphic(visualizadorImagem);

        EventResult eventResult = Admin.getEvents(Admin.getUsername());
        if(eventResult == null){
            eventResult = new EventResult(" ");
            eventResult.setColumns(" ");
            return;
        }
        String[] nomeColunas = eventResult.getColumns().split(",");

        ObservableList<String> observableList = FXCollections.observableArrayList(eventResult.events);
        //tbPresenca.setItems(observableList);
        //pede a lista das presenças e preenche a tabela
        ArrayList<String> eventos = eventResult.events;
        //int size = eventos.size();

        dataEventos = FXCollections.observableArrayList();

        tbDesignacao.setCellValueFactory(new PropertyValueFactory<>("designacao"));
        tbLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        tbInicio.setCellValueFactory(new PropertyValueFactory<>("horaInicio"));
        tbFim.setCellValueFactory(new PropertyValueFactory<>("horafim"));


        for(String evento : eventos){
            String[] eventoData = evento.split(",");
            Eventos event = new Eventos();

            event.setDesignacao(eventoData[1]);
            event.setLocal(eventoData[2]);
            event.setHoraInicio(eventoData[3]);
            event.setHoraFim(eventoData[4]);
            dataEventos.add(event);
        }

        tbEvento.setItems(dataEventos);

    }

    public void ConsultPresentsEvent(ActionEvent actionEvent) {



        int i = tbEvento.getSelectionModel().getSelectedIndex();

        if(i != -1) {
            Eventos eventos = (Eventos) tbEvento.getItems().get(i);

            Parent root = null;
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Admin/consultaPresencasEvento.fxml"));

                loader.setControllerFactory(controllerClass -> {
                    if (controllerClass == ConsultPresencesUserController.class) {
                        return new ConsultPresencesUserController(eventos.getDesignacao());
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
            stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            preScene = stage.getScene();

            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selecione um evento");
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma linha da tabela!");
            alert.showAndWait();
        }
    }

    public void Edit(ActionEvent actionEvent) throws IOException {

    }

    public void eliminate(ActionEvent actionEvent) {
        int i = tbEvento.getSelectionModel().getSelectedIndex();

        if(i!=-1) {
            Eventos eventos = (Eventos) tbEvento.getItems().get(i);


            //if (Objects.equals(Admin.CheckPresences(eventos.getDesignacao()), ErrorMessages.INVALID_REQUEST.toString())) {

                if (Admin.deleteEvent(eventos.getDesignacao()).equals(ErrorMessages.INVALID_EVENT_NAME.toString())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Evento eliminado!");
                    alert.setHeaderText(null);
                    alert.setContentText("O evento foi eliminado!");
                    alert.showAndWait();


                    //buscar os novos eventos para mostrar na tabela!
                    dataEventos.clear();

                    EventResult eventResult = Admin.getEvents(Admin.getUsername());
                    if (eventResult == null) {
                        eventResult = new EventResult(" ");
                        eventResult.setColumns(" ");
                        return;
                    }
                    String[] nomeColunas = eventResult.getColumns().split(",");

                    ObservableList<String> observableList = FXCollections.observableArrayList(eventResult.events);
                    //tbPresenca.setItems(observableList);
                    //pede a lista das presenças e preenche a tabela
                    ArrayList<String> eventosNovos = eventResult.events;
                    //int size = eventos.size();

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

                    tbEvento.setItems(dataEventos);

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Ocorreu um erro!");
                    alert.setHeaderText(null);
                    alert.setContentText("Ocorreu um erro a eliminar o evento!");
                    alert.showAndWait();
                }
            /*} else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Não é possivel eliminar o evento!");
                alert.setHeaderText(null);
                alert.setContentText("Não é possível eliminar o evento ou ocorreu um erro e não foi encontrado!");
                alert.showAndWait();
            }*/
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selecione um evento");
            alert.setHeaderText(null);
            alert.setContentText("Selecione uma linha da tabela!");
            alert.showAndWait();
        }

    }

    public void createEvent(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin/criarEvento.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void pesquisa(ActionEvent actionEvent) {

        String selectedType = filtros.getSelectionModel().getSelectedItem().toString();

        if(selectedType.equals("Todos")){
            dataEventos.clear();
            EventResult eventResult = Admin.getEvents(Admin.getUsername());
            preencheTabela(eventResult);
        }
        else {
            if(!filtro.getText().equals("")) {
                if (selectedType.equals("Designação")) {
                    dataEventos.clear();
                    EventResult eventResult = Admin.queryEvents("eventDesignation", filtro.getText());
                    preencheTabela(eventResult);
                } else if (selectedType.equals("Local")) {
                    dataEventos.clear();
                    EventResult eventResult = Admin.queryEvents("place", filtro.getText());
                    preencheTabela(eventResult);
                } else if (selectedType.equals("Inicio")) {
                    dataEventos.clear();
                    EventResult eventResult = Admin.queryEvents("timeBegin", filtro.getText());
                    preencheTabela(eventResult);
                } else if (selectedType.equals("Fim")) {
                    dataEventos.clear();
                    EventResult eventResult = Admin.queryEvents("timeEnd", filtro.getText());
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

    public void voltar(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Admin/beginAdmin.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exportCSV(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("File save...");
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSVs (*.csv)","*.csv"),
                new FileChooser.ExtensionFilter("All files", "*.*")
        );
        File file = fileChooser.showSaveDialog(consultEvents.getScene().getWindow());
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

        tbEvento.setItems(dataEventos);

    }
}
