package com.quickchat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Message class – Part 2 + Part 3 extensions.
 * Handles creation, validation, hashing, send/store/disregard,
 * and the arrays + search/delete/report features required for Part 3.
 *
 * JSON storage uses org.json library.
 * Reference: https://github.com/stleary/JSON-java
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag; // Sent / Stored / Disregard
    private String sender; // for Part 3 display

    // ---------- Static arrays required by Part 3 ----------
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<String> messageHashes = new ArrayList<>();
    private static List<String> messageIDs = new ArrayList<>();

    private static int totalMessagesSent = 0;
    private static final String JSON_FILE = "stored_messages.json";

    public Message() {
        this.messageID = generateMessageID();
    }

    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
    }

    // ---------- Part 2 required methods ----------

    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        Pattern pattern = Pattern.compile("^\\+27[0-9]{9,10}$");
        if (pattern.matcher(recipient).matches()) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2 || messageText == null || messageText.trim().isEmpty()) {
            this.messageHash = "00:0:EMPTY";
            return this.messageHash;
        }
        String firstTwo = messageID.substring(0, 2);
        String[] words = messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "").toUpperCase();
        this.messageHash = firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
        return this.messageHash;
    }

    public String SentMessage(String choice) {
        if (choice == null) return "Invalid option.";
        switch (choice.trim()) {
            case "1":
            case "Send Message":
            case "send":
                this.flag = "Sent";
                totalMessagesSent++;
                sentMessages.add(this);
                addToArrays(this);
                return "Message successfully sent.";
            case "2":
            case "Disregard Message":
            case "disregard":
            case "discard":
                this.flag = "Disregard";
                disregardedMessages.add(this);
                addToArrays(this);
                return "Press 0 to delete the message.";
            case "3":
            case "Store Message":
            case "store":
                this.flag = "Stored";
                storedMessages.add(this);
                addToArrays(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    public String printMessages() {
        if (sentMessages.isEmpty()) return "No messages have been sent yet.";
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append(m.getFullDetails()).append("\n----------------------------\n");
        }
        return sb.toString();
    }

    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    public void storeMessage() {
        try {
            JSONArray array;
            if (Files.exists(Paths.get(JSON_FILE))) {
                String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
                array = content.trim().isEmpty() ? new JSONArray() : new JSONArray(content);
            } else {
                array = new JSONArray();
            }
            JSONObject obj = new JSONObject();
            obj.put("messageID", this.messageID);
            obj.put("messageNumber", this.messageNumber);
            obj.put("recipient", this.recipient);
            obj.put("message", this.messageText);
            obj.put("messageHash", this.messageHash);
            obj.put("flag", this.flag != null ? this.flag : "Stored");
            obj.put("sender", this.sender != null ? this.sender : "");
            array.put(obj);
            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                writer.write(array.toString(4));
            }
        } catch (IOException e) {
            System.out.println("Error storing message to JSON: " + e.getMessage());
        }
    }

    public String validateMessageLength() {
        if (messageText == null) {
            return "Message exceeds 250 characters by 0; please reduce the size.";
        }
        int length = messageText.length();
        if (length <= 250) return "Message ready to send.";
        return "Message exceeds 250 characters by " + (length - 250) + "; please reduce the size.";
    }

    // ---------- Part 3: Array helpers & required features ----------

    private void addToArrays(Message m) {
        if (m.messageHash != null && !messageHashes.contains(m.messageHash)) {
            messageHashes.add(m.messageHash);
        }
        if (m.messageID != null && !messageIDs.contains(m.messageID)) {
            messageIDs.add(m.messageID);
        }
    }

    /** Load stored messages from JSON file into the storedMessages array. */
    public static void loadStoredMessagesFromJSON() {
        storedMessages.clear();
        try {
            if (!Files.exists(Paths.get(JSON_FILE))) return;
            String content = new String(Files.readAllBytes(Paths.get(JSON_FILE)));
            if (content.trim().isEmpty()) return;
            JSONArray array = new JSONArray(content);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                Message m = new Message();
                m.setMessageID(obj.optString("messageID", ""));
                m.setMessageNumber(obj.optInt("messageNumber", 0));
                m.setRecipient(obj.optString("recipient", ""));
                m.setMessageText(obj.optString("message", ""));
                m.messageHash = obj.optString("messageHash", "");
                m.flag = obj.optString("flag", "Stored");
                m.sender = obj.optString("sender", "");
                storedMessages.add(m);
                if (m.messageHash != null && !messageHashes.contains(m.messageHash)) {
                    messageHashes.add(m.messageHash);
                }
                if (m.messageID != null && !messageIDs.contains(m.messageID)) {
                    messageIDs.add(m.messageID);
                }
            }
        } catch (Exception e) {
            System.out.println("Error reading JSON: " + e.getMessage());
        }
    }

    /** a. Display sender and recipient of all stored messages */
    public static String displaySenderAndRecipient() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            String sender = (m.sender == null || m.sender.isEmpty()) ? "Unknown" : m.sender;
            sb.append("Sender: ").append(sender)
              .append(" | Recipient: ").append(m.recipient).append("\n");
        }
        return sb.toString();
    }

    /** b. Display the longest stored message */
    public static String displayLongestMessage() {
        if (storedMessages.isEmpty()) return "No stored messages.";
        Message longest = storedMessages.get(0);
        for (Message m : storedMessages) {
            if (m.messageText != null && m.messageText.length() > longest.messageText.length()) {
                longest = m;
            }
        }
        return longest.messageText;
    }

    /** c. Search by message ID → show recipient and message */
    public static String searchByMessageID(String id) {
        for (Message m : storedMessages) {
            if (id != null && id.equals(m.messageID)) {
                return "Recipient: " + m.recipient + "\nMessage: " + m.messageText;
            }
        }
        // Also check sent messages
        for (Message m : sentMessages) {
            if (id != null && id.equals(m.messageID)) {
                return "Recipient: " + m.recipient + "\nMessage: " + m.messageText;
            }
        }
        return "Message ID not found.";
    }

    /** d. Search all messages for a particular recipient */
    public static String searchByRecipient(String recipient) {
        StringBuilder sb = new StringBuilder();
        for (Message m : storedMessages) {
            if (recipient != null && recipient.equals(m.recipient)) {
                sb.append(m.messageText).append("\n");
            }
        }
        for (Message m : sentMessages) {
            if (recipient != null && recipient.equals(m.recipient)) {
                sb.append(m.messageText).append("\n");
            }
        }
        if (sb.length() == 0) return "No messages found for that recipient.";
        return sb.toString().trim();
    }

    /** e. Delete a message using the message hash */
    public static String deleteByHash(String hash) {
        Message toRemove = null;
        for (Message m : storedMessages) {
            if (hash != null && hash.equals(m.messageHash)) {
                toRemove = m;
                break;
            }
        }
        if (toRemove != null) {
            storedMessages.remove(toRemove);
            messageHashes.remove(hash);
            // Also rewrite JSON without this message
            rewriteJSON();
            return "Message: \"" + toRemove.messageText + "\" successfully deleted.";
        }
        return "Message hash not found. Nothing deleted.";
    }

    /** f. Display a full report of all stored messages */
    public static String displayReport() {
        if (storedMessages.isEmpty()) return "No stored messages to report.";
        StringBuilder sb = new StringBuilder();
        sb.append("===== STORED MESSAGES REPORT =====\n");
        for (Message m : storedMessages) {
            sb.append("Message Hash: ").append(m.messageHash).append("\n")
              .append("Recipient: ").append(m.recipient).append("\n")
              .append("Message: ").append(m.messageText).append("\n")
              .append("----------------------------\n");
        }
        return sb.toString();
    }

    private static void rewriteJSON() {
        try {
            JSONArray array = new JSONArray();
            for (Message m : storedMessages) {
                JSONObject obj = new JSONObject();
                obj.put("messageID", m.messageID);
                obj.put("messageNumber", m.messageNumber);
                obj.put("recipient", m.recipient);
                obj.put("message", m.messageText);
                obj.put("messageHash", m.messageHash);
                obj.put("flag", m.flag != null ? m.flag : "Stored");
                obj.put("sender", m.sender != null ? m.sender : "");
                array.put(obj);
            }
            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                writer.write(array.toString(4));
            }
        } catch (IOException e) {
            System.out.println("Error rewriting JSON: " + e.getMessage());
        }
    }

    // ---------- Helpers for tests / Main ----------

    private String generateMessageID() {
        Random random = new Random();
        long id = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(id);
    }

    public void setMessageNumber(int n) { this.messageNumber = n; }
    public void setRecipient(String r) { this.recipient = r; }
    public void setMessageText(String t) { this.messageText = t; }
    public void setMessageID(String id) { this.messageID = id; }
    public void setSender(String s) { this.sender = s; }
    public void setFlag(String f) { this.flag = f; }
    public void setMessageHash(String h) { this.messageHash = h; }

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }
    public String getSender() { return sender; }

    public String getFullDetails() {
        return "Message ID: " + messageID + "\n"
             + "Message Hash: " + messageHash + "\n"
             + "Recipient: " + recipient + "\n"
             + "Message: " + messageText;
    }

    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }
    public static List<String> getMessageHashes() { return messageHashes; }
    public static List<String> getMessageIDs() { return messageIDs; }

    public static void resetAll() {
        totalMessagesSent = 0;
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();
    }

    /** Populate arrays with the exact test data from the brief (for unit tests). */
    public static void populateTestData() {
        resetAll();

        // Message 1 – Sent
        Message m1 = new Message();
        m1.setMessageID("1000000001");
        m1.setRecipient("+27834557896");
        m1.setMessageText("Did you get the cake?");
        m1.setMessageNumber(0);
        m1.createMessageHash();
        m1.setFlag("Sent");
        m1.setSender("Developer");
        sentMessages.add(m1);
        addToStaticArrays(m1);

        // Message 2 – Stored
        Message m2 = new Message();
        m2.setMessageID("1000000002");
        m2.setRecipient("+27838884567");
        m2.setMessageText("Where are you? You are late! I have asked you to be on time.");
        m2.setMessageNumber(1);
        m2.createMessageHash();
        m2.setFlag("Stored");
        m2.setSender("Developer");
        storedMessages.add(m2);
        addToStaticArrays(m2);

        // Message 3 – Disregard
        Message m3 = new Message();
        m3.setMessageID("1000000003");
        m3.setRecipient("+27834484567");
        m3.setMessageText("Yohoooo, I am at your gate.");
        m3.setMessageNumber(2);
        m3.createMessageHash();
        m3.setFlag("Disregard");
        m3.setSender("Developer");
        disregardedMessages.add(m3);
        addToStaticArrays(m3);

        // Message 4 – Sent
        Message m4 = new Message();
        m4.setMessageID("0838884567"); // as per brief test data for search
        m4.setRecipient("0838884567");
        m4.setMessageText("It is dinner time !");
        m4.setMessageNumber(3);
        m4.createMessageHash();
        m4.setFlag("Sent");
        m4.setSender("Developer");
        sentMessages.add(m4);
        addToStaticArrays(m4);

        // Message 5 – Stored
        Message m5 = new Message();
        m5.setMessageID("1000000005");
        m5.setRecipient("+27838884567");
        m5.setMessageText("Ok, I am leaving without you.");
        m5.setMessageNumber(4);
        m5.createMessageHash();
        m5.setFlag("Stored");
        m5.setSender("Developer");
        storedMessages.add(m5);
        addToStaticArrays(m5);

        totalMessagesSent = 2; // m1 and m4
    }

    private static void addToStaticArrays(Message m) {
        if (m.messageHash != null && !messageHashes.contains(m.messageHash)) {
            messageHashes.add(m.messageHash);
        }
        if (m.messageID != null && !messageIDs.contains(m.messageID)) {
            messageIDs.add(m.messageID);
        }
    }
}
