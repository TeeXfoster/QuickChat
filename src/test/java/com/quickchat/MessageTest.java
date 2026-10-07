package com.quickchat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Part 2 – Message class.
 * Test data taken directly from the PROG5121 brief.
 */
public class MessageTest {

    @BeforeEach
    void setUp() {
        Message.resetCounters();
    }

    // ---------- Message length tests ----------

    @Test
    void testMessageLengthSuccess() {
        Message msg = new Message();
        msg.setMessageText("Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.validateMessageLength());
    }

    @Test
    void testMessageLengthFailure() {
        Message msg = new Message();
        // Create a string longer than 250 characters
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            longMsg.append("This is a long message. ");
        }
        msg.setMessageText(longMsg.toString());
        String result = msg.validateMessageLength();
        assertTrue(result.startsWith("Message exceeds 250 characters by "));
        assertTrue(result.contains("please reduce the size."));
    }

    // ---------- Recipient tests ----------

    @Test
    void testRecipientCorrectlyFormatted() {
        Message msg = new Message();
        msg.setRecipient("+27718693002");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    void testRecipientIncorrectlyFormatted() {
        Message msg = new Message();
        msg.setRecipient("08575975889");
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            msg.checkRecipientCell()
        );
    }

    // ---------- Message Hash test (exact data from brief) ----------

    @Test
    void testMessageHashCorrect() {
        // Test Case 1 data from brief:
        // Message: “Hi Mike, can you join us for dinner tonight?”
        // Expected hash style: 00:0:HITONIGHT  (first two of ID : number : first+last word CAPS)
        Message msg = new Message();
        msg.setMessageID("0012345678"); // first two digits = 00
        msg.setMessageNumber(0);
        msg.setMessageText("Hi Mike, can you join us for dinner tonight?");
        String hash = msg.createMessageHash();
        assertEquals("00:0:HITONIGHT", hash);
    }

    // ---------- Message ID test ----------

    @Test
    void testMessageIDCreated() {
        Message msg = new Message();
        assertNotNull(msg.getMessageID());
        assertTrue(msg.checkMessageID());
        assertEquals(10, msg.getMessageID().length());
    }

    // ---------- SentMessage options ----------

    @Test
    void testSendMessageOption() {
        Message msg = new Message();
        msg.setRecipient("+27718693002");
        msg.setMessageText("Hi Mike, can you join us for dinner tonight?");
        msg.setMessageNumber(0);
        msg.createMessageHash();
        String result = msg.SentMessage("1");
        assertEquals("Message successfully sent.", result);
        assertEquals(1, msg.returnTotalMessagess());
    }

    @Test
    void testDisregardMessageOption() {
        Message msg = new Message();
        msg.setRecipient("+27718693002");
        msg.setMessageText("Test");
        String result = msg.SentMessage("2");
        assertEquals("Press 0 to delete the message.", result);
    }

    @Test
    void testStoreMessageOption() {
        Message msg = new Message();
        msg.setRecipient("+27718693002");
        msg.setMessageText("Hi Mike, can you join us for dinner tonight?");
        msg.setMessageNumber(0);
        msg.createMessageHash();
        String result = msg.SentMessage("3");
        assertEquals("Message successfully stored.", result);
    }

    // ---------- Integration style test using brief Test Data ----------

    @Test
    void testFullFlowTestData1() {
        // Num Messages = 2
        // Message 1: Recipient +27718693002, Message “Hi Mike, can you join us for dinner tonight?”, Select Send
        Message msg1 = new Message();
        msg1.setMessageID("0012345678");
        msg1.setMessageNumber(0);
        msg1.setRecipient("+27718693002");
        msg1.setMessageText("Hi Mike, can you join us for dinner tonight?");

        assertEquals("Message ready to send.", msg1.validateMessageLength());
        assertEquals("Cell phone number successfully captured.", msg1.checkRecipientCell());
        assertEquals("00:0:HITONIGHT", msg1.createMessageHash());

        String sendResult = msg1.SentMessage("1");
        assertEquals("Message successfully sent.", sendResult);
        assertEquals(1, msg1.returnTotalMessagess());
    }

    @Test
    void testFullFlowTestData2_Discard() {
        // Message 2: Recipient 08575975889 (invalid), Message “Hi Keegan, did you receive the payment?”, Select Discard
        Message msg2 = new Message();
        msg2.setMessageNumber(1);
        msg2.setRecipient("08575975889");
        msg2.setMessageText("Hi Keegan, did you receive the payment?");

        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            msg2.checkRecipientCell()
        );

        String discardResult = msg2.SentMessage("2");
        assertEquals("Press 0 to delete the message.", discardResult);
        // Total sent should still be 0 because we discarded
        assertEquals(0, msg2.returnTotalMessagess());
    }
}
