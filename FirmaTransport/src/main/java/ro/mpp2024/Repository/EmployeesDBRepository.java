package ro.mpp2024.Repository;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import ro.mpp2024.Model.Employee;
import ro.mpp2024.Utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EmployeesDBRepository implements EmployeesRepository{

    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public EmployeesDBRepository(JdbcUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    @Override
    public Employee findByUsername(String username) {
        logger.traceEntry("Finding employee by username {}", username);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from employees where username = ?")) {

            preStmt.setString(1, username);

            try (ResultSet resultSet = preStmt.executeQuery()) {

                if (resultSet.next()) {

                    long id = resultSet.getLong("id");
                    String password = resultSet.getString("password");

                    Employee employee = new Employee(username, password);
                    employee.setId(id);

                    logger.traceExit(employee);
                    return employee;

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
    public void add(Employee employee) {

        logger.traceEntry("saving employee {}", employee);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "insert into employees (username, password) " +
                        "values (?, ?)")) {

            preStmt.setString(1, employee.getUsername());
            preStmt.setString(2, employee.getPassword());

            int result = preStmt.executeUpdate();
            logger.trace("Saving {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Employee employee) {

        logger.traceEntry("deleting employees {}", employee);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "delete from employees " +
                        "where id = ?")) {

            preStmt.setLong(1, employee.getId());

            int result = preStmt.executeUpdate();
            logger.trace("deleting {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();

    }

    @Override
    public void update(Employee employee, Long id) {

        logger.traceEntry("updating employee {}", employee);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "update employees " +
                        "set username = ?, password = ? " +
                        "where id = ?")) {

            preStmt.setString(1, employee.getUsername());
            preStmt.setString(2, employee.getPassword());
            preStmt.setLong(3, employee.getId());

            int result = preStmt.executeUpdate();
            logger.trace("Update {} instances", result);

        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error DB: " + ex);
        }
        logger.traceExit();

    }

    @Override
    public Employee findById(Long id) {
        logger.traceEntry("Finding employee by id {}", id);
        Connection con = dbUtils.getConnection();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from employees where id = ?")) {

            preStmt.setLong(1, id);

            try (ResultSet resultSet = preStmt.executeQuery()) {

                if (resultSet.next()) {

                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    Employee employee = new Employee(username, password);
                    employee.setId(id);

                    logger.traceExit(employee);
                    return employee;
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
    public Iterable<Employee> findAll() {
        logger.traceEntry("Finding all employees");
        Connection con = dbUtils.getConnection();

        List<Employee> employees = new ArrayList<>();

        try (PreparedStatement preStmt = con.prepareStatement(
                "select * from employees")) {

            try (ResultSet resultSet = preStmt.executeQuery()) {

                while (resultSet.next()) {

                    long id = resultSet.getLong("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");

                    Employee employee = new Employee(username, password);
                    employee.setId(id);

                    employees.add(employee);
                }
            }
        } catch (SQLException ex) {
            logger.error(ex);
            System.err.println("Error BD " + ex);
        }
        logger.traceExit(employees);
        return employees;
    }
}
