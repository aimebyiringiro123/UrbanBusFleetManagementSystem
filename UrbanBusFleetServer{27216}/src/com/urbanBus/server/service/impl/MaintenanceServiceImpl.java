/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.BusDao;
import com.urbanBus.server.dao.MaintenanceDao;
import com.urbanBus.server.model.Bus;
import com.urbanBus.server.model.BusStatus;
import com.urbanBus.server.model.Maintenance;
import com.urbanBus.server.service.MaintenanceService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import com.urbanBus.server.notification.NotificationProducer;

/**
 *
 * @author aimeb
 */
public class MaintenanceServiceImpl extends UnicastRemoteObject implements MaintenanceService {

    MaintenanceDao dao = new MaintenanceDao();
    BusDao busDao = new BusDao();

    public MaintenanceServiceImpl() throws RemoteException {
    }

    @Override
    public Maintenance saveMaintenance(Maintenance maintenance)throws RemoteException {
    // Automatically set bus status to UNDER_MAINTENANCE
    Bus bus = maintenance.getBus();
    bus.setStatus(BusStatus.UNDER_MAINTENANCE);
    busDao.updateBus(bus);

    // Save maintenance record
    Maintenance saved = dao.saveMaintenance(maintenance);

    // Send notification via ActiveMQ
    NotificationProducer.sendMaintenanceAlert(
        "aimeb909@gmail.com",
        maintenance.getBus().getPlateNumber()
    );

    return saved;
}

    @Override
    public Maintenance updateMaintenance(Maintenance maintenance) throws RemoteException { 
        return dao.updateMaintenance(maintenance);
    }

    @Override
    public Maintenance deleteMaintenance(Maintenance maintenance) throws RemoteException {
        return dao.deleteMaintenance(maintenance);
    }

    @Override
    public Maintenance findMaintenanceById(Long id) throws RemoteException {
        return dao.findMaintenanceById(id);
    }

    @Override
    public List<Maintenance> findAllMaintenance() throws RemoteException {
        return dao.findAllMaintenance();
    }

    @Override
    public List<Maintenance> findMaintenanceByBus(Long busId) throws RemoteException {
        return dao.findMaintenanceByBus(busId);
    }
}
