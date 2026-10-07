package com.quickchat;

import java.util.Scanner;

/**
 * Console application for QuickChat Part 2.
 * Includes registration/login from Part 1 + messaging features.
 * No GUI / JOptionPane allowed.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("========================================");
        System.out.println("        QuickChat Application");
        System.out.println("========================================");
        System.out.println();

        // ---------- REGISTRATION ----------
        System.out.println("--- CREATE ACCOUNT ---");
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine().trim();

        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine().trim();

        System.out.print("Enter username: ");
        String username = scanner.nextLine().trim();
        System.out.println(login.getUsernameMessage(username));

        System.out.print("Enter password: ");
        String password = scanner.nextLine().trim();
        System.out.println(login.getPasswordMessage(password));

        System.out.print("Enter South African cell phone number (e.g. +27838968976): ");
        String cellPhone = scanner.nextLine().trim();
        System.out.println(login.getCellPhoneMessage(cellPhone));

        System.out.println();
        System.out.println(login.registerUser(username, password, cellPhone, firstName, lastName));
        System.out.println();

        if (!login.isRegistered()) {
            System.out.println("Registration failed. Exiting.");
            scanner.close();
            return;
        }

        // ---------- LOGIN ----------
        System.out.println("--- LOGIN ---");
        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine().trim();
        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine().trim();
        System.out.println();
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));
        System.out.println();

        if (!login.isLoggedIn()) {
            System.out.println("Login failed. You cannot send messages. Exiting.");
            scanner.close();
            return;
        }

        // ---------- PART 2: MESSAGING ----------
        System.out.println("Welcome to QuickChat.");
        System.out.println();

        boolean running = true;
        while (running) {
            System.out.println("Please choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Enter choice (1-3): ");

            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1":
                    sendMessagesFlow(scanner);
                    break;
                case "2":
                    System.out.println("Coming Soon.");
                    System.out.println();
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    System.out.println();
            }
        }

        scanner.close();
    }

    /**
     * Handles the "Send Messages" option.
     * Asks how many messages, then loops that many times.
     */
    private static void sendMessagesFlow(Scanner scanner) {
        System.out.print("How many messages do you wish to enter? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(scanner.nextLine().trim());
            if (numMessages <= 0) {
                System.out.println("Please enter a positive number.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number. Returning to menu.");
            return;
        }

        Message tracker = new Message(); // used for totals / print

        for (int i = 1; i <= numMessages; i++) {
            System.out.println();
            System.out.println("--- Message " + i + " of " + numMessages + " ---");

            Message msg = new Message();
            msg.setMessageNumber(i - 1); // brief examples start from 0 in hash

            // Recipient
            System.out.print("Enter recipient cell number (e.g. +27718693002): ");
            String recipient = scanner.nextLine().trim();
            msg.setRecipient(recipient);
            System.out.println(msg.checkRecipientCell());

            // Only continue if recipient is valid (optional hard stop – brief does not force it)
            // Message text
            System.out.print("Enter message (max 250 characters): ");
            String text = scanner.nextLine();
            msg.setMessageText(text);

            String lengthCheck = msg.validateMessageLength();
            System.out.println(lengthCheck);
            if (!lengthCheck.equals("Message ready to send.")) {
                System.out.println("Message skipped due to length.");
                continue;
            }

            // Create hash
            String hash = msg.createMessageHash();
            System.out.println("Message Hash: " + hash);

            // Show Message ID
            System.out.println("Message ID generated: " + msg.getMessageID());

            // Send / Disregard / Store choice
            System.out.println();
            System.out.println("What would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Enter choice (1-3): ");
            String action = scanner.nextLine().trim();

            String result = msg.SentMessage(action);
            System.out.println(result);

            // Display full details after processing (especially when sent)
            if ("Sent".equals(msg.getFlag()) || "Stored".equals(msg.getFlag())) {
                System.out.println();
                System.out.println(msg.getFullDetails());
            }
        }

        // Total messages sent
        System.out.println();
        System.out.println("Total number of messages sent: " + tracker.returnTotalMessagess());
        System.out.println();
    }
}
