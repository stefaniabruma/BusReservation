package ro.mpp2024.Repository;

import ro.mpp2024.Model.Route;

import java.time.LocalDateTime;

public interface RoutesRepository extends Repository<Route, Long> {
    public Route findRouteByDestinationDateTime(String destination, LocalDateTime dateTime);
}
