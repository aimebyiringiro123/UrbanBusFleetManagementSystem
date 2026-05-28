/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.DriverProfile;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author aimeb
 */
public interface DriverProfileService   extends Remote {

    DriverProfile saveProfile(
        DriverProfile profile)
        throws RemoteException;

    DriverProfile updateProfile(
        DriverProfile profile)
        throws RemoteException;

    DriverProfile findByDriverId(Long driverId)
        throws RemoteException;
}
