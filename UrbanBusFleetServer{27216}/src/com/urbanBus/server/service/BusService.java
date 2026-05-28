/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.Bus;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface BusService extends Remote{

    Bus saveBus(Bus bus) throws RemoteException;
    Bus updateBus(Bus bus) throws RemoteException;
    Bus deleteBus(Bus bus) throws RemoteException;
    Bus findBusById(Long id) throws RemoteException;
    Bus findBusByPlate(String plateNumber) throws RemoteException;
    List<Bus> findAllBuses() throws RemoteException;  
    
}
