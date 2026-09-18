# QuickChat – Part 1: Registration and Login Feature

**Module:** PROGRAMMING 1A (PROG5121)  
**Assessment:** PoE – Part 1

## Overview

This is a console-only Java application that implements:

- User registration (username, password, South African cell-phone number)
- Validation rules exactly as specified in the brief
- Login authentication
- Unit tests with the exact test data from the marking rubric

## Project Structure

```
QuickChat-Part1/
├── pom.xml
├── README.md
├── src/
│   ├── main/java/com/quickchat/
│   │   ├── Login.java      ← All required methods
│   │   └── Main.java       ← Console driver
│   └── test/java/com/quickchat/
│       └── LoginTest.java  ← JUnit 5 tests (exact test data)
```

## Requirements Implemented

| Requirement | Method / Feature |
|-------------|------------------|
| Username contains `_` and ≤ 5 characters | `checkUserName()` |
| Password complexity (8+ chars, capital, number, special) | `checkPasswordComplexity()` |
| Cell phone with international code (regex) | `checkCellPhoneNumber()` |
| Registration messaging | `registerUser()` + helper message methods |
| Login verification | `loginUser()` |
| Login status messages | `returnLoginStatus()` |

## How to Run

### Prerequisites
- JDK 17+
- Maven 3.8+

### Compile & Run the console application
```bash
mvn clean compile
mvn exec:java
```

### Run the unit tests
```bash
mvn test
```

All 16 tests should pass.

## Test Data Used (from the brief)

| Test | Input | Expected Result |
|------|-------|-----------------|
| Username correct | `kyl_1` | Username successfully captured. |
| Username incorrect | `kyle!!!!!!!` | Username is not correctly formatted… |
| Password correct | `Ch&&sec@ke99!` | Password successfully captured. |
| Password incorrect | `password` | Password is not correctly formatted… |
| Cell correct | `+27838968976` | Cell phone number successfully added. |
| Cell incorrect | `08966553` | Cell number is incorrectly formatted… |

## Cell-phone Regex Attribution

The regular expression used in `checkCellPhoneNumber()` follows the standard South African mobile format with the international country code (`+27`).

Pattern: `^\+27[0-9]{9,10}$`

Reference: Oracle Java Pattern documentation  
https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html

## Next Steps (Part 2 & PoE)

- Create a feature branch: `git checkout -b feature/part2-messages`
- Add Message class, loops, JSON storage, arrays, etc.
- Add GitHub Actions workflow for automated testing

## GitHub Submission Notes

- Push this project to a GitHub repository (use your Connect account)
- Make **at least 6 commits** for Part 1
- Submit the GitHub link on Arc
- Record the required video presentation with your own voice-over
