/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.Ticket;
import com.urbanBus.server.util.HibernateUtil;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class TicketDao {
    
    //save ticket
     public Ticket saveTicket(Ticket ticket) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(ticket);
            tr.commit();
            session.close();
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     //update ticket
    public Ticket updateTicket(Ticket ticket) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(ticket);
            tr.commit();
            session.close();
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete ticket
    public Ticket deleteTicket(Ticket ticket) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(ticket);
            tr.commit();
            session.close();
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //find ticket by id
    public Ticket findTicketById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Ticket ticket = (Ticket) session.get(Ticket.class, id);
            session.close();
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //list of all ticket
    public List<Ticket> findAllTickets() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Ticket> tickets = session.createQuery("FROM Ticket").list();
            session.close();
            return tickets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    //find ticket by trip
    public List<Ticket> findTicketsByTrip(Long tripId) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Ticket> tickets = session.createQuery(
                "FROM Ticket WHERE trip.tripId = :tripId")
                .setParameter("tripId", tripId)
                .list();
            session.close();
            return tickets;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
