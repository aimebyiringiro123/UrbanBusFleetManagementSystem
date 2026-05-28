/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.Bus;
import com.urbanBus.server.util.*;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class BusDao {
    
    
    //save
    public Bus saveBus(Bus bus) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(bus);
            tr.commit();
            session.close();
            return bus;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //update
    public Bus updateBus(Bus bus) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(bus);
            tr.commit();
            session.close();
            return bus;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //delete
     public Bus deleteBus(Bus bus) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(bus);
            tr.commit();
            session.close();
            return bus;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     
     //find by Id
      public Bus findBusById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Bus bus = (Bus) session.get(Bus.class, id);
            session.close();
            return bus;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
      
    //list of all
     public List<Bus> findAllBuses() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Bus> buses = session.createQuery("FROM Bus").list();
            session.close();
            return buses;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
     
     //find bus by plate
      public Bus findBusByPlate(String plateNumber) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Bus bus = (Bus) session.createQuery(
                "FROM Bus WHERE plateNumber = :plate")
                .setParameter("plate", plateNumber)
                .uniqueResult();
            session.close();
            return bus;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    
    
}
