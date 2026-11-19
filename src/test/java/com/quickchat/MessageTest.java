package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Part 2 + Part 3.
 * Test data taken from the PROG5121 brief.
 */
public class MessageTest {

    @BeforeEach
    void setUp() {
        Message.resetAll();
        Message.populateTestData();
    }

    // ---------- Part 2 style tests ----------

    @Test
    void testMessageLengthSuccess() {
        Message msg = new Message();
        msg.setMessageText("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.validateMessageLength());
    }

    @Test
    void testRecipientCorrect() {
        Message msg = new Message();
        msg.setRecipient("+27718693002");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    void testRecipientIncorrect() {
        Message msg = new Message();
        msg.setRecipient("08575975889");
        assertTrue(msg.checkRecipientCell().contains("incorrectly formatted"));
    }

    @Test
    void testMessageHash() {
        Message msg = new Message();
        msg.setMessageID("0012345678");
        msg.setMessageNumber(0);
        msg.setMessageText("Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", msg.createMessageHash());
    }

    @Test
    void testSendOption() {
        Message.resetAll();
        Message msg = new Message();
        msg.setRecipient("+27718693002");
        msg.setMessageText("Test");
        msg.setMessageNumber(0);
        msg.createMessageHash();
        assertEquals("Message successfully sent.", msg.SentMessage("1"));
        assertEquals(1, msg.returnTotalMessagess());
    }

    // ---------- Part 3 required tests (exact brief expectations) ----------

    @Test
    void testSentMessagesArrayPopulated() {
        // Brief: system returns “Did you get the cake?”, “It is dinner time!”
        String combined = "";
        for (Message m : Message.getSentMessages()) {
            combined += m.getMessageText() + " ";
        }
        assertTrue(combined.contains("Did you get the cake?"));
        assertTrue(combined.contains("It is dinner time !") || combined.contains("It is dinner time!"));
    }

    @Test
    void testDisplayLongestMessage() {
        // Brief expected: “Where are you? You are late! I have asked you to be on time.”
        String longest = Message.displayLongestMessage();
        assertEquals("Where are you? You are late! I have asked you to be on time.", longest);
    }

    @Test
    void testSearchByMessageID() {
        // Brief: message 4, ID “0838884567” → “It is dinner time!”
        String result = Message.searchByMessageID("0838884567");
        assertTrue(result.contains("It is dinner time"));
    }

    @Test
    void testSearchByRecipient() {
        // Brief: +27838884567 → the two stored messages for that number
        String result = Message.searchByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    void testDeleteByHash() {
        // Delete the message that has the long text (Message 2)
        Message m2 = null;
        for (Message m : Message.getStoredMessages()) {
            if (m.getMessageText().contains("Where are you?")) {
                m2 = m;
                break;
            }
        }
        assertNotNull(m2);
        String hash = m2.getMessageHash();
        String result = Message.deleteByHash(hash);
        assertTrue(result.contains("successfully deleted"));
        // After delete it should no longer be the longest in the remaining stored messages
        String longestAfter = Message.displayLongestMessage();
        assertFalse(longestAfter.contains("Where are you? You are late!"));
    }

    @Test
    void testDisplayReport() {
        String report = Message.displayReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time.")
                || report.contains("Ok, I am leaving without you."));
    }
}
