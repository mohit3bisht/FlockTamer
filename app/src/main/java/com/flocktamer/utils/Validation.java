package com.flocktamer.utils;

/**
 * Created by amandeep on 16/10/15.
 */
public class Validation {

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password != null && (password.trim().length() > 5);

    }

    public static boolean isMatchedPassword(String firstPassword, String secondPassword) {
        return (firstPassword.trim().equals(secondPassword));
    }

    public static boolean isValidMobile(String mobilenumber) {
//      return  Pattern.matches("^[+][0-9]{10}$",mobilenumber);
        return (android.util.Patterns.PHONE.matcher(mobilenumber).matches());
    }

}
