/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.TicketDao;
import com.urbanBus.server.model.Ticket;
import com.urbanBus.server.service.TicketService;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aimeb
 */
public class TicketServiceImpl extends UnicastRemoteObject implements TicketService {

    TicketDao dao = new TicketDao();

    public TicketServiceImpl() throws RemoteException {
    }

    @Override
    public Ticket saveTicket(Ticket ticket) throws RemoteException {
        
        return dao.saveTicket(ticket);
    }

    @Override
    public Ticket updateTicket(Ticket ticket) throws RemoteException {
        return dao.updateTicket(ticket);
    }

    @Override
    public Ticket deleteTicket(Ticket ticket) throws RemoteException {
        return dao.deleteTicket(ticket);
    }

    @Override
    public Ticket findTicketById(Long id) throws RemoteException {
        return dao.findTicketById(id);
    }

    @Override
    public List<Ticket> findAllTickets() throws RemoteException {
        return dao.findAllTickets();
    }

    @Override
    public List<Ticket> findTicketsByTrip(Long tripId) throws RemoteException {
        return dao.findTicketsByTrip(tripId);
    }
}
