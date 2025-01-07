# Advertising System Management Platform

## Overview
The Advertising System Management Platform is a robust and scalable solution designed to streamline the lifecycle of advertisements. This includes submission, validation, approval/rejection, and storage, ensuring seamless collaboration between advertisers, editors, and marketing departments. The system supports multithreading, modular design, and centralized logging for effective and efficient operations.

## Features

### Advertiser Management
- Register advertisers with unique IDs, contact details, and address.
- Advertisers can submit details and manage their advertisements.

### Advertisement Lifecycle
- **Status Tracking:** PENDING, VALIDATED, APPROVED, REJECTED.
- Content validation with detailed error handling.
- Automated placement in approval or revision queues based on status.

### Employee Roles
- **Editor:** Reviews advertisements and decides approval/rejection.
- **Marketing Department:** Validates and submits advertisements for review.

### Command Pattern
- Execute, undo, and manage commands for advertisement reviews using a clean and maintainable command queue architecture.

### Centralized Logging
- Log every key event and error to a file for transparency and debugging.

### System State Management
- Monitor operational status and available storage to ensure optimal performance.

## Architecture
The platform is built with a modular design that includes the following core components:

- **Logger Utility:** Centralized logging using `java.util.logging`.
- **System Manager:** Orchestrates commands and maintains execution history.
- **Processing Center:** Handles advertisement storage and queue management.
- **Employee Hierarchy:** Abstract class for all employee roles, enabling polymorphism.
- **Advertiser & Advertisement:** Classes to manage ad content, publication, and validation.

## Technology Stack
- **Language:** Java 11+
- **Build Tool:** Gradle
- **Dependencies:** Standard Java libraries for concurrency, logging, and collections.

## Example Workflow

### Advertiser Registration
Advertisers can register with their details and submit advertisements.

### Advertisement Submission
Advertisements are validated and placed in the appropriate queue based on status.

### Approval/Rejection
Editors review ads and decide their outcome, which is logged for auditing.

### Revision Handling
Rejected ads are queued for revision, ensuring efficient feedback loops.

---

## Directory Structure
```scss
├── src/main/java
│   ├── com.example.gradleproject1
│   │   └── App.java                // Main application class containing all logic
├── src/test/java
│   ├── com.example.gradleproject1
│   │   ├── AdvertisementTest.java  // Unit tests for advertisement logic
│   │   └── ProcessingCenterTest.java // Unit tests for processing center logic
├── build.gradle
└── README.md
```

## License

This project is licensed under the [MIT License](LICENSE).  
See the `LICENSE` file for more details.




 
