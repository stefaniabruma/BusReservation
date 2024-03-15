package ro.mpp2024.Repository;

import ro.mpp2024.Model.Reservation;

public interface ReservationsRepository extends Repository<Reservation, Long> {
    public Iterable<Reservation> findByRouteId(Long route_id);
}
