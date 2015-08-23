package com.mammatustech.hr;


import io.advantageous.qbit.reactive.Callback;

/**
 * Async interface to DepartmentRepo internal service.
 *
 */
public interface DepartmentRepoAsync {

    /**
     * Add a department to the repo.
     * @param callback callback which returns the success code async.
     * @param department department to add
     */
     void addDepartment(final Callback<Boolean> callback,
                        final Department department);

}
