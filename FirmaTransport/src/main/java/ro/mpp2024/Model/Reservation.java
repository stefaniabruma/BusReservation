package ro.mpp2024.Model;

import java.util.Objects;

public class Reservation extends Entity<Long>{

    private Route route;
    private int seat_no;
    private String client_name = "-";

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(int seat_no) {
        this.seat_no = seat_no;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Reservation that = (Reservation) o;
        return route == that.route && seat_no == that.seat_no && Objects.equals(client_name, that.client_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), route, seat_no, client_name);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", route_id=" + route +
                ", seat_no=" + seat_no +
                ", client_name='" + client_name + '\'';
    }
}
