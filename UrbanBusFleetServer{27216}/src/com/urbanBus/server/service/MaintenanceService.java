/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.Maintenance;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface MaintenanceService extends Remote{
    Maintenance saveMaintenance(Maintenance maintenance) throws RemoteException;
    Maintenance updateMaintenance(Maintenance maintenance) throws RemoteException;
    Maintenance deleteMaintenance(Maintenance maintenance) throws RemoteException;
    Maintenance findMaintenanceById(Long id) throws RemoteException;
    List<Maintenance> findAllMaintenance() throws RemoteException;
    List<Maintenance> findMaintenanceByBus(Long busId) throws RemoteException;
}
