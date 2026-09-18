package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Login class (Part 1).
 * Test data and expected responses are taken directly from the PROG5121 brief.
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    // =========================================================================
    // assertEquals tests – Username formatting
    // =========================================================================

    @Test
    void testUsernameCorrectlyFormatted() {
        // Test Data: “kyl_1”
        // Expected: “Username successfully captured.”
        String result = login.getUsernameMessage("kyl_1");
        assertEquals("Username successfully captured.", result);
    }

    @Test
    void testUsernameIncorrectlyFormatted() {
        // Test Data: “kyle!!!!!!!”
        // Expected: “Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.”
        String result = login.getUsernameMessage("kyle!!!!!!!");
        assertEquals(
            "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
            result
        );
    }

    // =========================================================================
    // assertEquals tests – Password complexity
    // =========================================================================

    @Test
    void testPasswordMeetsComplexityRequirements() {
        // Test Data: “Ch&&sec@ke99!”
        // Expected: “Password successfully captured.”
        String result = login.getPasswordMessage("Ch&&sec@ke99!");
        assertEquals("Password successfully captured.", result);
    }

    @Test
    void testPasswordDoesNotMeetComplexityRequirements() {
        // Test Data: “password”
        // Expected: “Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.”
        String result = login.getPasswordMessage("password");
        assertEquals(
            "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
            result
        );
    }

    // =========================================================================
    // assertEquals tests – Cell phone number
    // =========================================================================

    @Test
    void testCellPhoneCorrectlyFormatted() {
        // Test Data: +27838968976
        // Expected: “Cell phone number successfully added.”  (or the “successfully captured” wording)
        String result = login.getCellPhoneMessage("+27838968976");
        // The brief shows both “successfully added” and “successfully captured”.
        // Our implementation uses the “successfully added” form for the True case.
        assertEquals("Cell phone number successfully added.", result);
    }

    @Test
    void testCellPhoneIncorrectlyFormatted() {
        // Test Data: 08966553
        // Expected: “Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.”
        String result = login.getCellPhoneMessage("08966553");
        assertEquals(
            "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
            result
        );
    }

    // =========================================================================
    // assertTrue / assertFalse tests
    // =========================================================================

    @Test
    void testLoginSuccessful() {
        // Register a valid user first
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        // Then attempt login with the same credentials
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFailed() {
        // Register a valid user
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        // Attempt login with wrong password
        assertFalse(login.loginUser("kyl_1", "wrongPassword"));
    }

    @Test
    void testUsernameCorrectlyFormatted_Boolean() {
        // “Username correctly formatted” → True
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void testUsernameIncorrectlyFormatted_Boolean() {
        // “Username incorrectly formatted” → False
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void testPasswordMeetsComplexity_Boolean() {
        // “Password meets complexity requirements” → True
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void testPasswordDoesNotMeetComplexity_Boolean() {
        // “Password does not meet complexity requirements” → False
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void testCellPhoneCorrectlyFormatted_Boolean() {
        // “Cell phone number correctly formatted” → True
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void testCellPhoneIncorrectlyFormatted_Boolean() {
        // “Cell phone number incorrectly formatted” → False
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }

    // =========================================================================
    // Additional useful tests for returnLoginStatus
    // =========================================================================

    @Test
    void testReturnLoginStatus_Success() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        String status = login.returnLoginStatus("kyl_1", "Ch&&sec@ke99!");
        assertEquals("Welcome Kyle, Smith it is great to see you again.", status);
    }

    @Test
    void testReturnLoginStatus_Failure() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        String status = login.returnLoginStatus("wrong", "wrong");
        assertEquals("Username or password incorrect, please try again.", status);
    }
}
