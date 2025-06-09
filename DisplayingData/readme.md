# JavaFX Multi-Scene Layout Application

## Project Overview

This project is a JavaFX desktop application demonstrating navigation between different UI layouts (VBox, HBox, GridPane) to display and browse records of people with attributes such as Name, City, and Zipcode. It connects to a MySQL database to fetch data and updates UI labels dynamically.

The application features:
- Database connectivity using JDBC and MySQL Connector.
- Multiple FXML views (VBox, HBox, GridPane) with corresponding controllers.
- Navigation buttons to browse through person records.
- Scene switching between different layouts.
- Unit tests validating functionality and UI elements.

---

## Tools and Technologies Used

- **Java 11**
- **JavaFX 22** (FXML with Scene Builder)
- **Maven** (build and dependency management)
- **MySQL** (database)
- **JDBC** (Java Database Connectivity)
- **JUnit and TestFX** (testing framework and UI testing)

---

## Getting Started

### Prerequisites

- JDK 11 or higher installed
- Maven installed
- MySQL Server running with a database and table setup for person records
- JavaFX SDK setup or dependency via Maven (already configured)

### Database Setup

Create a MySQL database and a table named `persons` with columns:
- `name` (VARCHAR)
- `city` (VARCHAR)
- `zipcode` (VARCHAR)

Populate the table with sample data for testing.

### Running the Application

1. Clone the repository.
2. Configure database connection parameters in the `Database` class.
3. Run the application using Maven:
   ```
   mvn clean javafx:run
   ```
4. The application window will open showing the default layout (VBox). Use navigation buttons to browse records and switch layouts using buttons.

---

## Project Structure

```
src/
├── main/
│   ├── java/com/example/
│   │   ├── App.java                 # Main JavaFX Application class
│   │   ├── Database.java            # Database connection utility
│   │   ├── VBoxController.java      # Controller for VBox layout
│   │   ├── HBoxController.java      # Controller for HBox layout
│   │   ├── GridController.java      # Controller for GridPane layout
│   ├── resources/
│       ├── primary.fxml              # VBox layout FXML
│       ├── hbox.fxml                 # HBox layout FXML
│       ├── grid.fxml                 # GridPane layout FXML
├── test/
│   ├── java/com/example/
│       ├── AppTest.java              # Unit and UI tests
```

---

## Features

- **Scene Switching:** Switch between VBox, HBox, and GridPane layouts.
- **Record Navigation:** "Next" and "Previous" buttons to browse person records.
- **Dynamic Data Binding:** UI labels update with current record data.
- **Database Connectivity:** Uses JDBC to fetch person data from MySQL.

---

## Testing

The project includes unit and UI tests using JUnit and TestFX. To run tests:

```
mvn test
```

---

## Contribution

Feel free to fork the repository and submit pull requests. For bugs or feature requests, please open an issue.

---


