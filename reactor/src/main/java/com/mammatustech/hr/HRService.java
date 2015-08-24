package com.mammatustech.hr;



import io.advantageous.qbit.annotation.*;
import io.advantageous.qbit.reactive.Callback;
import io.advantageous.qbit.reactive.Reactor;

import java.util.*;
import java.util.concurrent.TimeoutException;

/** This is the public REST interface to the Human Resources services.
 *
 */
@RequestMapping("/hr")
public class HRService {

    private final Map<Integer, Department> departmentMap = new HashMap<>();

    private final Reactor reactor;
    private final DepartmentRepoAsync departmentRepoAsync;

    /**
     * Construct a new HR REST Service.
     * @param reactor reactor
     * @param departmentRepoAsync async interface to DepartmentStore
     */
    public HRService(final Reactor reactor, final DepartmentRepoAsync departmentRepoAsync) {
        this.reactor = reactor;
        this.reactor.addServiceToFlush(departmentRepoAsync);
        this.departmentRepoAsync = departmentRepoAsync;
    }

    /**
     * Add a new department
     * @param callback callback
     * @param departmentId department id
     * @param department department
     */
    @RequestMapping(value = "/department/{departmentId}/", method = RequestMethod.POST)
    public void addDepartment(final Callback<Boolean> callback, @PathVariable("departmentId") Integer departmentId,
                              final Department department) {

        final Callback<Boolean> repoCallback = reactor.callbackBuilder()
                .setCallback(Boolean.class, succeeded -> {
                    departmentMap.put(departmentId, department);
                    callback.accept(succeeded);
                }).setOnTimeout(() -> {
                    // callback.accept(false); One way.
                    // callback.onTimeout(); Another way
                    callback.onError(
                            new TimeoutException("Timeout can't add department " + departmentId));
                }).setOnError(error -> {
                    callback.onError(error);
                }).build();

        departmentRepoAsync.addDepartment(repoCallback, department);

    }

    /** Register to be notified when the service queue is idle, empty, or has hit its batch limit.
     */
    @QueueCallback({QueueCallbackType.EMPTY, QueueCallbackType.IDLE, QueueCallbackType.LIMIT})
    private void process () {

        /** Call the reactor to process callbacks. */
        reactor.process();
    }

    @RequestMapping("/department/")
    public List<Department> getDepartments() {
        return new ArrayList<>(departmentMap.values());
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
