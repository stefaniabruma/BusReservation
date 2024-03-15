package ro.mpp2024.Repository;

import ro.mpp2024.Model.Employee;

public interface EmployeesRepository extends Repository<Employee, Long> {
    public Employee findByUsername(String username);
}
