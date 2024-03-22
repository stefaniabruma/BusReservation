package ro.mpp2024.Service;

import ro.mpp2024.Model.Employee;
import ro.mpp2024.Model.Reservation;
import ro.mpp2024.Model.Route;

import java.time.LocalDateTime;

public interface ServiceFirmaTransport {
    Employee findEmployeeByCredentials(String username, String password);
    Iterable<Route> findAllRoutes();
    Route findRouteByDestinationAndDepartureTime(String destination, LocalDateTime dep_time);
    Iterable<Reservation> findReservationByRouteId(Long route_id);
    void reserveSeats(Long route_id, int no_rq_seats, String client_name);
}
