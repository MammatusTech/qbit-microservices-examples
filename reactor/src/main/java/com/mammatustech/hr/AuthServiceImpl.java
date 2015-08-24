package com.mammatustech.hr;

import io.advantageous.qbit.reactive.Callback;

public class AuthServiceImpl implements AuthService {

    public void allowedToAddDepartment(final Callback<Boolean> callback,
                                       final String username,
                                       final int departmentId) {

        /* We don't do actual auth. */
        callback.accept(true);

    }

}
