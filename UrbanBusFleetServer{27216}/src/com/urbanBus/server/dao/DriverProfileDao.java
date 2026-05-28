/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.DriverProfile;
import com.urbanBus.server.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class DriverProfileDao {
    
     public DriverProfile saveProfile(
            DriverProfile profile) {
        try {
            Session session = HibernateUtil
                .getSessionFactory().openSession();
            Transaction tr =
                session.beginTransaction();
            session.save(profile);
            tr.commit();
            session.close();
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DriverProfile updateProfile(
            DriverProfile profile) {
        try {
            Session session = HibernateUtil
                .getSessionFactory().openSession();
            Transaction tr =
                session.beginTransaction();
            session.update(profile);
            tr.commit();
            session.close();
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public DriverProfile findByDriverId(
            Long driverId) {
        try {
            Session session = HibernateUtil
                .getSessionFactory().openSession();
            DriverProfile profile =
                (DriverProfile) session
                    .createQuery(
                        "FROM DriverProfile WHERE "
                        + "driver.driverId = :id")
                    .setParameter("id", driverId)
                    .uniqueResult();
            session.close();
            return profile;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
}
