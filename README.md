# QuickChat – Part 2: Sending Messages

**Module:** PROGRAMMING 1A (PROG5121)  
**Assessment:** PoE – Part 2

## Overview

This project extends Part 1 (Registration & Login) with the full messaging feature required for Part 2.

### Features implemented

- Login required before sending messages
- Welcome message: “Welcome to QuickChat.”
- Numeric menu (1 = Send Messages, 2 = Coming Soon, 3 = Quit)
- User chooses how many messages to enter
- Each message has:
  - Unique 10-digit Message ID
  - Auto-generated message number
  - Recipient cell number (international code validated)
  - Message text (max 250 characters)
  - Message Hash (e.g. `00:0:HITONIGHT`)
- Options after creating a message: Send / Disregard / Store
- Messages can be stored in a JSON file
- Total number of messages sent is displayed
- Unit tests with the exact test data from the brief
- GitHub Actions workflow for automated testing

## Project Structure

```
QuickChat-Part2/
├── pom.xml
├── README.md
├── .gitignore
├── .github/workflows/maven.yml   ← GitHub Actions
└── src/
    ├── main/java/com/quickchat/
    │   ├── Login.java
    │   ├── Message.java
    │   └── Main.java
    └── test/java/com/quickchat/
        ├── LoginTest.java
        └── MessageTest.java
```

## How to Run

```bash
# Compile & run the console app
mvn clean compile
mvn exec:java

# Run all unit tests
mvn test
```

## Message Hash Format

`firstTwoDigitsOfID : messageNumber : FIRSTWORDLASTWORD` (all caps)

Example from brief test data:  
Message = “Hi Mike, can you join us for dinner tonight?”  
→ Hash = `00:0:HITONIGHT`

## JSON Storage

Stored messages are written to `stored_messages.json` using the **org.json** library.  
Attribution: https://github.com/stleary/JSON-java

## GitHub Notes

- Create a feature branch: `git checkout -b feature/part2-messages`
- Minimum 6 commits recommended
- GitHub Actions will automatically run tests on every push
