package ro.mpp2024;

import ro.mpp2024.Repository.EmployeesDBRepository;
import ro.mpp2024.Repository.EmployeesRepository;
import ro.mpp2024.Repository.RoutesDBRepository;
import ro.mpp2024.Repository.RoutesRepository;
import ro.mpp2024.Utils.JdbcUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {

        Properties props=new Properties();
        try {
            props.load(new FileReader("db.config"));
        } catch (IOException e) {
            System.out.println("Cannot find bd.config "+e);
        }

        EmployeesRepository er = new EmployeesDBRepository(new JdbcUtils(props));
        RoutesRepository rr = new RoutesDBRepository(new JdbcUtils(props));

        er.findAll().forEach(System.out::println);
        rr.findAll().forEach(System.out::println);
    }
}