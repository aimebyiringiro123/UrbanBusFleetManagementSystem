/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.Driver;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface DriverService extends Remote{
    
    Driver saveDriver(Driver driver) throws RemoteException;
    Driver updateDriver(Driver driver) throws RemoteException;
    Driver deleteDriver(Driver driver) throws RemoteException;
    Driver findDriverById(Long id) throws RemoteException;
    Driver findDriverByLicense(String licenseNumber) throws RemoteException;
    List<Driver> findAllDrivers() throws RemoteException;
    List<Driver> findAvailableDrivers() throws RemoteException;
    
}
