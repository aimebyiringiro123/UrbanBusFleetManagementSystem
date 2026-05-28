/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 *
 * @author aimeb
 */
@Entity
@Table(name = "drivers")
public class Driver implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "driver_id")
    private Long driverId;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "license_number", unique = true, nullable = false)
    private String licenseNumber;

    @Temporal(TemporalType.DATE)
    @Column(name = "license_expiry", nullable = false)
    private Date licenseExpiry;

    @Column(name = "phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "hours_worked_today")
    private double hoursWorkedToday;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DriverStatus status;
    
    @OneToMany(mappedBy = "driver", fetch = FetchType.EAGER)
    private List<Trip> trips = new ArrayList<>();
    
    private static final long serialVersionUID = 2L;
    
    @OneToOne(mappedBy = "driver",
    cascade = CascadeType.ALL,
    fetch = FetchType.EAGER)
    private DriverProfile profile;

    public DriverProfile getProfile() {
      return profile;
    }
    public void setProfile(DriverProfile profile) {
      this.profile = profile;
    }

    // Constructors
    public Driver() {}

    public Driver(String firstName, String lastName, String licenseNumber,
                  Date licenseExpiry, String phone, DriverStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.licenseNumber = licenseNumber;
        this.licenseExpiry = licenseExpiry;
        this.phone = phone;
        this.hoursWorkedToday = 0;
        this.status = status;
    }

    // Getters and Setters
    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Date getLicenseExpiry() { return licenseExpiry; }
    public void setLicenseExpiry(Date licenseExpiry) {
        this.licenseExpiry = licenseExpiry;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public double getHoursWorkedToday() { return hoursWorkedToday; }
    public void setHoursWorkedToday(double hoursWorkedToday) {
        this.hoursWorkedToday = hoursWorkedToday;
    }

    public DriverStatus getStatus() { return status; }
    public void setStatus(DriverStatus status) { this.status = status; }

    public boolean isLicenseValid() {
        return licenseExpiry != null && licenseExpiry.after(new Date());
    }

    public boolean isAvailable() {
        return status == DriverStatus.AVAILABLE
                && hoursWorkedToday < 8
                && isLicenseValid();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}
