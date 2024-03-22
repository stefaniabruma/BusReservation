package ro.mpp2024;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import ro.mpp2024.Repository.EmployeesDBRepository;
import ro.mpp2024.Repository.ReservationsDBRepository;
import ro.mpp2024.Repository.RoutesDBRepository;
import ro.mpp2024.Service.Service;
import ro.mpp2024.Utils.JdbcUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class FXMain extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Firma Transport");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("login-view.fxml"));
        AnchorPane layout = loader.load();
        LoginController ctrl = loader.getController();

        ctrl.setService(getService(), primaryStage);
        Scene myScene = new Scene(layout);
        primaryStage.setScene(myScene);


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    static Service getService(){
        Properties serverProps = new Properties();
        try {
            serverProps.load(new FileReader("db.config"));

            System.out.println("Properties set. ");
            serverProps.list(System.out);
        } catch (IOException e) {
            System.out.println("Cannot find db.config "+e);
            return null;
        }

        EmployeesDBRepository repo_employee = new EmployeesDBRepository(new JdbcUtils(serverProps));
        RoutesDBRepository repo_route = new RoutesDBRepository(new JdbcUtils(serverProps));
        ReservationsDBRepository repo_res = new ReservationsDBRepository(new JdbcUtils(serverProps), repo_route);
        return new Service(repo_employee, repo_route, repo_res);
    }
}
