package com.borovyksv.sql.vehicle;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String model_make;
    private String model_name;

    public Vehicle(String model_make, String model_name) {
        this.model_make = model_make;
        this.model_name = model_name;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", model_make='" + model_make + '\'' +
                ", model_name='" + model_name + '\'' +
                '}';
    }

    public Vehicle() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getModel_make() {
        return model_make;
    }

    public void setModel_make(String model_make) {
        this.model_make = model_make;
    }

    public String getModel_name() {
        return model_name;
    }

    public void setModel_name(String model_name) {
        this.model_name = model_name;
    }

}
