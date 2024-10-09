package com.esoft.citytaxi.util;

import java.util.Random;

public class PasswordUtil {

    public static String generatePassword(int length) {
        // Define the characters to include in the password
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[]{}|;:',.<>?/~`";
        StringBuilder password = new StringBuilder(length);
        Random random = new Random();

        // Generate a password of the specified length
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }
}
