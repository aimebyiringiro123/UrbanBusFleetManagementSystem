/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.BusDao;
import com.urbanBus.server.model.Bus;
import com.urbanBus.server.service.BusService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aimeb
 */
public class BusServiceImpl extends UnicastRemoteObject implements BusService{
    
    BusDao dao = new BusDao();
    
    public BusServiceImpl() throws RemoteException {
    }

   @Override
    public Bus saveBus(Bus bus) throws RemoteException {
        
        if (bus.getCapacity() <= 0) {
            throw new RemoteException(
                "Bus capacity must be greater than zero.");
        }
        
        if (bus.getMileage() < 0) {
            throw new RemoteException(
                "Bus mileage cannot be negative.");
        }
        return dao.saveBus(bus);
    }

    @Override
    public Bus updateBus(Bus bus) throws RemoteException {
        
        if (bus.getCapacity() <= 0) {
            throw new RemoteException(
                "Bus capacity must be greater than zero.");
        }
        
        if (bus.getMileage() < 0) {
            throw new RemoteException(
                "Bus mileage cannot be negative.");
        }
        return dao.updateBus(bus);
    }

    @Override
    public Bus deleteBus(Bus bus) throws RemoteException {
        return dao.deleteBus(bus);
    }

    @Override
    public Bus findBusById(Long id) throws RemoteException {
        return dao.findBusById(id);
    }

    @Override
    public Bus findBusByPlate(String plateNumber) throws RemoteException {
        return dao.findBusByPlate(plateNumber);
    }

    @Override
    public List<Bus> findAllBuses() throws RemoteException {
        return dao.findAllBuses();
    }
    
}
