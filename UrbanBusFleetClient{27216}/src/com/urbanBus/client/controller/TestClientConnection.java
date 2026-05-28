/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.client.controller;

import com.urbanBus.server.service.BusService;

/**
 *
 * @author aimeb
 */
public class TestClientConnection {
    
     public static void main(String[] args) {
        try {
            BusService busService = RMIClient.getBusService();
            if (busService != null) {
                System.out.println("Client connected to server successfully!");
                System.out.println("BusService is ready to use!");
            } else {
                System.out.println("Could not get BusService.");
            }
        } catch (Exception e) {
            System.err.println("Connection test failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
}
