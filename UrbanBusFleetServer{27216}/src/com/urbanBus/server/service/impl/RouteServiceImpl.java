/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.RouteDao;
import com.urbanBus.server.model.Route;
import com.urbanBus.server.service.RouteService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aimeb
 */
public class RouteServiceImpl extends UnicastRemoteObject implements RouteService {

    RouteDao dao = new RouteDao();

    public RouteServiceImpl() throws RemoteException {
    }

    @Override
    public Route saveRoute(Route route) throws RemoteException {     
        return dao.saveRoute(route);
    }

    @Override
    public Route updateRoute(Route route) throws RemoteException {
        return dao.updateRoute(route);
    }

    @Override
    public Route deleteRoute(Route route) throws RemoteException {
        return dao.deleteRoute(route);
    }

    @Override
    public Route findRouteById(Long id) throws RemoteException {
        return dao.findRouteById(id);
    }

    @Override
    public List<Route> findAllRoutes() throws RemoteException {
        return dao.findAllRoutes();
    }

    @Override
    public List<Route> findActiveRoutes() throws RemoteException {
        return dao.findActiveRoutes();
    }
}