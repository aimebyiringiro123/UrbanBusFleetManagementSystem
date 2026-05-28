/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.util;

import com.urbanBus.server.util.HibernateUtil;
import org.hibernate.Session;

/**
 *
 * @author aimeb
 */
public class TestConnection {

    public static void main(String[] args) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            System.out.println("Database connection successful!");
            System.out.println("Tables created successfully!");
            session.close();
            HibernateUtil.shutdown();
        } catch (Exception e) {
            System.err.println("Connection failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}