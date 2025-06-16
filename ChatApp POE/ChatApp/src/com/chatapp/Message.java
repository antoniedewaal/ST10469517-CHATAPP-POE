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
import java.io.*;
import java.util.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Message {

    static ArrayList<String> sentMessages = new ArrayList<>();
    static ArrayList<String> disregardedMessages = new ArrayList<>();
    static ArrayList<String> storedMessages = new ArrayList<>();
    static ArrayList<String> messageIDs = new ArrayList<>();
    static ArrayList<String> messageHashes = new ArrayList<>();

    public void sendMessage() {
        String recipient = JOptionPane.showInputDialog("Enter recipient phone number:");
        if (recipient == null) return;

        String phoneMessage = new Login().checkPhoneNumber(recipient);
        JOptionPane.showMessageDialog(null, phoneMessage);
        if (!phoneMessage.equals("Cell number successfully captured.")) return;

        String message = JOptionPane.showInputDialog("Type your message (Max 250 characters):");
        if (message == null) return;

        if (message.length() > 250) {
            int excess = message.length() - 250;
            JOptionPane.showMessageDialog(null, "Message exceeds 250 characters by " + excess + ", please reduce size.");
            return;
        } else {
            JOptionPane.showMessageDialog(null, "Message ready to send.");
        }

        String[] options = {"Send Message", "Disregard Message", "Store Message"};
        int choice = JOptionPane.showOptionDialog(null, "What would you like to do with this message?", "Message Options",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

        String id = generateID(recipient);
        String hash = createMessageHash(id, message);

        messageIDs.add(id);
        messageHashes.add(hash);

        switch (choice) {
            case 0 -> {
                sentMessages.add("ID: " + id + "\nHash: " + hash + "\nTo: " + recipient + "\nMessage: " + message);
                JOptionPane.showMessageDialog(null, "Message successfully sent.");
            }
            case 1 -> {
                disregardedMessages.add("ID: " + id + "\nHash: " + hash + "\nTo: " + recipient + "\nMessage: " + message);
                JOptionPane.showOptionDialog(null, "Press 0 to delete message.", "Disregard", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new String[]{"0 (Delete)"}, "0 (Delete)");
            }
            case 2 -> {
                storeToJSON(id, hash, recipient, message);
                JOptionPane.showMessageDialog(null, "Message successfully stored.");
            }
        }
    }

    public static void printMessages() {
        if (sentMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No sent messages available.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessages) {
            sb.append(msg).append("\n---\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static void showLongestMessage() {
        String longest = "";
        for (String msg : sentMessages) {
            String[] parts = msg.split("Message: ");
            if (parts.length > 1 && parts[1].length() > longest.length()) {
                longest = msg;
            }
        }
        JOptionPane.showMessageDialog(null, "Longest Sent Message:\n" + longest);
    }

    public static void searchByID(String id) {
        int index = messageIDs.indexOf(id);
        if (index != -1 && index < sentMessages.size()) {
            JOptionPane.showMessageDialog(null, sentMessages.get(index));
        } else {
            JOptionPane.showMessageDialog(null, "Message ID not found.");
        }
    }

    public static void searchByRecipient(String phone) {
        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessages) {
            if (msg.contains("To: " + phone)) {
                sb.append(msg).append("\n---\n");
            }
        }
        for (String msg : storedMessages) {
            if (msg.contains("To: " + phone)) {
                sb.append(msg).append("\n---\n");
            }
        }
        JOptionPane.showMessageDialog(null, sb.length() > 0 ? sb.toString() : "No messages found for that recipient.");
    }

    public static void deleteByHash(String hash) {
        int index = messageHashes.indexOf(hash);
        if (index != -1 && index < sentMessages.size()) {
            String msg = sentMessages.remove(index);
            messageHashes.remove(index);
            messageIDs.remove(index);
            JOptionPane.showMessageDialog(null, "Message \"" + msg + "\" successfully deleted.");
        } else {
            JOptionPane.showMessageDialog(null, "Message not found.");
        }
    }

    public static void storeToJSON(String id, String hash, String recipient, String message) {
        JSONObject obj = new JSONObject();
        obj.put("id", id);
        obj.put("hash", hash);
        obj.put("recipient", recipient);
        obj.put("message", message);

        JSONArray array = readJSON();
        array.add(obj);

        try (FileWriter fw = new FileWriter("stored_messages.json")) {
            fw.write(array.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JSONArray readJSON() {
        try (FileReader reader = new FileReader("stored_messages.json")) {
            return (JSONArray) new JSONParser().parse(reader);
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    public static void viewStoredMessagesInteractive() {
        JSONArray stored = readJSON();
        storedMessages.clear();

        if (stored.isEmpty()) {
            int option = JOptionPane.showConfirmDialog(null, "No stored messages. Would you like to send one now?", "Stored Messages", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                new Message().sendMessage();
            }
            return;
        }

        for (Object obj : stored) {
            JSONObject msg = (JSONObject) obj;
            String line = "ID: " + msg.get("id") + "\nHash: " + msg.get("hash") + "\nTo: " + msg.get("recipient") + "\nMessage: " + msg.get("message");
            storedMessages.add(line);
        }

        for (int i = 0; i < storedMessages.size(); i++) {
            String[] options = {"Send", "Delete", "Skip"};
            int choice = JOptionPane.showOptionDialog(null, storedMessages.get(i), "Stored Message Options",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

            if (choice == 0) {
                sentMessages.add(storedMessages.get(i));
                stored.remove(i);
                storedMessages.remove(i);
                i--;
                JOptionPane.showMessageDialog(null, "Message sent.");
            } else if (choice == 1) {
                stored.remove(i);
                storedMessages.remove(i);
                i--;
                JOptionPane.showMessageDialog(null, "Message deleted.");
            }
        }

        try (FileWriter fw = new FileWriter("stored_messages.json")) {
            fw.write(stored.toJSONString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void viewDisregardedMessages() {
        if (disregardedMessages.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No disregarded messages.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (String msg : disregardedMessages) {
            sb.append(msg).append("\n---\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString());
    }

    public static String generateID(String recipient) {
        String clean = recipient.replaceAll("[^\\d]", "");
        return "MSG" + clean.substring(Math.max(0, clean.length() - 4)) + (messageIDs.size() + 1);
    }

    public static String createMessageHash(String id, String message) {
        String[] idParts = id.replaceAll("[^\\d]", "").split("");
        String prefix = idParts.length >= 2 ? idParts[0] + idParts[1] : "00";
        String count = String.valueOf(messageIDs.size() + 1);

        String[] words = message.trim().split("\\s+");
        String firstWord = words.length > 0 ? words[0].replaceAll("[^a-zA-Z]", "").toUpperCase() : "";
        String lastWord = words.length > 1 ? words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase() : firstWord;

        return prefix + ":" + count + ":" + firstWord + lastWord;
    }
}
