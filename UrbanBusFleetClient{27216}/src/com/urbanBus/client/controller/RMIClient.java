/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.controller;

import com.urbanBus.server.service.TicketService;
import com.urbanBus.server.service.DriverService;
import com.urbanBus.server.service.TripService;
import com.urbanBus.server.service.MaintenanceService;
import com.urbanBus.server.service.PassengerService;
import com.urbanBus.server.service.BusService;
import com.urbanBus.server.service.DriverProfileService;
import com.urbanBus.server.service.RouteService;
import com.urbanBus.server.service.UserService;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 *
 * @author aimeb
 */
public class RMIClient {
    
    
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 4000;

    private static Registry registry;

    // Connect to server
    private static Registry getRegistry() {
        try {
            if (registry == null) {
                registry = LocateRegistry.getRegistry(HOST, PORT);
                System.out.println("Connected to server on port " + PORT);
            }
            return registry;
        } catch (Exception e) {
            System.err.println("Failed to connect to server: " + e.getMessage());
            return null;
        }
    }

    // Get each service
    public static BusService getBusService() {
        try {
            return (BusService) getRegistry().lookup("bus-service");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static DriverService getDriverService() {
        try {
            return (DriverService) getRegistry().lookup("driver-service");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static RouteService getRouteService() {
        try {
            return (RouteService) getRegistry().lookup("route-service");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TripService getTripService() {
        try {
            return (TripService) getRegistry().lookup("trip-service");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static PassengerService getPassengerService() {
        try {
            return (PassengerService) getRegistry().lookup("passenger-service");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static TicketService getTicketService() {
        try {
            return (TicketService) getRegistry().lookup("ticket-service");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static MaintenanceService getMaintenanceService() {
        try {
            return (MaintenanceService) getRegistry().lookup("maintenance-service");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static UserService getUserService() {
    try {
        return (UserService) getRegistry().lookup("user-service");
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
}
    
    public static DriverProfileService getDriverProfileService() {
    try {
        return (DriverProfileService)
            getRegistry().lookup(
                "driver-profile-service");
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    }
    }
}
        
    
    

