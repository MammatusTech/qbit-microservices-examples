package com.mammatustech.hr;

import io.advantageous.boon.core.Sys;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a storage repo. Imagine this is talking to MongoDB or
 * Cassandra. Perhaps it is also indexing the department name via
 * SOLR. It does all of this and then returns when it is finished.
 * If this in turn called other services, it would use a Callback instead of
 * returning a boolean.
 */
public class DepartmentRepo {

    private final Map<Long, Department> departmentMap = new HashMap<>();


    /**
     * Add a department.
     * @param department department we are adding.
     * @return true if successfully stored the department
     */
    public boolean addDepartment(final Department department) {

        Sys.sleep(100_000);
        departmentMap.put(department.getId(), department);
        return true;
    }
}
