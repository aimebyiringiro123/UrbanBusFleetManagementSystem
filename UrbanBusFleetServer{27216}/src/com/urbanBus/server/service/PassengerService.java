/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.Passenger;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface PassengerService extends Remote{
    Passenger savePassenger(Passenger passenger) throws RemoteException;
    Passenger updatePassenger(Passenger passenger) throws RemoteException;
    Passenger deletePassenger(Passenger passenger) throws RemoteException;
    Passenger findPassengerById(Long id) throws RemoteException;
    Passenger findPassengerByPhone(String phone) throws RemoteException;
    List<Passenger> findAllPassengers() throws RemoteException;
}
