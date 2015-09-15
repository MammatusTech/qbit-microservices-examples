package com.mammatustech.hr;

import io.advantageous.qbit.annotation.Description;

import java.util.ArrayList;
import java.util.List;



@Description("A department")
public class Department {


    @Description("department id")
    private final long id;

    @Description("employees in department")
    private final List<Employee> employeeList;

    public Department(long id, List<Employee> employeeList) {
        this.id = id;
        this.employeeList = employeeList;
    }

    public void addEmployee(Employee employee) {
        employeeList.add(employee);
    }

    public List<Employee> getEmployeeList() {
        return new ArrayList<>(employeeList);
    }

    public long getId() {
        return id;
    }
}
