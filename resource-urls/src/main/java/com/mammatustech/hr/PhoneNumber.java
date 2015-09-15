package com.mammatustech.hr;


import io.advantageous.qbit.annotation.Description;

@Description("A phone number")
public class PhoneNumber {


    @Description("A phone number string")
    private final String phoneNumber;

    public  PhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }
}
