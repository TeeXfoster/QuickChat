package com.quickchat;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * Login class for the QuickChat application (Part 1).
 * Handles username, password and cell-phone validation,
 * user registration and login.
 *
 * Cell-phone regex is based on common patterns for South African
 * numbers that include the international country code (+27).
 * Reference: Oracle Java Pattern documentation and standard SA mobile
 * number format research (https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html).
 */
public class Login {

    // Stored registration details (used for login verification)
    private String storedUsername;
    private String storedPassword;
    private String storedCellPhone;
    private String firstName;
    private String lastName;

    // Track whether a successful registration has occurred
    private boolean isRegistered = false;

    /**
     * Checks that the username contains an underscore and is no more than five characters long.
     * @param username the username to validate
     * @return true if the username is correctly formatted, false otherwise
     */
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        // Must contain underscore AND length <= 5
        return username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks that the password meets complexity rules:
     * - At least 8 characters long
     * - Contains a capital letter
     * - Contains a number
     * - Contains a special character
     * @param password the password to validate
     * @return true if the password meets all complexity requirements
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecial = true;
            }
        }

        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Checks that the cell phone number contains the international country code
     * followed by the number, and that the total length is appropriate.
     *
     * Regex pattern explanation:
     * ^\\+27          - must start with +27 (South African international code)
     * [0-9]{9,10}     - followed by 9 or 10 digits (SA mobile numbers are typically 9 digits after the code)
     * $               - end of string
     *
     * The brief states "no more than ten characters long" for the number portion,
     * and requires the international code. The pattern enforces +27 followed by
     * a reasonable number of digits.
     *
     * Reference: Oracle Java Pattern API and South African mobile number format.
     *
     * @param cellPhone the cell phone number to validate
     * @return true if the cell phone number is correctly formatted
     */
    public boolean checkCellPhoneNumber(String cellPhone) {
        if (cellPhone == null) {
            return false;
        }

        // Pattern: starts with +27 and is followed by digits.
        // Total length after +27 should keep the whole string reasonable.
        // Brief example: +27838968976 (12 characters total)
        Pattern pattern = Pattern.compile("^\\+27[0-9]{9,10}$");
        Matcher matcher = pattern.matcher(cellPhone);
        return matcher.matches();
    }

    /**
     * Registers a user after validating username, password and cell phone.
     * Returns the appropriate messaging as specified in the brief.
     *
     * @param username  the desired username
     * @param password  the desired password
     * @param cellPhone the cell phone number
     * @param firstName user's first name (used later in welcome message)
     * @param lastName  user's last name  (used later in welcome message)
     * @return registration status message
     */
    public String registerUser(String username, String password, String cellPhone,
                               String firstName, String lastName) {

        // Validate username first
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        // Validate password
        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        // Validate cell phone
        if (!checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        // All conditions met – store the details
        this.storedUsername = username;
        this.storedPassword = password;
        this.storedCellPhone = cellPhone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isRegistered = true;

        // The brief shows individual success messages for each field.
        // For the overall registration success we return a combined message.
        // Individual field success messages are available via the check methods
        // and are used in unit tests / console output.
        return "Username successfully captured.\nPassword successfully captured.\nCell phone number successfully added.\nUser registered successfully.";
    }

    /**
     * Convenience overload used by unit tests that only supply the three core fields.
     */
    public String registerUser(String username, String password, String cellPhone) {
        return registerUser(username, password, cellPhone, "User", "Name");
    }

    /**
     * Verifies that the login details entered match the details stored at registration.
     * @param username the username entered at login
     * @param password the password entered at login
     * @return true if credentials match, false otherwise
     */
    public boolean loginUser(String username, String password) {
        if (!isRegistered || storedUsername == null || storedPassword == null) {
            return false;
        }
        return storedUsername.equals(username) && storedPassword.equals(password);
    }

    /**
     * Returns the appropriate login status messaging.
     * @param username the username that was attempted
     * @param password the password that was attempted
     * @return welcome message on success, error message on failure
     */
    public String returnLoginStatus(String username, String password) {
        if (loginUser(username, password)) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        } else {
            return "Username or password incorrect, please try again.";
        }
    }

    // ---------- Helper methods that return the exact success / failure strings
    // required by the unit-test table in the brief ----------

    /**
     * Returns the exact username capture message (used by unit tests).
     */
    public String getUsernameMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        } else {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }
    }

    /**
     * Returns the exact password capture message (used by unit tests).
     */
    public String getPasswordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        } else {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }
    }

    /**
     * Returns the exact cell-phone capture message (used by unit tests).
     * Note: the brief shows two slightly different wordings; both are supported.
     */
    public String getCellPhoneMessage(String cellPhone) {
        if (checkCellPhoneNumber(cellPhone)) {
            return "Cell phone number successfully added.";
        } else {
            // Matching the longer form that appears in the unit-test table
            return "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.";
        }
    }

    // Getters useful for testing / later parts
    public String getStoredUsername() { return storedUsername; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public boolean isRegistered() { return isRegistered; }
}
