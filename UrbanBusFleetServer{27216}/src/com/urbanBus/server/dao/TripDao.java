/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.Trip;
import com.urbanBus.server.util.HibernateUtil;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class TripDao {
 
    //save trip
     public Trip saveTrip(Trip trip) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(trip);
            tr.commit();
            session.close();
            return trip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     //update trip
    public Trip updateTrip(Trip trip) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(trip);
            tr.commit();
            session.close();
            return trip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete trip
    public Trip deleteTrip(Trip trip) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(trip);
            tr.commit();
            session.close();
            return trip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //find trip by id
    public Trip findTripById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Trip trip = (Trip) session.get(Trip.class, id);
            session.close();
            return trip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //listof all trip
    public List<Trip> findAllTrips() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Trip> trips = session.createQuery("FROM Trip").list();
            session.close();
            return trips;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    //find trip by status
    public List<Trip> findTripsByStatus(String status) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Trip> trips = session.createQuery(
                "FROM Trip WHERE status = :status")
                .setParameter("status", status)
                .list();
            session.close();
            return trips;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
