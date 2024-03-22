package ro.mpp2024.Service;

import ro.mpp2024.Model.Employee;
import ro.mpp2024.Model.Reservation;
import ro.mpp2024.Model.Route;
import ro.mpp2024.Repository.EmployeesRepository;
import ro.mpp2024.Repository.ReservationsRepository;
import ro.mpp2024.Repository.RoutesRepository;
import ro.mpp2024.Utils.Exceptions.InvalidRequestException;
import ro.mpp2024.Utils.Exceptions.NotFoundException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class Service implements ServiceFirmaTransport{

    EmployeesRepository repo_employee;
    RoutesRepository repo_route;
    ReservationsRepository repo_res;

    public Service(EmployeesRepository repo_employee, RoutesRepository repo_route, ReservationsRepository repo_res) {
        this.repo_employee = repo_employee;
        this.repo_route = repo_route;
        this.repo_res = repo_res;
    }

    @Override
    public Employee findEmployeeByCredentials(String username, String password) {

        Employee employee = repo_employee.findByUsername(username);

        if(employee == null || !employee.getPassword().equals(password)){
            throw new NotFoundException("Incorrect credentials!");
        }

        return employee;
    }

    @Override
    public Iterable<Route> findAllRoutes() {
        return repo_route.findAll();
    }

    @Override
    public Route findRouteByDestinationAndDepartureTime(String destination, LocalDateTime dep_time) {
        Route route = repo_route.findRouteByDestinationDateTime(destination, dep_time);

        if(route == null){
            throw new NotFoundException("Route not found!");
        }

        return route;
    }

    @Override
    public Iterable<Reservation> findReservationByRouteId(Long route_id) {

        Route route = repo_route.findById(route_id);
        if(route == null){
            throw new NotFoundException("Route not found!");
        }

        var reservations = StreamSupport.stream(repo_res.findByRouteId(route.getId()).spliterator(), false).collect(Collectors.toList());

        int no_res_seats = route.getNo_total_seats() - route.getNo_available_seats();
        for (int i = no_res_seats + 1; i <= route.getNo_total_seats(); i++) {
            reservations.add(new Reservation(route, i, "-"));
        }

        return reservations;
    }

    @Override
    public void reserveSeats(Long route_id, int no_rq_seats, String client_name) {

        Route route = repo_route.findById(route_id);
        if(route == null){
            throw new NotFoundException("Route not found!");
        }

        if(route.getNo_available_seats() < no_rq_seats){
            throw new InvalidRequestException("Cannot reserve seats: not enough available seats!");
        }

        int no_res_seats = route.getNo_total_seats() - route.getNo_available_seats();

        for (int i = 0; i < no_rq_seats; i++) {

            no_res_seats += 1;
            repo_res.add(new Reservation(route, no_res_seats, client_name));
        }

        route.setNo_available_seats(route.getNo_total_seats() - no_res_seats);
        repo_route.update(route, route.getId());

    }
}
