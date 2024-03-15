package ro.mpp2024.Repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.mpp2024.Model.Route;
import ro.mpp2024.Utils.JdbcUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoutesDBRepository implements RoutesRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public RoutesDBRepository(JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public void add(Route route) {
        logger.traceEntry("saving task {}", route);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "insert into routes (destination, departure_time, no_available_seats, no_total_seats) " +
                        "values (?, ?, ?, ?)")) {

            preStmt.setString(1, route.getDestination());
            preStmt.setTimestamp(2, Timestamp.valueOf(route.getDeparture_time()));
            preStmt.setInt(3, route.getNo_available_seats());
            preStmt.setInt(4, route.getNo_total_seats());
            
            int result = preStmt.executeUpdate();
            logger.trace("Saving {} instances", result);

    } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Route route) {

        logger.traceEntry("saving task {}", route);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "delete from routes " +
                        "where id = ?")) {

            preStmt.setLong(1, route.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Saving {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void update(Route route, Long id) {

        logger.traceEntry("update task {}", route);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "update routes " +
                        "set destination = ?, departure_time = ?, no_available_seats = ?, no_total_seats = ? " +
                        "where id = ?")) {

            preStmt.setString(1, route.getDestination());
            preStmt.setTimestamp(2, Timestamp.valueOf(route.getDeparture_time()));
            preStmt.setInt(3, route.getNo_available_seats());
            preStmt.setInt(4, route.getNo_total_seats());
            preStmt.setLong(5, route.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Update {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();

    }

    @Override
    public Route findById(Long id) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from routes where id = ?")) {

            preStmt.setLong(1, id);

            try (ResultSet resultSet = preStmt.executeQuery()) {

                if (resultSet.next()) {

                    String destination = resultSet.getString("destination");
                    LocalDateTime dep_time = resultSet.getTimestamp("departure_time").toLocalDateTime();
                    int no_available_seats = resultSet.getInt("no_available_seats");
                    int no_total_seats = resultSet.getInt("no_total_seats");

                    Route route = new Route(destination, dep_time, no_available_seats, no_total_seats);
                    route.setId(id);

                    logger.traceExit(route);
                    return route;
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
    public Iterable<Route> findAll() {

        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        List<Route> routes = new ArrayList<>();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from routes")) {

            try (ResultSet resultSet = preStmt.executeQuery()) {

                while (resultSet.next()) {

                    long id = resultSet.getLong("id");
                    String destination = resultSet.getString("destination");
                    LocalDateTime dep_time = resultSet.getTimestamp("departure_time").toLocalDateTime();
                    int no_available_seats = resultSet.getInt("no_available_seats");
                    int no_total_seats = resultSet.getInt("no_total_seats");

                    Route route = new Route(destination, dep_time, no_available_seats, no_total_seats);
                    route.setId(id);

                    routes.add(route);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD " + ex);
        }
        logger.traceExit(routes);
        return routes;
    }

    @Override
    public Route findRouteByDestinationDateTime(String destination, LocalDateTime dateTime) {
        logger.traceEntry();
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from routes where destination = ? and departure_time = ?")) {

            preStmt.setString(1, destination);
            preStmt.setTimestamp(2, Timestamp.valueOf(dateTime));

            try (ResultSet resultSet = preStmt.executeQuery()) {

                if (resultSet.next()) {

                    long id = resultSet.getLong("id");
                    int no_available_seats = resultSet.getInt("no_available_seats");
                    int no_total_seats = resultSet.getInt("no_total_seats");

                    Route route = new Route(destination, dateTime, no_available_seats,  no_total_seats);
                    route.setId(id);

                    logger.traceExit(route);
                    return route;

                }
            }

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD " + ex);
        }

        logger.traceExit(null);
        return null;
    }
}
