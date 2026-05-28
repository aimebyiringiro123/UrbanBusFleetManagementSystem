/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.service;

import com.urbanBus.server.model.User;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author aimeb
 */
public interface UserService extends Remote {

    User saveUser(User user) throws RemoteException;
    User updateUser(User user) throws RemoteException;
    User deleteUser(User user) throws RemoteException;
    User findUserById(Long id) throws RemoteException;
    User findByUsername(String username) throws RemoteException;
    User login(String username, String password) throws RemoteException;
    List<User> findAllUsers() throws RemoteException;
    String generateOTP(String username, String email) throws RemoteException;
    boolean verifyOTP(String username, String otp) throws RemoteException;
}
