/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.Driver;
import com.urbanBus.server.util.*;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class DriverDao {
    
    //save driver
     public Driver saveDriver(Driver driver) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(driver);
            tr.commit();
            session.close();
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     
     //update
    public Driver updateDriver(Driver driver) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(driver);
            tr.commit();
            session.close();
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete
    public Driver deleteDriver(Driver driver) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(driver);
            tr.commit();
            session.close();
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    //find by id
    public Driver findDriverById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Driver driver = (Driver) session.get(Driver.class, id);
            session.close();
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //all drivers
    public List<Driver> findAllDrivers() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Driver> drivers = session.createQuery("FROM Driver").list();
            session.close();
            return drivers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    //find driver by license
    public Driver findDriverByLicense(String licenseNumber) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Driver driver = (Driver) session.createQuery(
                "FROM Driver WHERE licenseNumber = :license")
                .setParameter("license", licenseNumber)
                .uniqueResult();
            session.close();
            return driver;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //find available drivers
    public List<Driver> findAvailableDrivers() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Driver> drivers = session.createQuery(
                "FROM Driver WHERE status = 'AVAILABLE'").list();
            session.close();
            return drivers;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
