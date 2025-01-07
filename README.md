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

## Quick Start

### Prerequisites
1. Install Java 11 or higher.
2. Install Gradle.



 
