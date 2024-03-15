package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Model.Reservation;
import ro.mpp2024.Model.Route;
import ro.mpp2024.Utils.JdbcUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationsDBRepository implements ReservationsRepository {

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    private RoutesRepository route_repo;

    public ReservationsDBRepository(JdbcUtils dbUtils, RoutesRepository route_repo) {
        this.dbUtils = dbUtils;
        this.route_repo = route_repo;
    }

    @Override
    public void add(Reservation reservation) {

        logger.traceEntry("saving reservation {}", reservation);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "insert into reservations (route_id, seat_no, client_name) " +
                        "values (?, ?, ?)")) {

            preStmt.setLong(1, reservation.getRoute().getId());
            preStmt.setInt(2, reservation.getSeat_no());
            preStmt.setString(3, reservation.getClient_name());

            int result = preStmt.executeUpdate();
            logger.trace("Saving {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Reservation reservation) {

        logger.traceEntry("deleting reservation {}", reservation);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "delete from reservations " +
                        "where id = ?")) {

            preStmt.setLong(1, reservation.getId());

            int result = preStmt.executeUpdate();
            logger.trace("deleting {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();


    }

    @Override
    public void update(Reservation reservation, Long id) {

        logger.traceEntry("update reservation {}", reservation);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "update reservations " +
                        "set route_id = ?, seat_no = ?, client_name = ? " +
                        "where id = ?")) {

            preStmt.setLong(1, reservation.getRoute().getId());
            preStmt.setInt(2, reservation.getSeat_no());
            preStmt.setString(3, reservation.getClient_name());
            preStmt.setLong(4, reservation.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Update {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();

    }

    @Override
    public Reservation findById(Long id) {
        logger.traceEntry("Finding reservation by id {}", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from reservations where id = ?")) {

            preStmt.setLong(1, id);

            try (ResultSet resultSet = preStmt.executeQuery()) {

                if (resultSet.next()) {

                    Long route_id = resultSet.getLong("route_id");
                    Route route = route_repo.findById(route_id);
                    int seat_no = resultSet.getInt("seat_no");
                    String client_name = resultSet.getString("client_name");

                    Reservation reservation = new Reservation(route, seat_no, client_name);
                    reservation.setId(id);

                    logger.traceExit(reservation);
                    return reservation;
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD " + ex);
        }
        logger.traceExit(null);
        return null;
    }

    @Override
    public Iterable<Reservation> findAll() {
        logger.traceEntry("Finding all reservations");
        Connection con = dbUtils.getConnection();

        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from reservations")) {

            try (ResultSet resultSet = preStmt.executeQuery()) {

                while (resultSet.next()) {

                    long id = resultSet.getLong("id");
                    Long route_id = resultSet.getLong("route_id");
                    Route route = route_repo.findById(route_id);
                    int seat_no = resultSet.getInt("seat_no");
                    String client_name = resultSet.getString("client_name");

                    Reservation reservation = new Reservation(route, seat_no, client_name);
                    reservation.setId(id);

                    reservations.add(reservation);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD " + ex);
        }
        logger.traceExit(reservations);
        return reservations;
    }

    @Override
    public Iterable<Reservation> findByRouteId(Long route_id) {
        logger.traceEntry("Finding reservation by route id {}", route_id);
        Connection con = dbUtils.getConnection();

        List<Reservation> reservations = new ArrayList<>();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from reservations where route_id = ?")) {

            preStmt.setLong(1, route_id);

            try (ResultSet resultSet = preStmt.executeQuery()) {

                Route route = route_repo.findById(route_id);

                while (resultSet.next()) {

                    Long id = resultSet.getLong("id");
                    int seat_no = resultSet.getInt("seat_no");
                    String client_name = resultSet.getString("client_name");

                    Reservation reservation = new Reservation(route, seat_no, client_name);
                    reservation.setId(id);

                    reservations.add(reservation);
                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD " + ex);
        }

        logger.traceExit(reservations);
        return reservations;
    }
}
