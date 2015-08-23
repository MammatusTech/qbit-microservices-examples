package io.example;

import io.swagger.client.api.DefaultApi;
import io.swagger.client.model.Department;
import io.swagger.client.model.Employee;
import io.swagger.client.model.PhoneNumber;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Main {

    public static void main(String... args) throws Exception {
        DefaultApi defaultApi = new DefaultApi();

        Department department = new Department();
        department.setId(1L);


        Employee rick = new Employee();
        rick.setName("Rick");
        rick.setId(1L);

        Employee diana = new Employee();
        diana.setName("Diana");
        diana.setId(2L);

        List<Employee> employeeList = new ArrayList<Employee>();

        employeeList.add(rick);
        employeeList.add(diana);

        department.setEmployeeList(employeeList);


        boolean success = defaultApi.addDepartment(1, department);

        System.out.println("Added department");


        List<Department> departments = defaultApi.getDepartments();

        System.out.println(departments);

        if (success) {

            Employee noah = new Employee();
            noah.setName("Noah");
            noah.setId(3L);

            defaultApi.addEmployee(1, noah);


            PhoneNumber phoneNumber = new PhoneNumber();
            phoneNumber.setPhoneNumber("555-1212");
            defaultApi.addPhoneNumber(1, 3, phoneNumber);

            System.out.println("Added phone number");



            PhoneNumber pn = new PhoneNumber();
            pn.setPhoneNumber("408-555-1213");

            defaultApi.addPhoneNumberKitchenSink(1, pn, 3, "passcode");

            System.out.println("Added phone number with passcode");


            departments = defaultApi.getDepartments();

            departments.forEach(new Consumer<Department>() {
                @Override
                public void accept(Department department) {
                    department.getEmployeeList().forEach(new Consumer<Employee>() {
                        @Override
                        public void accept(Employee employee) {
                            System.out.println(employee);
                        }
                    });
                }
            });
        } else {

            System.out.println("Unable to handle add department");
        }
    }

}
