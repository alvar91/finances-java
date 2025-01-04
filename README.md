# Console Application for Personal Finance Management

## Description
This console application is designed for personal finance management. It allows users to track their income and expenses, set category limits, and generate reports about their finances.

## Installation

### 1. Clone the repository

### 2. Build the project in IntelliJ IDEA or using Maven

- **Using IntelliJ IDEA**:  
  Open the project in IntelliJ IDEA, and it should automatically detect and configure the project structure. Then, click on the "Build" menu and choose "Build Project."

- **Using Maven**:  
  Navigate to the project folder in the terminal and run the following command to build the project:

  ```bash
  mvn clean install
  ```

This will download all the necessary dependencies and compile the project.

### 3. Commands Supported by the Service
- `cw` - create a new wallet;
- `chgw` - switch wallet;
- `dw` - delete a wallet;
- `ainc` - record income;
- `aexp` - record expenses;
- `bal` - get current balance information;
- `increp` - get income report;
- `exprep` - get expenses report;
- `frep` - get full report;
- `cb` - set budget for a category;
- `wtrans` - transfer funds to another wallet;
- `utrans` - transfer funds to another user;
- `lo` - log out of the account.
