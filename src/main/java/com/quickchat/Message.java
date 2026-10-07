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
 * Message class for QuickChat Part 2.
 * Handles message creation, validation, hashing, sending, storing and reporting.
 *
 * JSON storage uses the org.json library.
 * Reference for JSON handling: https://github.com/stleary/JSON-java
 */
public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String flag; // "Sent", "Stored", "Disregard"

    // Static tracking across all messages
    private static int totalMessagesSent = 0;
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();
    private static List<Message> disregardedMessages = new ArrayList<>();
    private static final String JSON_FILE = "stored_messages.json";

    public Message() {
        this.messageID = generateMessageID();
    }

    public Message(String recipient, String messageText) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
    }

    // ---------- Required methods ----------

    /**
     * Ensures the message ID is not more than ten characters.
     */
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    /**
     * Ensures the recipient cell number is no more than ten characters long
     * for the number portion and starts with an international code.
     * Returns success / failure message as required by the brief.
     */
    public String checkRecipientCell() {
        if (recipient == null) {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
        // Reuse same validation style as Part 1
        Pattern pattern = Pattern.compile("^\\+27[0-9]{9,10}$");
        if (pattern.matcher(recipient).matches()) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    /**
     * Creates and returns the Message Hash.
     * Format: first two digits of Message ID + ":" + message number + ":" + first and last words in CAPS
     * Example: 00:0:HITONIGHT
     */
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

    /**
     * Allows the user to choose Send, Store or Disregard.
     * Returns the appropriate message string.
     */
    public String SentMessage(String choice) {
        if (choice == null) return "Invalid option.";

        switch (choice.trim()) {
            case "1":
            case "Send Message":
            case "send":
                this.flag = "Sent";
                totalMessagesSent++;
                sentMessages.add(this);
                return "Message successfully sent.";
            case "2":
            case "Disregard Message":
            case "disregard":
            case "discard":
                this.flag = "Disregard";
                disregardedMessages.add(this);
                return "Press 0 to delete the message.";
            case "3":
            case "Store Message":
            case "store":
                this.flag = "Stored";
                storedMessages.add(this);
                storeMessage();
                return "Message successfully stored.";
            default:
                return "Invalid option selected.";
        }
    }

    /**
     * Returns all the messages sent while the program is running.
     */
    public String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (Message m : sentMessages) {
            sb.append("Message ID: ").append(m.messageID).append("\n")
              .append("Message Hash: ").append(m.messageHash).append("\n")
              .append("Recipient: ").append(m.recipient).append("\n")
              .append("Message: ").append(m.messageText).append("\n")
              .append("----------------------------\n");
        }
        return sb.toString();
    }

    /**
     * Returns the total number of messages sent.
     */
    public int returnTotalMessagess() {
        return totalMessagesSent;
    }

    /**
     * Stores the message in a JSON file.
     * Research attribution: Uses org.json library (https://github.com/stleary/JSON-java)
     */
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

            array.put(obj);

            try (FileWriter writer = new FileWriter(JSON_FILE)) {
                writer.write(array.toString(4));
            }
        } catch (IOException e) {
            System.out.println("Error storing message to JSON: " + e.getMessage());
        }
    }

    // ---------- Helper / validation methods ----------

    public String validateMessageLength() {
        if (messageText == null) {
            return "Message exceeds 250 characters by 0; please reduce the size.";
        }
        int length = messageText.length();
        if (length <= 250) {
            return "Message ready to send.";
        }
        int excess = length - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    private String generateMessageID() {
        Random random = new Random();
        // Generate a 10-digit number as a string (leading zeros possible)
        long id = 1_000_000_000L + (long) (random.nextDouble() * 9_000_000_000L);
        return String.valueOf(id);
    }

    // ---------- Setters used by Main / tests ----------

    public void setMessageNumber(int number) {
        this.messageNumber = number;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setMessageText(String text) {
        this.messageText = text;
    }

    public void setMessageID(String id) {
        this.messageID = id;
    }

    // ---------- Getters ----------

    public String getMessageID() { return messageID; }
    public int getMessageNumber() { return messageNumber; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public String getFlag() { return flag; }

    public static List<Message> getSentMessages() { return sentMessages; }
    public static List<Message> getStoredMessages() { return storedMessages; }
    public static List<Message> getDisregardedMessages() { return disregardedMessages; }

    public static void resetCounters() {
        totalMessagesSent = 0;
        sentMessages.clear();
        storedMessages.clear();
        disregardedMessages.clear();
    }

    /**
     * Full details display required by the brief.
     */
    public String getFullDetails() {
        return "Message ID: " + messageID + "\n"
             + "Message Hash: " + messageHash + "\n"
             + "Recipient: " + recipient + "\n"
             + "Message: " + messageText;
    }
}
