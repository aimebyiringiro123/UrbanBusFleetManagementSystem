/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.Trip;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface TripService extends Remote{
    Trip saveTrip(Trip trip) throws RemoteException;
    Trip updateTrip(Trip trip) throws RemoteException;
    Trip deleteTrip(Trip trip) throws RemoteException;
    Trip findTripById(Long id) throws RemoteException;
    List<Trip> findAllTrips() throws RemoteException;
    List<Trip> findTripsByStatus(String status) throws RemoteException;
}
