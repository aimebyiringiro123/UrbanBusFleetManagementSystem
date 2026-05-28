/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.Passenger;
import com.urbanBus.server.util.HibernateUtil;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class PassengerDao {
    
    //save passenger
    public Passenger savePassenger(Passenger passenger) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(passenger);
            tr.commit();
            session.close();
            return passenger;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //update passenger
    public Passenger updatePassenger(Passenger passenger) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(passenger);
            tr.commit();
            session.close();
            return passenger;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete passenger
    public Passenger deletePassenger(Passenger passenger) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(passenger);
            tr.commit();
            session.close();
            return passenger;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //find passenger by id
    public Passenger findPassengerById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Passenger passenger = (Passenger) session.get(Passenger.class, id);
            session.close();
            return passenger;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //all passenger
    public List<Passenger> findAllPassengers() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Passenger> passengers = session.createQuery("FROM Passenger").list();
            session.close();
            return passengers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    //find passenger by phone
    public Passenger findPassengerByPhone(String phone) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Passenger passenger = (Passenger) session.createQuery(
                "FROM Passenger WHERE phone = :phone")
                .setParameter("phone", phone)
                .uniqueResult();
            session.close();
            return passenger;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

