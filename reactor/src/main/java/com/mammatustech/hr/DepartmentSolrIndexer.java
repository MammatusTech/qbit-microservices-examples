package com.mammatustech.hr;

import java.util.HashMap;
import java.util.Map;


/**
 * Represents a storage indexer. Imagine this is talking to SOLR.
 * Perhaps it is also indexing the department name via
 * SOLR. It does all of this and then returns when it is finished.
 * If this in turn called other services, it would use a Callback instead of
 * returning a boolean.
 */
public class DepartmentSolrIndexer {

    private final Map<Long, Department> departmentMap = new HashMap<>();


    /**
     * Add a department.
     *
     * @param department department we are adding.
     * @return true if successfully stored the department
     */
    public boolean addDepartment(final Department department) {
        departmentMap.put(department.getId(), department);
        return true;
    }
}

