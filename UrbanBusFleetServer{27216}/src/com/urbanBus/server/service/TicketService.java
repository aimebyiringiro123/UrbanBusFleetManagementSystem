/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.Ticket;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface TicketService extends Remote{
    Ticket saveTicket(Ticket ticket) throws RemoteException;
    Ticket updateTicket(Ticket ticket) throws RemoteException;
    Ticket deleteTicket(Ticket ticket) throws RemoteException;
    Ticket findTicketById(Long id) throws RemoteException;
    List<Ticket> findAllTickets() throws RemoteException;
    List<Ticket> findTicketsByTrip(Long tripId) throws RemoteException;
}
