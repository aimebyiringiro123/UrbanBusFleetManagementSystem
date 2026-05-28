/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.DriverDao;
import com.urbanBus.server.model.Driver;
import com.urbanBus.server.service.DriverService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aimeb
 */
public class DriverServiceImpl extends UnicastRemoteObject implements DriverService {

    DriverDao dao = new DriverDao();

    public DriverServiceImpl() throws RemoteException {
        
    }

    @Override
    public Driver saveDriver(Driver driver) throws RemoteException {       
        return dao.saveDriver(driver);
    }

    @Override
    public Driver updateDriver(Driver driver) throws RemoteException {
        return dao.updateDriver(driver);
    }

    @Override
    public Driver deleteDriver(Driver driver) throws RemoteException {
        return dao.deleteDriver(driver);
    }

    @Override
    public Driver findDriverById(Long id) throws RemoteException {
        return dao.findDriverById(id);
    }

    @Override
    public Driver findDriverByLicense(String licenseNumber) throws RemoteException {
        return dao.findDriverByLicense(licenseNumber);
    }

    @Override
    public List<Driver> findAllDrivers() throws RemoteException {
        return dao.findAllDrivers();
    }

    @Override
    public List<Driver> findAvailableDrivers() throws RemoteException {
        return dao.findAvailableDrivers();
    }
}
