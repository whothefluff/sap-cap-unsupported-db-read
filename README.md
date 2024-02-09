# sap_cap_unsupported_db_read

## Description
This project is an (almost) self-contained CAP Java application designed to demonstrate accessing a database engine not natively supported by standard SAP tools. Although SAP CAP officially supports only HANA and PostgreSQL, this project illustrates a successful implementation for reading from a MySQL database by leveraging regular Spring Boot dependencies (JPQL with fields mapped to a JPA entity). This serves as proof of concept that integrating with unsupported databases is feasible using conventional Spring Boot Data approaches.

## Installation

### Prerequisites
- Maven
- MySQL database setup

### Setup Instructions
1. Ensure Maven and MySQL are correctly installed on your system.
2. Clone this repository to your local environment.
3. Execute the `some_schema.sql` script in a MySQL console to create the necessary table structure and insert initial data. This script is included within the project files.
4. Run `mvn clean install` from your terminal or command line within the project directory. This command compiles the application and manages all necessary dependencies.

## Usage
After the application has been initialized, it operates similarly to any standard Spring project. The primary endpoint becomes available and can typically be accessed via:

http://localhost:8080/odata/v4/ExternalCatalogService

For convenience, a `.rest` file with example calls is provided to demonstrate how to interact with the application.