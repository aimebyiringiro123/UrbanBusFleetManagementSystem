/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

/**
 *
 * @author aimeb
 */
@Entity
@Table(name = "maintenance")
public class Maintenance implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Long maintenanceId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_id", nullable = false)
    private Bus bus;

    @Temporal(TemporalType.DATE)
    @Column(name = "service_date", nullable = false)
    private Date serviceDate;

    @Column(name = "service_type", nullable = false)
    private String serviceType;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "technician_name", nullable = false)
    private String technicianName;

    @Column(name = "next_service_mileage")
    private double nextServiceMileage;

    @Column(name = "notes")
    private String notes;

    @Column(name = "completed")
    private boolean completed;
    
    private static final long serialVersionUID = 7L;

    // Constructors
    public Maintenance() {}

    public Maintenance(Bus bus, Date serviceDate, String serviceType,
                       double cost, String technicianName,
                       double nextServiceMileage, String notes) {
        this.bus = bus;
        this.serviceDate = serviceDate;
        this.serviceType = serviceType;
        this.cost = cost;
        this.technicianName = technicianName;
        this.nextServiceMileage = nextServiceMileage;
        this.notes = notes;
        this.completed = false;
    }

    // Getters and Setters
    public Long getMaintenanceId() { return maintenanceId; }
    public void setMaintenanceId(Long maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public Bus getBus() { return bus; }
    public void setBus(Bus bus) { this.bus = bus; }

    public Date getServiceDate() { return serviceDate; }
    public void setServiceDate(Date serviceDate) { this.serviceDate = serviceDate; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public String getTechnicianName() { return technicianName; }
    public void setTechnicianName(String technicianName) {
        this.technicianName = technicianName;
    }

    public double getNextServiceMileage() { return nextServiceMileage; }
    public void setNextServiceMileage(double nextServiceMileage) {
        this.nextServiceMileage = nextServiceMileage;
    }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    @Override
    public String toString() {
        return serviceType;
    }
}