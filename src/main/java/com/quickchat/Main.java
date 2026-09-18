package com.quickchat;

import java.util.Scanner;

/**
 * Console driver for Part 1 – Registration and Login feature.
 * All interaction is done via the console (no GUI / JOptionPane).
 */
public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();

        System.out.println("========================================");
        System.out.println("   QuickChat - Registration & Login");
        System.out.println("========================================");
        System.out.println();

        // ---------- REGISTRATION ----------
        System.out.println("--- CREATE ACCOUNT ---");
        System.out.println();

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
        String registrationResult = login.registerUser(username, password, cellPhone, firstName, lastName);
        System.out.println(registrationResult);
        System.out.println();

        if (!login.isRegistered()) {
            System.out.println("Registration failed. Please restart the application and try again.");
            scanner.close();
            return;
        }

        // ---------- LOGIN ----------
        System.out.println("--- LOGIN ---");
        System.out.println();

        System.out.print("Enter username: ");
        String loginUsername = scanner.nextLine().trim();

        System.out.print("Enter password: ");
        String loginPassword = scanner.nextLine().trim();

        System.out.println();
        System.out.println(login.returnLoginStatus(loginUsername, loginPassword));

        scanner.close();
    }
}
