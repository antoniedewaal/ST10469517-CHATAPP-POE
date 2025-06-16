/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatapp;

/**
 *Z
 * @author RC_Student_lab
 */

public class Login {
    private String username;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;

    // Check if the username is valid
    public String checkUserName(String username) {
        if (username.contains("_") && username.length() <= 5) {
            this.username = username;
            return "Username successfully captured.";
        } else {
            return "Username is not correctly formatted, please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    }

    // Check if the password meets complexity requirements
    public String checkPassword(String password) {
        if (password.length() >= 8 &&
            password.matches(".*[A-Z].*") && // Contains a capital letter
            password.matches(".*\\d.*") &&  // Contains a number
            password.matches(".*[!@#$%^&*()].*")) { // Contains a special character
            this.password = password;
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    // Check if the phone number matches the criteria using a regular expression
    public String checkPhoneNumber(String phoneNumber) {
        if (phoneNumber.matches("^\\+27\\d{9}$")) { // Regular expression to validate format
            this.phoneNumber = phoneNumber;
            return "Cell number successfully captured.";
        } else {
            return "Cell number is incorrectly formatted or does not contain an international code, please correct the number and try again.";
        }
    }

    // Save user's name
    public void saveName(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Login and verify credentials
    public boolean login(String username, String password) {
        return username.equals(this.username) && password.equals(this.password);
    }

    // Return personalized login message
    public String getWelcomeMessage() {
        return "Welcome " + firstName + " " + lastName + ", it is great to see you.";
    }
}
