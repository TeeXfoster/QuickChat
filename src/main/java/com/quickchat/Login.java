package com.quickchat;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Login class (Part 1) – reused in Part 2 and Part 3.
 * Cell-phone regex: South African numbers with +27.
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
        boolean hasCapital = false, hasNumber = false, hasSpecial = false;
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
        return pattern.matcher(cellPhone).matches();
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
        if (!isRegistered || storedUsername == null || storedPassword == null) return false;
        boolean success = storedUsername.equals(username) && storedPassword.equals(password);
        this.isLoggedIn = success;
        return success;
    }

    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }

    public String getUsernameMessage(String username) {
        return checkUserName(username)
            ? "Username successfully captured."
            : "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
    }

    public String getPasswordMessage(String password) {
        return checkPasswordComplexity(password)
            ? "Password successfully captured."
            : "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
    }

    public String getCellPhoneMessage(String cellPhone) {
        return checkCellPhoneNumber(cellPhone)
            ? "Cell phone number successfully added."
            : "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
    }

    public boolean isLoggedIn() { return isLoggedIn; }
    public boolean isRegistered() { return isRegistered; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getStoredUsername() { return storedUsername; }
    public String getStoredCellPhone() { return storedCellPhone; }
}
