/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.DriverProfileDao;
import com.urbanBus.server.model.DriverProfile;
import com.urbanBus.server.service.DriverProfileService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 *
 * @author aimeb
 */
public class DriverProfileServiceImpl
        extends UnicastRemoteObject
        implements DriverProfileService {

    DriverProfileDao dao = new DriverProfileDao();

    public DriverProfileServiceImpl()
            throws RemoteException {}

    @Override
    public DriverProfile saveProfile(
            DriverProfile profile)
            throws RemoteException {
        return dao.saveProfile(profile);
    }

    @Override
    public DriverProfile updateProfile(
            DriverProfile profile)
            throws RemoteException {
        return dao.updateProfile(profile);
    }

    @Override
    public DriverProfile findByDriverId(
            Long driverId)
            throws RemoteException {
        return dao.findByDriverId(driverId);
    }
}