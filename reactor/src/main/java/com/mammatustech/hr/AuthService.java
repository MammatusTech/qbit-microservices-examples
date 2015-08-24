package com.mammatustech.hr;

import io.advantageous.qbit.reactive.Callback;

public interface AuthService {

    void allowedToAddDepartment(Callback<Boolean> callback,
                                String username,
                                int departmentId);

}
