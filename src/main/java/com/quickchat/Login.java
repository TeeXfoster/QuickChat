package com.quickchat;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Login class for QuickChat (Part 1 functionality reused in Part 2).
 * Handles username, password and cell-phone validation, registration and login.
 *
 * Cell-phone regex based on South African numbers with international code (+27).
 * Reference: Oracle Java Pattern documentation
 * https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html
 */
public class Login {

    private String storedUsername;
    private String storedPassword;
    private String storedCellPhone;
    private String firstName;
    private String lastName;
    private boolean isRegistered = false;
    private boolean isLoggedIn = false;

    public boolean checkUserName(String username) {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) return false;

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasCapital = true;
            else if (Character.isDigit(c)) hasNumber = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    public boolean checkCellPhoneNumber(String cellPhone) {
        if (cellPhone == null) return false;
        Pattern pattern = Pattern.compile("^\\+27[0-9]{9,10}$");
        Matcher matcher = pattern.matcher(cellPhone);
        return matcher.matches();
    }

    public String registerUser(String username, String password, String cellPhone,
                               String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isRegistered = true;

        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.\nUser registered successfully.";
    }

    public String registerUser(String username, String password, String cellPhone) {
        return registerUser(username, password, cellPhone, "User", "Name");
    }

    public boolean loginUser(String username, String password) {
        if (!isRegistered || storedUsername == null || storedPassword == null) {
            return false;
        }
        boolean success = storedUsername.equals(username) && storedPassword.equals(password);
        this.isLoggedIn = success;
        return success;
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    public String getUsernameMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    public String getPasswordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    public String getCellPhoneMessage(String cellPhone) {
        if (checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number successfully added.";
        }
        return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getStoredUsername() { return storedUsername; }
}
