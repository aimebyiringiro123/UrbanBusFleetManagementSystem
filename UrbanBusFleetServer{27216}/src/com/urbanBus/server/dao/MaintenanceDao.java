/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.Maintenance;
import com.urbanBus.server.util.HibernateUtil;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class MaintenanceDao {
    
    //save maintenance
     public Maintenance saveMaintenance(Maintenance maintenance) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(maintenance);
            tr.commit();
            session.close();
            return maintenance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     //update maintenance
    public Maintenance updateMaintenance(Maintenance maintenance) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(maintenance);
            tr.commit();
            session.close();
            return maintenance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete maintenance
    public Maintenance deleteMaintenance(Maintenance maintenance) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(maintenance);
            tr.commit();
            session.close();
            return maintenance;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // find maintenance by id
    public Maintenance findMaintenanceById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Maintenance m = (Maintenance) session.get(Maintenance.class, id);
            session.close();
            return m;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //list of maintenance
    public List<Maintenance> findAllMaintenance() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Maintenance> list = session.createQuery("FROM Maintenance").list();
            session.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }

    //find maintance by bus
    public List<Maintenance> findMaintenanceByBus(Long busId) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Maintenance> list = session.createQuery(
                "FROM Maintenance WHERE bus.busId = :busId")
                .setParameter("busId", busId)
                .list();
            session.close();
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
