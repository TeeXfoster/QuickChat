package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = new Login();
    }

    @Test
    void testUsernameCorrect() {
        assertEquals("Username successfully captured.", login.getUsernameMessage("kyl_1"));
    }

    @Test
    void testUsernameIncorrect() {
        assertTrue(login.getUsernameMessage("kyle!!!!!!!").contains("not correctly formatted"));
    }

    @Test
    void testPasswordCorrect() {
        assertEquals("Password successfully captured.", login.getPasswordMessage("Ch&&sec@ke99!"));
    }

    @Test
    void testPasswordIncorrect() {
        assertTrue(login.getPasswordMessage("password").contains("not correctly formatted"));
    }

    @Test
    void testCellCorrect() {
        assertEquals("Cell phone number successfully added.", login.getCellPhoneMessage("+27838968976"));
    }

    @Test
    void testCellIncorrect() {
        assertTrue(login.getCellPhoneMessage("08966553").contains("incorrectly formatted"));
    }

    @Test
    void testLoginSuccess() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void testLoginFail() {
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertFalse(login.loginUser("kyl_1", "wrong"));
    }
}
