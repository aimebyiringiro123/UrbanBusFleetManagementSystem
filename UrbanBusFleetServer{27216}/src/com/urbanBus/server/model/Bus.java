/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author aimeb
 */
@Entity
@Table(name = "buses")
public class Bus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bus_id")
    private Long busId;

    @Column(name = "plate_number", unique = true, nullable = false)
    private String plateNumber;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "fuel_type", nullable = false)
    private String fuelType;

    @Column(name = "mileage", nullable = false)
    private double mileage;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private BusStatus status;
    
    @OneToMany(mappedBy = "bus", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Maintenance> maintenanceList = new ArrayList<>();
    
    private static final long serialVersionUID = 1L;


    // Constructors
    public Bus() {}

    public Bus(String plateNumber, String brand, int capacity,
               String fuelType, double mileage, BusStatus status) {
        this.plateNumber = plateNumber;
        this.brand = brand;
        this.capacity = capacity;
        this.fuelType = fuelType;
        this.mileage = mileage;
        this.status = status;
    }

    // Getters and Setters
    public Long getBusId() { return busId; }
    public void setBusId(Long busId) { this.busId = busId; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }

    public double getMileage() { return mileage; }
    public void setMileage(double mileage) { this.mileage = mileage; }

    public BusStatus getStatus() { return status; }
    public void setStatus(BusStatus status) { this.status = status; }


    @Override
    public String toString() {
        return plateNumber + " - " + brand;
    }
}

