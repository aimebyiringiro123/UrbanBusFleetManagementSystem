/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 *
 * @author aimeb
 */
@Entity
@Table(name = "driver_profiles")
public class DriverProfile implements Serializable {

    private static final long serialVersionUID = 9L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long profileId;

    @OneToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @Column(name = "address")
    private String address;

    @Column(name = "emergency_contact")
    private String emergencyContact;

    @Column(name = "blood_type")
    private String bloodType;

    @Column(name = "years_experience")
    private int yearsExperience;

    // Constructors
    public DriverProfile() {}

    // Getters and Setters
    public Long getProfileId() { return profileId; }
    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public String getAddress() { return address; }
    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }
    public void setEmergencyContact(
            String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getBloodType() {
        return bloodType;
    }
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public int getYearsExperience() {
        return yearsExperience;
    }
    public void setYearsExperience(
            int yearsExperience) {
        this.yearsExperience = yearsExperience;
    }
}