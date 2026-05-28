/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.TripDao;
import com.urbanBus.server.model.Trip;
import com.urbanBus.server.model.TripStatus;
import com.urbanBus.server.notification.NotificationProducer;
import com.urbanBus.server.service.TripService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aimeb
 */
public class TripServiceImpl extends UnicastRemoteObject implements TripService {

    TripDao dao = new TripDao();

    public TripServiceImpl() throws RemoteException {
    }

    @Override
public Trip saveTrip(Trip trip)
        throws RemoteException {

    List<Trip> existingTrips =
        dao.findAllTrips();

    for (Trip existing : existingTrips) {

        java.util.Date newDep =
            trip.getDepartureTime();
        java.util.Date exDep =
            existing.getDepartureTime();
        java.util.Date exArr =
            existing.getArrivalTime();

        boolean activeTrip =
            existing.getStatus() !=
                TripStatus.CANCELLED
            && existing.getStatus() !=
                TripStatus.COMPLETED;

        boolean timeConflict = false;
        if (exArr != null) {
            timeConflict =
                (newDep.equals(exDep)) ||
                (newDep.after(exDep) &&
                    newDep.before(exArr));
        } else {
            timeConflict = newDep.equals(exDep);
        }

      
        if (activeTrip && timeConflict &&
                existing.getBus().getBusId()
                .equals(trip.getBus()
                    .getBusId())) {
            throw new RemoteException(
                " Bus "
                + trip.getBus().getPlateNumber()
                + " is already scheduled "
                + "for another trip at this time.");
        }

        
        if (activeTrip && timeConflict &&
                existing.getDriver().getDriverId()
                .equals(trip.getDriver()
                    .getDriverId())) {
            throw new RemoteException(
                " Driver "
                + trip.getDriver().getFullName()
                + "is already scheduled "
                + "for another trip at this time.");
        }
    }

    return dao.saveTrip(trip);
}

    @Override
    public Trip updateTrip(Trip trip) throws RemoteException {
        Trip updated = dao.updateTrip(trip);
    
        if (trip.getStatus() == TripStatus.COMPLETED) {
        NotificationProducer.sendTripCompletedAlert(
            "aimeb909@gmail.com",                                 
            trip.getRoute().getRouteName(),                                    
            trip.getDriver().getFirstName() + " " + trip.getDriver().getLastName(), 
            trip.getBus().getPlateNumber()                                     
        );
    }

    return updated;
        
        
    }

    @Override
    public Trip deleteTrip(Trip trip) throws RemoteException {
        return dao.deleteTrip(trip);
    }

    @Override
    public Trip findTripById(Long id) throws RemoteException {
        return dao.findTripById(id);
    }

    @Override
    public List<Trip> findAllTrips() throws RemoteException {
        return dao.findAllTrips();
    }

    @Override
    public List<Trip> findTripsByStatus(String status) throws RemoteException {
        return dao.findTripsByStatus(status);
    }
}
