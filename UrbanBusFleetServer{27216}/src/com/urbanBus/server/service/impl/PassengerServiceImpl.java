/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.PassengerDao;
import com.urbanBus.server.model.Passenger;
import com.urbanBus.server.service.PassengerService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aimeb
 */
public class PassengerServiceImpl extends UnicastRemoteObject implements PassengerService {

    PassengerDao dao = new PassengerDao();

    public PassengerServiceImpl() throws RemoteException {
    }

    @Override
    public Passenger savePassenger(Passenger passenger) throws RemoteException {       
        return dao.savePassenger(passenger);
    }

    @Override
    public Passenger updatePassenger(Passenger passenger) throws RemoteException {      
        return dao.updatePassenger(passenger);
    }

    @Override
    public Passenger deletePassenger(Passenger passenger) throws RemoteException {
        return dao.deletePassenger(passenger);
    }

    @Override
    public Passenger findPassengerById(Long id) throws RemoteException {
        return dao.findPassengerById(id);
    }

    @Override
    public Passenger findPassengerByPhone(String phone) throws RemoteException {
        return dao.findPassengerByPhone(phone);
    }

    @Override
    public List<Passenger> findAllPassengers() throws RemoteException {
        return dao.findAllPassengers();
    }
}
