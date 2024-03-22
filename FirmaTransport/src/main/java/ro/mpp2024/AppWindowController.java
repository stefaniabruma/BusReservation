package ro.mpp2024;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mpp2024.Model.Reservation;
import ro.mpp2024.Model.Route;
import ro.mpp2024.Service.Service;
import ro.mpp2024.Utils.Exceptions.InvalidRequestException;
import ro.mpp2024.Utils.Exceptions.NotFoundException;
import ro.mpp2024.Utils.GUIMessage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.stream.StreamSupport;

public class AppWindowController {
    private Service service;
    private Stage stage;
    private ObservableList<Route> model = FXCollections.observableArrayList();
    private ObservableList<Reservation> model2 = FXCollections.observableArrayList();
    private Route searchedRoute = null;

    @FXML
    TableView<Route> TableViewRoutes;
    @FXML
    TableColumn<Route, String> TableColumnDestination;
    @FXML
    TableColumn<Route, LocalDateTime> TableColumnDepartureTime;
    @FXML
    TableColumn<Route, Integer> TableColumnAvailable;
    @FXML
    TableColumn<Route, Integer> TableColumnTotal;
    @FXML
    TextField TextFieldDestination;
    @FXML
    TextField TextFieldDepartureTime;
    @FXML
    TableView<Reservation> TableViewReservations;
    @FXML
    TableColumn<Reservation, Integer> TableColumnSeatNo;
    @FXML
    TableColumn<Reservation, String> TableColumnClientName;
    @FXML
    TextField TextFieldNoSeats;
    @FXML
    TextField TextFieldClientName;

    public void setService(Service serv, Stage stage){
        this.service = serv;
        this.stage = stage;
        initModel();
    }

    @FXML
    public void initialize(){

        TableColumnDestination.setCellValueFactory(new PropertyValueFactory<Route, String>("destination"));
        TableColumnDepartureTime.setCellValueFactory(new PropertyValueFactory<Route, LocalDateTime>("departure_time"));
        TableColumnAvailable.setCellValueFactory(new PropertyValueFactory<Route, Integer>("no_available_seats"));
        TableColumnTotal.setCellValueFactory(new PropertyValueFactory<Route, Integer>("no_total_seats"));

        TableViewRoutes.setItems(model);

        TableColumnSeatNo.setCellValueFactory(new PropertyValueFactory<Reservation, Integer>("seat_no"));
        TableColumnClientName.setCellValueFactory(new PropertyValueFactory<Reservation, String>("client_name"));

        TableViewReservations.setItems(model2);
    }

    private void initModel() {
        model.setAll(StreamSupport.stream(service.findAllRoutes().spliterator(), false).toList());
    }
    @FXML
    public void handleSearch(){
        String destination = TextFieldDestination.getText();
        LocalDateTime dep_time;
        try{
            dep_time = LocalDateTime.parse(TextFieldDepartureTime.getText());
        }
        catch (DateTimeParseException e){
            searchedRoute = null;
            model2.setAll();
            GUIMessage.showMessage(stage, Alert.AlertType.INFORMATION, "Info", "Invalid DateTime format!");
            return;
        }

        try{
            searchedRoute = service.findRouteByDestinationAndDepartureTime(destination, dep_time);
            model2.setAll(StreamSupport.stream(service.findReservationByRouteId(searchedRoute.getId()).spliterator(), false).toList());
        }
        catch (NotFoundException e){
            searchedRoute = null;
            model2.setAll();
            GUIMessage.showMessage(stage, Alert.AlertType.INFORMATION, "Info", e.getMessage());
        }
    }
    @FXML
    public void handleReserve(){

        int no_req_seats;
        try{
            no_req_seats = Integer.parseInt(TextFieldNoSeats.getText());
        }
        catch (NumberFormatException e){
            GUIMessage.showMessage(stage, Alert.AlertType.ERROR, "Error", "No. of seats field is empty!");
            return;
        }

        String client_name = TextFieldClientName.getText();

        if(searchedRoute == null){
            GUIMessage.showMessage(stage, Alert.AlertType.ERROR, "Error", "No route found!");
            return;
        }

        try{
            service.reserveSeats(searchedRoute.getId(), no_req_seats, client_name);
        }
        catch (InvalidRequestException e){
            GUIMessage.showMessage(stage, Alert.AlertType.ERROR, "Error", e.getMessage());
            return;
        }

        TextFieldNoSeats.clear();
        TextFieldClientName.clear();

    }
    @FXML
    public void handleLogOut(){
        openLoginWindow();
        stage.close();
    }

    private void openLoginWindow() {
        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("login-view.fxml"));

            AnchorPane root = loader.load();

            Stage new_stage = new Stage();
            new_stage.setTitle("Firma Transport");
            new_stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            new_stage.setScene(scene);

            LoginController loginController = loader.getController();
            loginController.setService(service, new_stage);

            new_stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.close();
    }
}
