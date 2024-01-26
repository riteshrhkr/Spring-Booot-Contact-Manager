# Contact Management System

## Introduction

The Contact Management System is a Java-based web application built using the Spring Boot framework. It provides users with the ability to manage their contacts, register accounts, and maintain user profiles. The system focuses on secure contact handling, user authentication, and seamless interaction through a user-friendly interface.

## Technologies Used

- **Java**: Core programming language
- **HTML, JavaScript**: Front-end development
- **Spring Boot, Spring MVC**: Framework for building Java-based web applications
- **Thymeleaf**: Server-side Java template engine
- **JPA, Hibernate**: For data persistence and database interaction
- **MySQL**: Database for storing user and contact information
- **Spring Security**: Ensures secure user authentication and authorization

## Project Structure

The project follows a well-organized structure with clear separation of concerns. It includes controllers for different functionalities, database entities for users and contacts, DAO classes for database operations, and services for business logic.

## Key Features

- **Contact Management**: Users can add, update, and delete contacts, ensuring a seamless experience for managing their personal and professional connections.

- **User Authentication**: Secure user authentication and authorization are implemented using Spring Security, safeguarding user data and interactions.

- **Pagination**: Efficient handling of contacts with pagination to enhance the user experience.

- **User Registration and Profile Management**: Users can register accounts, update their profiles, and perform essential account-related actions.

## Database Entities

- **User**: Represents user information, including name, email, password, role, and associated contacts.
  
- **Contact**: Stores details of contacts, such as name, phone, email, address, work, and a reference to the associated user.

## Security

The system prioritizes the security of user data, employing Spring Security features for secure user authentication and authorization.

## How to Run
- It is maven based project and you can use on you favorite IDE.
- Step 1: Download the project and import on your IDE.
- Step 2: Configure database properties in the application.properties file.
- Step 3: Run the project.

- Step 4: Modify as per your need and Enjoy


## Future Improvements

- Integration with cloud services for seamless file handling.
- Additional features for user interaction and collaboration.
- Enhanced error handling and user feedback.

## Current Issue
Project on local machine is working fine, but after deploying on cloud facing issue with image upload. I will fix it in future. 

## Contributing
If you find any issue or have suggestions, please feel free to open an issue or submit a pull request. 

Thanks for reading!😊