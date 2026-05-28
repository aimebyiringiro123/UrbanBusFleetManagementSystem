/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.dao;

import com.urbanBus.server.model.User;
import com.urbanBus.server.util.HibernateUtil;
import java.util.Collections;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author aimeb
 */
public class UserDao {

    public User saveUser(User user) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.save(user);
            tr.commit();
            session.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User updateUser(User user) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.update(user);
            tr.commit();
            session.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User deleteUser(User user) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tr = session.beginTransaction();
            session.delete(user);
            tr.commit();
            session.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findUserById(Long id) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            User user = (User) session.get(User.class, id);
            session.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByUsername(String username) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            User user = (User) session.createQuery(
                "FROM User WHERE username = :username")
                .setParameter("username", username)
                .uniqueResult();
            session.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User findByUsernameAndPassword(String username, String password) {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            User user = (User) session.createQuery(
                "FROM User WHERE username = :username AND password = :password AND active = true")
                .setParameter("username", username)
                .setParameter("password", password)
                .uniqueResult();
            session.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<User> findAllUsers() {
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            List<User> users = session.createQuery("FROM User").list();
            session.close();
            return users;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.EMPTY_LIST;
    }
}