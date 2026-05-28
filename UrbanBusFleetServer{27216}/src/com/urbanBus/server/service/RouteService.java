/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.Route;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface RouteService extends Remote {
    
    Route saveRoute(Route route) throws RemoteException;
    Route updateRoute(Route route) throws RemoteException;
    Route deleteRoute(Route route) throws RemoteException;
    Route findRouteById(Long id) throws RemoteException;
    List<Route> findAllRoutes() throws RemoteException;
    List<Route> findActiveRoutes() throws RemoteException;
}
