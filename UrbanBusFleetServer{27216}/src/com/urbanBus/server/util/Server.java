/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.util;

import com.urbanBus.server.service.impl.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import com.urbanBus.server.notification.NotificationConsumer;

/**
 *
 * @author aimeb
 */
public class Server {

    public static void main(String[] args) {
        try {
            // Step 1 - Set server hostname
            System.setProperty("java.rmi.server.hostname", "127.0.0.1");

            // Step 2 - Create RMI registry on port 4000
            Registry registry = LocateRegistry.createRegistry(4000);

            // Step 3 - Register all services
            registry.rebind("bus-service",         new BusServiceImpl());
            registry.rebind("driver-service",      new DriverServiceImpl());
            registry.rebind("route-service",       new RouteServiceImpl());
            registry.rebind("trip-service",        new TripServiceImpl());
            registry.rebind("passenger-service",   new PassengerServiceImpl());
            registry.rebind("ticket-service",      new TicketServiceImpl());
            registry.rebind("maintenance-service", new MaintenanceServiceImpl());
            registry.rebind("user-service", new UserServiceImpl());
            registry.rebind("driver-profile-service", new DriverProfileServiceImpl());
            
            NotificationConsumer consumer = new NotificationConsumer();
            Thread consumerThread = new Thread(consumer);
            consumerThread.setDaemon(true);
            consumerThread.start();
            System.out.println("notification-consumer started");

            System.out.println("========================================");
            System.out.println("Urban Bus Fleet Server Started!");
            System.out.println("Running on port: 4000");
            System.out.println("Services registered:");
            System.out.println("bus-service");
            System.out.println("driver-service");
            System.out.println("route-service");
            System.out.println("trip-service");
            System.out.println("passenger-service");
            System.out.println("ticket-service");
            System.out.println("maintenance-service");
            System.out.println("user-service");
            System.out.println("driver-profile-service");
            System.out.println("========================================");
            System.out.println("  Waiting for client connections...");

        } catch (Exception e) {
            System.err.println("Server failed to start: " + e.getMessage());
            e.printStackTrace();
        }
    }
}