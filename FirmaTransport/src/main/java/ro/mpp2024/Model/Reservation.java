package ro.mpp2024.Model;

import java.util.Objects;

public class Reservation extends Entity<Long>{

    private long route_id;
    private int seat_no;
    private String client_name = "-";

    public long getRoute_id() {
        return route_id;
    }

    public void setRoute_id(long route_id) {
        this.route_id = route_id;
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
        return route_id == that.route_id && seat_no == that.seat_no && Objects.equals(client_name, that.client_name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), route_id, seat_no, client_name);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", route_id=" + route_id +
                ", seat_no=" + seat_no +
                ", client_name='" + client_name + '\'';
    }
}
