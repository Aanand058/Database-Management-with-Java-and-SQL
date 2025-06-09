package com.example;

public class Person {
    private String name;
    private String city;
    private String zipcode;

    public Person(String name, String city, String zipcode) {
        this.name = name;
        this.city = city;
        this.zipcode = zipcode;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }
}
