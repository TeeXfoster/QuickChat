# QuickChat – Final PoE (Part 3)

**Module:** PROGRAMMING 1A (PROG5121)  
**Assessment:** PoE – Part 3 (Store Data and Display Task Report)

## Features

### Part 1
- Registration & Login with validation

### Part 2
- Send / Disregard / Store messages
- Message ID, Hash, JSON storage
- Menu options 1–3

### Part 3 (new)
- Arrays: Sent, Disregarded, Stored, Message Hashes, Message IDs
- Menu option **4) Stored Messages** with:
  - a) Display sender & recipient of stored messages
  - b) Display longest stored message
  - c) Search by Message ID
  - d) Search by recipient
  - e) Delete by Message Hash
  - f) Full report of stored messages

## How to Run

```bash
mvn clean compile
mvn exec:java
mvn test
```

## Test Data (from brief)

| # | Recipient        | Message                                              | Flag      |
|---|------------------|------------------------------------------------------|-----------|
| 1 | +27834557896     | Did you get the cake?                                | Sent      |
| 2 | +27838884567     | Where are you? You are late! I have asked you to be on time. | Stored |
| 3 | +27834484567     | Yohoooo, I am at your gate.                          | Disregard |
| 4 | 0838884567       | It is dinner time !                                  | Sent      |
| 5 | +27838884567     | Ok, I am leaving without you.                        | Stored    |
