/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.chatapp;

/**
 *
 * @author RC_Student_lab
 */
import javax.swing.JOptionPane;

public class MainApp {
    public static void main(String[] args) {
        Login login = new Login();

        // Welcome Message with options to Login or Register
        while (true) {
            String[] options = {"Register", "Login", "Exit"};
            int choice = JOptionPane.showOptionDialog(
                null,
                "Welcome to the Chat App! Please choose an option:",
                "Chat App",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (choice == 0) { // Register
                registerUser(login);
            } else if (choice == 1) { // Login
                loginUser(login);
            } else { // Exit
                JOptionPane.showMessageDialog(null, "Goodbye!");
                break;
            }
        }
    }

    private static void registerUser(Login login) {
        String firstName = JOptionPane.showInputDialog("Enter your first name:");
        if (firstName == null) {
            JOptionPane.showMessageDialog(null, "Registration cancelled.");
            return;
        }

        String lastName = JOptionPane.showInputDialog("Enter your last name:");
        if (lastName == null) {
            JOptionPane.showMessageDialog(null, "Registration cancelled.");
            return;
        }

        login.saveName(firstName, lastName);

        String username;
        while (true) {
            username = JOptionPane.showInputDialog("Enter your username:\nRules: Must contain an underscore (_) and be no longer than 5 characters.");
            if (username == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled.");
                return;
            }
            String usernameMessage = login.checkUserName(username);
            JOptionPane.showMessageDialog(null, usernameMessage);
            if (usernameMessage.equals("Username successfully captured.")) {
                break;
            }
        }

        String password;
        while (true) {
            password = JOptionPane.showInputDialog("Enter your password:\nRules:\n- At least 8 characters long\n- Must include a capital letter\n- Must include a number\n- Must include a special character (!, @, #, etc.)");
            if (password == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled.");
                return;
            }
            String passwordMessage = login.checkPassword(password);
            JOptionPane.showMessageDialog(null, passwordMessage);
            if (passwordMessage.equals("Password successfully captured.")) {
                break;
            }
        }

        String phoneNumber;
        while (true) {
            phoneNumber = JOptionPane.showInputDialog("Enter your phone number:\nRules: Must start with +27 and have 9 additional characters (e.g., +27123456789).");
            if (phoneNumber == null) {
                JOptionPane.showMessageDialog(null, "Registration cancelled.");
                return;
            }
            String phoneMessage = login.checkPhoneNumber(phoneNumber);
            JOptionPane.showMessageDialog(null, phoneMessage);
            if (phoneMessage.equals("Cell number successfully captured.")) {
                break;
            }
        }

        JOptionPane.showMessageDialog(null, "Thank you! Your account has been successfully created.");
    }

    private static void loginUser(Login login) {
        while (true) {
            String loginUsername = JOptionPane.showInputDialog("Login:\nEnter your username:");
            if (loginUsername == null) {
                JOptionPane.showMessageDialog(null, "Login cancelled.");
                return;
            }

            String loginPassword = JOptionPane.showInputDialog("Login:\nEnter your password:");
            if (loginPassword == null) {
                JOptionPane.showMessageDialog(null, "Login cancelled.");
                return;
            }

            if (login.login(loginUsername, loginPassword)) {
                JOptionPane.showMessageDialog(null, login.getWelcomeMessage());
                JOptionPane.showMessageDialog(null, "Welcome to QuickChat.");

                while (true) {
                    String[] postLoginOptions = {"Send Message", "View Stored Messages", "Logout", "Report Tools"};
                    int postLoginChoice = JOptionPane.showOptionDialog(
                        null,
                        "What would you like to do next?",
                        "Chat Options",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        postLoginOptions,
                        postLoginOptions[0]
                    );

                    if (postLoginChoice == 0) {
                        new Message().sendMessage();
                    } else if (postLoginChoice == 1) {
                        Message.viewStoredMessagesInteractive();
                    } else if (postLoginChoice == 2) {
                        JOptionPane.showMessageDialog(null, "You have been logged out.");
                        break;
                    } else if (postLoginChoice == 3) {
                        String[] tools = {
                            "1. Show Recently Sent Messages",
                            "2. View Disregarded Messages",
                            "3. Longest Sent Message",
                            "4. Search by ID",
                            "5. Search by Recipient",
                            "6. Delete by Hash"
                        };
                        int toolChoice = JOptionPane.showOptionDialog(
                            null,
                            "Select a Report Tool:",
                            "Report Tools",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.INFORMATION_MESSAGE,
                            null,
                            tools,
                            tools[0]
                        );

                        switch (toolChoice) {
                            case 0 -> Message.printMessages();
                            case 1 -> Message.viewDisregardedMessages();
                            case 2 -> Message.showLongestMessage();
                            case 3 -> {
                                String id = JOptionPane.showInputDialog("Enter Message ID:");
                                Message.searchByID(id);
                            }
                            case 4 -> {
                                String phone = JOptionPane.showInputDialog("Enter Recipient Number:");
                                Message.searchByRecipient(phone);
                            }
                            case 5 -> {
                                String hash = JOptionPane.showInputDialog("Enter Message Hash:");
                                Message.deleteByHash(hash);
                            }
                        }
                    }
                }
                break;
            } else {
                JOptionPane.showMessageDialog(null, "Incorrect username or password. Please try again.");
            }
        }
    }
}
