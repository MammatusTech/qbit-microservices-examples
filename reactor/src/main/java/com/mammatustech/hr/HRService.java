package com.mammatustech.hr;



import io.advantageous.qbit.annotation.*;
import io.advantageous.qbit.reactive.Callback;
import io.advantageous.qbit.reactive.CallbackBuilder;
import io.advantageous.qbit.reactive.Reactor;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/** This is the public REST interface to the Human Resources services.
 *
 */
@RequestMapping("/hr")
public class HRService {

    private final Map<Integer, Department> departmentMap = new HashMap<>();

    private final Reactor reactor;
    private final DepartmentRepoAsync solrIndexer;
    private final DepartmentRepoAsync cassandraStore;
    private final AuthService authService;

    /**
     * Construct a new HR REST Service.
     * @param reactor reactor
     * @param cassandraStore async interface to DepartmentStore
     * @param solrIndexer async interface to SOLR Service
     */
    public HRService(final Reactor reactor,
                     final DepartmentRepoAsync cassandraStore,
                     final DepartmentRepoAsync solrIndexer,
                     final AuthService authService) {
        this.reactor = reactor;
        this.reactor.addServiceToFlush(cassandraStore);
        this.reactor.addServiceToFlush(solrIndexer);
        this.reactor.addServiceToFlush(authService);
        this.cassandraStore = cassandraStore;
        this.solrIndexer = solrIndexer;
        this.authService = authService;

        this.reactor.addRepeatingTask(1, TimeUnit.SECONDS, () -> {
            manageCache();
        });
    }

    private void manageCache() {
        System.out.println("Manage Cache");
    }

    /**
     * Add a new department
     * @param clientCallback callback
     * @param departmentId department id
     * @param department department
     */
    @RequestMapping(value = "/department/{departmentId}/", method = RequestMethod.POST)
    public void addDepartment(final Callback<Boolean> clientCallback,
                              @PathVariable("departmentId") Integer departmentId,
                              final Department department,
                              @HeaderParam(value="username", defaultValue = "noAuth")
                                  final String userName) {

        final CallbackBuilder callbackBuilder = reactor.callbackBuilder()
                .setOnTimeout(() -> {
                    clientCallback.onError(
                            new TimeoutException("Timeout can't add department " + departmentId));
                }).setOnError(clientCallback::onError)
                .setTimeoutDuration(200)
                .setTimeoutTimeUnit(TimeUnit.MILLISECONDS);


        authService.allowedToAddDepartment(callbackBuilder.setCallback(Boolean.class, allowed -> {
            if (allowed) {
                doAddDepartment(clientCallback, callbackBuilder, department);
            } else {
                clientCallback.onError(new SecurityException("Go away!"));
            }
        }).build(), userName,  departmentId);


    }

    private void doAddDepartment(final Callback<Boolean> clientCallback,
                                 final CallbackBuilder callbackBuilder,
                                 final Department department) {

        final Callback<Boolean> callbackDeptRepo = callbackBuilder.setCallback(Boolean.class, addedDepartment -> {

            departmentMap.put((int)department.getId(), department);
            clientCallback.accept(addedDepartment);

            solrIndexer.addDepartment(indexedOk -> {
            }, department);
        }).build();

        cassandraStore.addDepartment(callbackDeptRepo, department);

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
