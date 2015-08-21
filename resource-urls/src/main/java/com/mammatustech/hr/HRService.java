package com.mammatustech.hr;



import io.advantageous.qbit.annotation.*;

import java.util.*;

@RequestMapping("/hr")
public class HRService {

    final Map<Integer, Department> departmentMap = new HashMap<>();


    @RequestMapping("/department/")
    public List<Department> getDepartments() {
        return new ArrayList<>(departmentMap.values());
    }

    @RequestMapping(value = "/department/{departmentId}/", method = RequestMethod.POST)
    public boolean addDepartment(@PathVariable("departmentId") Integer departmentId,
                                  final Department department) {

        departmentMap.put(departmentId, department);
        return true;
    }

    @RequestMapping(value = "/department/{departmentId}/employee/", method = RequestMethod.POST)
    public boolean addEmployee(@PathVariable("departmentId") Integer departmentId,
                               final Employee employee) {

        final Department department = departmentMap.get(departmentId);

        if (department ==  null) {
            throw new IllegalArgumentException("Department " + departmentId + " does not exist");
        }

        department.addEmployee(employee);
        return true;
    }

    @RequestMapping(value = "/department/{departmentId}/employee/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployee(@PathVariable("departmentId") Integer departmentId,
                                @PathVariable("employeeId") Long employeeId) {

        final Department department = departmentMap.get(departmentId);

        if (department ==  null) {
            throw new IllegalArgumentException("Department " + departmentId + " does not exist");
        }

        Optional<Employee> employee = department.getEmployeeList().stream().filter(
                employee1 -> employee1.getId() == employeeId).findFirst();

        if (employee.isPresent()){
            return employee.get();
        } else {
            throw new IllegalArgumentException("Employee with id " + employeeId + " Not found ");
        }
    }


    @RequestMapping(value = "/department/{departmentId}/employee/{employeeId}/phoneNumber/",
            method = RequestMethod.POST)
    public boolean addPhoneNumber(@PathVariable("departmentId") Integer departmentId,
                                  @PathVariable("employeeId") Long employeeId,
                                  PhoneNumber phoneNumber) {

        Employee employee = getEmployee(departmentId, employeeId);
        employee.addPhoneNumber(phoneNumber);
        return true;
    }



    @RequestMapping(value = "/department/{departmentId}/employee/{employeeId}/phoneNumber/")
    public List<PhoneNumber> getPhoneNumbers(@PathVariable("departmentId") Integer departmentId,
                                             @PathVariable("employeeId") Long employeeId) {

        Employee employee = getEmployee(departmentId, employeeId);
        return employee.getPhoneNumbers();
    }


    @RequestMapping(value = "/kitchen/{departmentId}/employee/phoneNumber/kitchen/",
            method = RequestMethod.POST)
    public boolean addPhoneNumberKitchenSink(@PathVariable("departmentId") Integer departmentId,
                                  @RequestParam("employeeId") Long employeeId,
                                  @HeaderParam("X-PASS-CODE") String passCode,
                                             PhoneNumber phoneNumber) {

        if ("passcode".equals(passCode)) {
            Employee employee = getEmployee(departmentId, employeeId);
            employee.addPhoneNumber(phoneNumber);
            return true;
        } else {
            return false;
        }
    }



}
