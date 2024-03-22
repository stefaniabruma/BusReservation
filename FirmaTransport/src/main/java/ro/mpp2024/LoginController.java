package ro.mpp2024;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ro.mpp2024.Model.Employee;
import ro.mpp2024.Service.Service;
import ro.mpp2024.Utils.Exceptions.NotFoundException;
import ro.mpp2024.Utils.GUIMessage;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginController {

    Service service;

    @FXML
    TextField TextFieldUsername;

    @FXML
    PasswordField TextFieldPassword;

    private Stage stage;

    public void setService(Service serv, Stage stage){
        this.service = serv;
        this.stage = stage;

        TextFieldPassword.setText("implicit");
    }

    public void handleLogIn() throws NoSuchAlgorithmException {

        String username = TextFieldUsername.getText();
        String password = TextFieldPassword.getText();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        // Convert the byte array to a hexadecimal string
        StringBuilder hashedPassword = new StringBuilder();
        for (byte b : md.digest()) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hashedPassword.append('0');
            }
            hashedPassword.append(hex);
        }

        password = hashedPassword.toString();

        Employee employee;
        try{
            employee = service.findEmployeeByCredentials(username, password);
        }
        catch (NotFoundException e){
            GUIMessage.showMessage(stage, Alert.AlertType.ERROR, "Error", e.getMessage());
            return;
        }

        showAppWindow(employee);

    }

    private void showAppWindow(Employee em) {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("appwindow-view.fxml"));

            AnchorPane root = loader.load();

            Stage new_stage = new Stage();
            new_stage.setTitle("Firma Transport");
            new_stage.initModality(Modality.WINDOW_MODAL);
            Scene scene = new Scene(root);
            new_stage.setScene(scene);

            AppWindowController awController = loader.getController();
            awController.setService(service, new_stage);

            new_stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        stage.close();
    }

}
