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
@Table(name = "routes")
public class Route implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "route_name", nullable = false)
    private String routeName;

    @Column(name = "start_point", nullable = false)
    private String startPoint;

    @Column(name = "end_point", nullable = false)
    private String endPoint;

    @Column(name = "distance_km", nullable = false)
    private double distanceKm;

    @Column(name = "estimated_time", nullable = false)
    private int estimatedTime;

    @Column(name = "fare", nullable = false)
    private double fare;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RouteStatus status;
    
    @OneToMany(mappedBy = "route", fetch = FetchType.EAGER)
    private List<Trip> trips = new ArrayList<>();
    
    private static final long serialVersionUID = 3L;    

    // Constructors
    public Route() {}

    public Route(String routeName, String startPoint, String endPoint,
                 double distanceKm, int estimatedTime, double fare, RouteStatus status) {
        this.routeName = routeName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.distanceKm = distanceKm;
        this.estimatedTime = estimatedTime;
        this.fare = fare;
        this.status = status;
    }

    // Getters and Setters
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }

    public String getRouteName() { return routeName; }
    public void setRouteName(String routeName) { this.routeName = routeName; }

    public String getStartPoint() { return startPoint; }
    public void setStartPoint(String startPoint) { this.startPoint = startPoint; }

    public String getEndPoint() { return endPoint; }
    public void setEndPoint(String endPoint) { this.endPoint = endPoint; }

    public double getDistanceKm() { return distanceKm; }
    public void setDistanceKm(double distanceKm) { this.distanceKm = distanceKm; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) {
        this.estimatedTime = estimatedTime;
    }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public RouteStatus getStatus() { return status; }
    public void setStatus(RouteStatus status) { this.status = status; }

    @Override
    public String toString() {
        return routeName + " (" + startPoint + " → " + endPoint + ")";
    }
}
