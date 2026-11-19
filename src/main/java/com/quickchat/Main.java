package com.quickchat;

import java.util.Scanner;

/**
 * Console application for QuickChat – Final PoE (Part 1 + 2 + 3).
 * No GUI / JOptionPane.
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("========================================");
        System.out.println("        QuickChat Application");
        System.out.println("========================================\n");

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
            System.out.println("Login failed. You cannot use messaging features. Exiting.");
            scanner.close();
            return;
        }

        // Load any previously stored messages from JSON
        Message.loadStoredMessagesFromJSON();

        System.out.println("Welcome to QuickChat.\n");

        boolean running = true;
        while (running) {
            System.out.println("Please choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Enter choice (1-4): ");

            String menuChoice = scanner.nextLine().trim();

            switch (menuChoice) {
                case "1":
                    sendMessagesFlow(scanner, login);
                    break;
                case "2":
                    System.out.println("Coming Soon.\n");
                    break;
                case "3":
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    break;
                case "4":
                    storedMessagesMenu(scanner);
                    break;
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
        scanner.close();
    }

    private static void sendMessagesFlow(Scanner scanner, Login login) {
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

        Message tracker = new Message();

        for (int i = 1; i <= numMessages; i++) {
            System.out.println("\n--- Message " + i + " of " + numMessages + " ---");
            Message msg = new Message();
            msg.setMessageNumber(i - 1);
            msg.setSender(login.getFirstName() + " " + login.getLastName());

            System.out.print("Enter recipient cell number (e.g. +27718693002): ");
            String recipient = scanner.nextLine().trim();
            msg.setRecipient(recipient);
            System.out.println(msg.checkRecipientCell());

            System.out.print("Enter message (max 250 characters): ");
            String text = scanner.nextLine();
            msg.setMessageText(text);

            String lengthCheck = msg.validateMessageLength();
            System.out.println(lengthCheck);
            if (!lengthCheck.equals("Message ready to send.")) {
                System.out.println("Message skipped due to length.");
                continue;
            }

            String hash = msg.createMessageHash();
            System.out.println("Message Hash: " + hash);
            System.out.println("Message ID generated: " + msg.getMessageID());

            System.out.println("\nWhat would you like to do with this message?");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");
            System.out.print("Enter choice (1-3): ");
            String action = scanner.nextLine().trim();

            String result = msg.SentMessage(action);
            System.out.println(result);

            if ("Sent".equals(msg.getFlag()) || "Stored".equals(msg.getFlag())) {
                System.out.println("\n" + msg.getFullDetails());
            }
        }

        System.out.println("\nTotal number of messages sent: " + tracker.returnTotalMessagess() + "\n");
    }

    /** Part 3 – Stored Messages sub-menu */
    private static void storedMessagesMenu(Scanner scanner) {
        // Refresh from JSON in case messages were stored earlier
        Message.loadStoredMessagesFromJSON();

        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Stored Messages Menu ---");
            System.out.println("a) Display sender and recipient of all stored messages");
            System.out.println("b) Display the longest stored message");
            System.out.println("c) Search for a message ID");
            System.out.println("d) Search messages by recipient");
            System.out.println("e) Delete a message using message hash");
            System.out.println("f) Display full report of stored messages");
            System.out.println("0) Back to main menu");
            System.out.print("Enter choice: ");

            String choice = scanner.nextLine().trim().toLowerCase();

            switch (choice) {
                case "a":
                    System.out.println("\n" + Message.displaySenderAndRecipient());
                    break;
                case "b":
                    System.out.println("\nLongest message:\n" + Message.displayLongestMessage());
                    break;
                case "c":
                    System.out.print("Enter Message ID to search: ");
                    String id = scanner.nextLine().trim();
                    System.out.println("\n" + Message.searchByMessageID(id));
                    break;
                case "d":
                    System.out.print("Enter recipient number: ");
                    String recip = scanner.nextLine().trim();
                    System.out.println("\n" + Message.searchByRecipient(recip));
                    break;
                case "e":
                    System.out.print("Enter Message Hash to delete: ");
                    String hash = scanner.nextLine().trim();
                    System.out.println("\n" + Message.deleteByHash(hash));
                    break;
                case "f":
                    System.out.println("\n" + Message.displayReport());
                    break;
                case "0":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
