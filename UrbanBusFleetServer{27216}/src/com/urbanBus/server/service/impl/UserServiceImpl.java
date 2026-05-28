/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service.impl;

import com.urbanBus.server.dao.UserDao;
import com.urbanBus.server.model.User;
import com.urbanBus.server.service.UserService;
import com.urbanBus.server.util.OTPUtil;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

/**
 *
 * @author aimeb
 */
public class UserServiceImpl extends UnicastRemoteObject implements UserService {

    UserDao dao = new UserDao();

    public UserServiceImpl() throws RemoteException {}

    @Override
    public User saveUser(User user) 
        throws RemoteException {
    User saved = dao.saveUser(user);
    
    // Send notification from server side
    com.urbanBus.server.notification
        .NotificationProducer.sendNewUserAlert(
            "aimeb909@gmail.com",
            user.getFullName(),
            user.getRole().toString());
    
    return saved;
}

    @Override
    public User updateUser(User user) throws RemoteException {
        return dao.updateUser(user);
    }

    @Override
    public User deleteUser(User user) throws RemoteException {
        return dao.deleteUser(user);
    }

    @Override
    public User findUserById(Long id) throws RemoteException {
        return dao.findUserById(id);
    }

    @Override
    public User findByUsername(String username) throws RemoteException {
        return dao.findByUsername(username);
    }

    @Override
    public User login(String username, String password) throws RemoteException {
        return dao.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<User> findAllUsers() throws RemoteException {
        return dao.findAllUsers();
    }

    @Override
    public String generateOTP(String username, String email) throws RemoteException {
        return OTPUtil.generateOTP(username, email);
    }

    @Override
    public boolean verifyOTP(String username, String otp) throws RemoteException {
        return OTPUtil.verifyOTP(username, otp);
    }
}
