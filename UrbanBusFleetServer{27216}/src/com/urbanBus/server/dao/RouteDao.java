/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.Route;
import com.urbanBus.server.util.HibernateUtil;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;
/**
 *
 * @author aimeb
 */
public class RouteDao {
    
    //save route
     public Route saveRoute(Route route) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(route);
            tr.commit();
            session.close();
            return route;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

     //update route
    public Route updateRoute(Route route) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(route);
            tr.commit();
            session.close();
            return route;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //delete route
    public Route deleteRoute(Route route) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(route);
            tr.commit();
            session.close();
            return route;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //find route by Id 
    public Route findRouteById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Route route = (Route) session.get(Route.class, id);
            session.close();
            return route;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    //list of routes
    public List<Route> findAllRoutes() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Route> routes = session.createQuery("FROM Route").list();
            session.close();
            return routes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
    

    //find active routes
    public List<Route> findActiveRoutes() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<Route> routes = session.createQuery(
                "FROM Route WHERE status = 'ACTIVE'").list();
            session.close();
            return routes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}
