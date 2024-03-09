package ro.mpp2024.Model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Route extends Entity<Long>{

    private String destination;
    private LocalDateTime departure_time;
    private int no_available_seats;
    private int no_total_seats;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDateTime getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(LocalDateTime departure_time) {
        this.departure_time = departure_time;
    }

    public int getNo_available_seats() {
        return no_available_seats;
    }

    public void setNo_available_seats(int no_available_seats) {
        this.no_available_seats = no_available_seats;
    }

    public int getNo_total_seats() {
        return no_total_seats;
    }

    public void setNo_total_seats(int no_total_seats) {
        this.no_total_seats = no_total_seats;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Route route = (Route) o;
        return no_available_seats == route.no_available_seats && no_total_seats == route.no_total_seats && Objects.equals(destination, route.destination) && Objects.equals(departure_time, route.departure_time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), destination, departure_time, no_available_seats, no_total_seats);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", destination='" + destination + '\'' +
                ", departure_time=" + departure_time +
                ", no_available_seats=" + no_available_seats +
                ", no_total_seats=" + no_total_seats ;
    }
}
