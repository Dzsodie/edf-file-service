
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![Java 17](https://img.shields.io/badge/Java-17-007396?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2+-6DB33F?style=for-the-badge&logo=springboot)
![Spring Data JPA](https://img.shields.io/badge/Spring%20Data%20JPA-Repository-6DB33F?style=for-the-badge&logo=spring)
![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Spring Boot Validation](https://img.shields.io/badge/Spring%20Boot-Validation-6DB33F?style=for-the-badge&logo=spring)
![Swagger](https://img.shields.io/badge/Swagger-API%20Docs-green?style=for-the-badge&logo=swagger)
![Mockito](https://img.shields.io/badge/Mockito-Testing-green?style=for-the-badge&logo=java)
![JUnit](https://img.shields.io/badge/JUnit-5-25A162?style=for-the-badge&logo=junit5)
![Logging](https://img.shields.io/badge/Logging-SLF4J%20%2F%20Logback-blue?style=for-the-badge&logo=java)

## EDF File Service
## Introduction
This project consists of an API endpoint that returns an EDF file descriptor for a given file.
## Purpose
This project is a demo for the Zetoinc interview process, it's main purpose is to showcase the ability to develop an API endpoint.
## Technologies Used
- **Java 17** - Programming Language
- **Spring Boot 3.2+** - Framework
- **Spring Data JPA** - Database ORM
- **MySQL 8.0+** - Database
- **Spring Boot Validation** - Data Validation
- **RESTful API** - Communication
- **Mockito** - Testing
- **JUnit 5** - Testing Framework
- **SLF4J** - Logging
- **Swagger** - API Documentation
- **Jacoco** - Test Coverage Reporting
## Features
- passes a `key` parameter to implement string-based authentication for access with a Mock PSK logic.
- allows the user to provide a `URL` pointing to an EDF file.
- returns the file’s descriptor, including key metadata such as the number of channels, duration, number of annotations, title/patient identification, and the start date.
## Project structure
    ```shell
    edf-file-service
    ├── src/main/java/com/zetoinc/edf-file-service
    │   ├── controller/
    │   ├── service/
    │   ├── model/
    │   ├── exception/
    │   ├── security/
    ├── src/test/java/com/zetoinc/edf-file-service
    ```
## Installation
1. Pre-requisites: Download IntelliJ, Postman, Java17, MySQL workbench. 
2. Install and start mysql server.
   - For macOS
   ```shell
       brew install mysql
       brew services start mysql
   ```  
   - For Windows
   Run the downloaded MySQL Installer for Windows from the [MySQL Download Page](https://dev.mysql.com/downloads/installer/)
   In command line use the following command after successful installation and configuration.
   ```shell
     net start mysql
    ```
3. Clone the repository to your local.
    ```shell
    git clone  https://github.com/Dzsodie/edf-file-service.git
    ```
## Starting the application
Start the service with the following command from the root folder of the cloned application.
    ```shell
    mvn spring-boot:run
    ```   
## API Documentation
Swagger documentation is available at: http://localhost:8080/swagger/index.html after the application started successfully.
Use the `Try out!` button, add your `key` and `Url`, then check the API output.
## Authentication steps
1. The `key` is provided by ZetoInc.
2. Add your `key` to system path.
- On macOS
    ```shell
    export SECRET_KEY=mySuperSecretKey
    ```
To make this permanent, add the above lines to `.profile`, `~/.bashrc`, or `~/.zshrc`.
- On Windows (PowerShell)
    ```shell
    $env:SECRET_KEY="mySuperSecretKey"
    ```
## Example Request and Response
### Postman Request
1. Start the application and open Postman.
2. Find the postman collection `EDF file service.postman_collection.json` in the root folder of the application.
3. Import the json file to Postman.
4. Use the `GET` request, in `Params` tab add your `key` and `Url` parameters.
### Curl command
1. Open your terminal or command line window.
2. Use the following command, where `key` and `Url` parameters are already added. Add your own `key`, and your own `Url`.
    ```shell
    curl -X GET "http://localhost:8080/api/edf/descriptor?key=very%20secret&fileUrl=https://example.com/sample.edf" -H "Accept: application/json"
    
    ```
### Example Responses
1. Response should look like the following json, if the request call was successful (200 OK).
    ```shell
    {
      "id": 1,
      "title": "Sample EDF",
      "patientId": "Patient-123",
      "numberOfChannels": 31,
      "duration": 300.0,
      "numberOfAnnotations": 5,
      "startDate": "2025-02-12T14:00:00Z"
    }
    ```
2. In case the key provided in the request call is invalid (403 Forbidden), the following error message will be returned.
    ```shell
    {
      "message": "Invalid authentication key."
    }
    ```
3. In case the File URL is missing (400 Bad Request), the following error message is returned.
    ```shell
    {
      "message": "File URL is required."
    }
    ```
4. In case the File URL is not valid (400 Bad Request), the following error message is returned.
    ```shell
    {
      "message": "Invalid EDF file URL. Must be a valid HTTP/HTTPS URL."
    }
    ```
5. In case there was any kind of error while file processing (500 Internal Server Error), the following error message is returned.
    ```shell
    {
      "message": "Error processing EDF file."
    }
    ```
## Logging and monitoring
1. SLF4J is used for logging.
2. Application logs for debugging can be found at `logs/edf-file-service.log`.
3. Log aggregation is not yet implemented, but Datadog offers an easy-to-implement solution with user-friendly monitoring interface.
## Testing
1. Mockito and JUnit5 is used for the unit testing.
2. Test coverage needs to be improved. Coverage report can be reached with this command.
    ```shell
    mvn jacoco:report
    ```
3. Then use this command to open the report.
   ```shell
   open target/site/jacoco/index.html 
   ```
4. Tests can be run with the following command.
    ```shell
    mvn test
    ```
## License
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.