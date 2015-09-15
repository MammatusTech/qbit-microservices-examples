package com.mammatustech.hr;

import io.advantageous.qbit.annotation.Description;

import java.util.ArrayList;
import java.util.List;



@Description("An employee")
public class Employee {


    @Description("An id")
    private final long id;

    @Description("A name")
    private final String name;


    @Description("A list of phone numbers")
    private List<PhoneNumber> phoneNumbers;


    public Employee(long id, String name, List<PhoneNumber> phoneNumbers) {
        this.id = id;
        this.name = name;
        this.phoneNumbers = phoneNumbers;
    }



    public Employee(long id, String name) {
        this.id = id;
        this.name = name;
        this.phoneNumbers = new ArrayList<>();
    }


    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        if (this.phoneNumbers == null) {
            phoneNumbers = new ArrayList<>();
        }
        phoneNumbers.add(phoneNumber);
    }


    public List<PhoneNumber> getPhoneNumbers() {
        return new ArrayList<>(phoneNumbers);
    }
}

