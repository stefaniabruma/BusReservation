package ro.mpp2024;

import ro.mpp2024.Model.Reservation;
import ro.mpp2024.Model.Route;
import ro.mpp2024.Repository.*;
import ro.mpp2024.Utils.JdbcUtils;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        EmployeesRepository er = new EmployeesDBRepository(new JdbcUtils(props));
        RoutesRepository rr = new RoutesDBRepository(new JdbcUtils(props));
        ReservationsRepository resr = new ReservationsDBRepository(new JdbcUtils(props), rr);

        System.out.println(er.findByUsername("andrei_p"));
        System.out.println(rr.findAll());
        System.out.println(rr.findRouteByDestinationDateTime("CLuj-Napoca", LocalDateTime.of(2024, 3, 22, 10, 0, 0)));
        System.out.println(resr.findByRouteId(1L));
        Route route = new Route();
        route.setId(1L);
        resr.add(new Reservation(route, 3, "Daniel Paduraru"));
        System.out.println(resr.findAll());
    }
}