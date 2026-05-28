/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.urbanBus.server.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author aimeb
 */
public class OTPUtil {
    
    private static Map<String, String> otpStore = new HashMap<>();
    private static Map<String, Long> otpExpiry = new HashMap<>();
    private static final long OTP_VALIDITY = 5 * 60 * 1000;

    public static String generateOTP(String username, String email) {
        Random random = new Random();
        String otp = String.format("%06d", random.nextInt(999999));
        otpStore.put(username, otp);
        otpExpiry.put(username, System.currentTimeMillis() + OTP_VALIDITY);

        boolean sent = EmailUtil.sendOTPEmail(email, otp);
        if (sent) {
            System.out.println("OTP sent to: " + email);
        } else {
            System.out.println("Email failed. OTP for "
                + username + ": " + otp);
        }
        return otp;
    }

    public static boolean verifyOTP(String username, String otp) {
        if (!otpStore.containsKey(username)) {
            return false;
        }
        long expiry = otpExpiry.get(username);
        if (System.currentTimeMillis() > expiry) {
            otpStore.remove(username);
            otpExpiry.remove(username);
            return false;
        }
        boolean valid = otpStore.get(username).equals(otp);
        if (valid) {
            otpStore.remove(username);
            otpExpiry.remove(username);
        }
        return valid;
    }
}
