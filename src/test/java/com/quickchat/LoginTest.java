package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Login class (Part 1 functionality still required).
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    @Test
    void testUsernameCorrectlyFormatted() {
        assertEquals("Username successfully captured.", login.getUsernameMessage("kyl_1"));
    }

    @Test
    void testUsernameIncorrectlyFormatted() {
        assertEquals(
            "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.",
            login.getUsernameMessage("kyle!!!!!!!")
        );
    }

    @Test
    void testPasswordMeetsComplexity() {
        assertEquals("Password successfully captured.", login.getPasswordMessage("Ch&&sec@ke99!"));
    }

    @Test
    void testPasswordDoesNotMeetComplexity() {
        assertEquals(
            "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.",
            login.getPasswordMessage("password")
        );
    }

    @Test
    void testCellPhoneCorrect() {
        assertEquals("Cell phone number successfully added.", login.getCellPhoneMessage("+27838968976"));
    }

    @Test
    void testCellPhoneIncorrect() {
        assertEquals(
            "Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.",
            login.getCellPhoneMessage("08966553")
        );
    }

    @Test
    void testLoginSuccessful() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFailed() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertFalse(login.loginUser("kyl_1", "wrongPassword"));
    }

    @Test
    void testCheckUserNameTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void testCheckUserNameFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void testCheckPasswordTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void testCheckPasswordFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void testCheckCellTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void testCheckCellFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}
