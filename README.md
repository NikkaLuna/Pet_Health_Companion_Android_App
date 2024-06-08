# Pet Health Companion Mobile Application

## Overview

The Pet Health Companion, developed in Android Studio, is a user-friendly platform designed to streamline pet care management. Serving as a centralized hub for scheduling, sharing, and receiving reminders for appointments, it aims to enhance the joy of pet ownership while minimizing the stress. 

Key features include appointment tracking, customizable reminders, report search functionality, and sharing options tailored to diverse pet owner needs.

- ## Video Demonstration

[Watch a video demonstration of the project](https://youtu.be/DzeBmGI4DZ0)

## Features

### Appointment and Pet Management

- **Add Appointments & Pets:** Users can add new pet healthcare appointments to the database.
- **Edit Appointments & Pets:** Users can modify existing appointments and pets.
- **Delete Appointments & Pets:** Users can remove appointments and pets from the database.


### Managing Pet Information and Notifications

![Pet Health Companion Notification](https://github.com/NikkaLuna/Pet_Health_Companion_Android_App/blob/master/Notifications.png)

*Users can set notifications for upcoming birthdays or appointments.*


### Sharing Pet Information

![Share Pet Functionality](https://github.com/NikkaLuna/Pet_Health_Companion_Android_App/blob/master/SharePet.png)

*Users can share information about an upcoming appointment or a pet via notepad, email, or text.*


### Search & Reports Page

![Pet Health Companion Interface](https://github.com/NikkaLuna/Pet_Health_Companion_Android_App/blob/master/SearchReport.png)

*Users can enter a pet's name to retrieve a detailed, timestamped report of past and upcoming appointments.*

## Security

- To safeguard the confidentiality of pet data, the application implements a secure authentication system utilizing industry-standard hashing algorithms.
- Users are required to log in with secure credentials, which are then hashed and stored using robust encryption techniques.  This prevents plaintext password storage.

## Unit Testing

**Pet & Appointment Management**:

  - **Addition and Deletion**: Simulates adding and deleting pets to validate data storage and removal mechanisms.
  - **Addition and Deletion**: Use mock appointments to evaluate data persistence and removal.
      
**Testing Framework**:
  - **JUnit and Mockito**: Employed for test script creation and integration testing to ensure overall functionality.
    
**Deliverables**:
  - **Test Scripts**: Written using JUnit and Mockito for automated verification.
  - **Test Results Report**: Detailed report of test outcomes, including successes, failures, and error messages.

## Challenge

During the initial development phase, the project packages in Android Studio were named 'demo', which violated the Google Play Console's package naming guidelines. Attempting to refactor all files within their respective packages using Android Studio proved challenging, as it disrupted the project's configurations and dependencies.

To address this issue, I took the following approach:

- **New Project Directory**: I created a new project directory with a compliant package name, adhering to the Google Play Console's naming conventions.
- **Clean Setup**: Within the new directory, I rebuilt the project from scratch, ensuring that all configurations and dependencies were correctly set up from the start.
- **Code Migration**: I carefully migrated the existing codebase to the new project, maintaining the project's structure and functionality.
- **Testing and Validation**: I conducted thorough testing and validation to ensure that the migrated project functioned as expected, without any regressions or issues.

By following this approach, I successfully resolved the package naming issue and ensured compliance with the Google Play Console's guidelines.


## Class Diagram

To represent the class diagram for the Pet Health Companion, a UML diagram has been included below, depicting the main class components and their interrelationships.

![UML Diagram](https://github.com/NikkaLuna/Pet_Health_Companion_Android_App/blob/master/Pet%20Health%20Companion%20Class%20Diagram.jpeg)

## Development Environment

- **Android Studio**: The main Integrated Development Environment (IDE) used to create Android apps, providing tools for coding and debugging.
- **Android SDK**: The Android Software Development Kit (SDK) is used for creating, testing, and distributing Android applications.
- **Room Persistence Library**: A component of Android Jetpack, Room is a library for managing SQLite databases, acting as an abstraction layer over underlying database infrastructures.
- **Version Control**: Git is used for tracking changes in source code, enabling team collaboration and efficient code management.
- **Dependency Management**: Gradle is used to manage dependencies and construct Android projects, making it easy to add external modules and libraries.

## Authors

- [@NikkaLuna](https://github.com/NikkaLuna)


## 🚀 About Me
I'm a Software Engineer with an emphasis on Java, Python,SQL, and AWS.  


## 🔗 Links
[![linkedin](https://img.shields.io/badge/linkedin-0A66C2?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/andrea-hayes-msml/)
[![twitter](https://img.shields.io/badge/twitter-1DA1F2?style=for-the-badge&logo=twitter&logoColor=white)](https://twitter.com/AHayes_Ninja_)
[![art portfolio](https://img.shields.io/badge/my_art-888?style=for-the-badge&logo=ko-fi&logoColor=white)](https://andreachristinehayes.wixsite.com/andreahayesart/)

