
package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class VBoxController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label zipcodeLabel;
    @FXML
    private Button prevButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button switchHSceneButton;

    private List<Person> personData = new ArrayList<>();
    private int currentIndex = 0;

    @FXML
    public void initialize() {
        personData = fetchPersonData(); // Fetch data from the database
        if (!personData.isEmpty()) {
            displayRecord(currentIndex); // Show the first record
        }
    }

    private List<Person> fetchPersonData() {
        personData.clear();

        try (Connection conn = Database.getConnection();
                Statement stmt = conn.createStatement()) {

            String sql = "SELECT Name, City, Zipcode FROM person";
            ResultSet resultSet = stmt.executeQuery(sql);

            while (resultSet.next()) {
                String name = resultSet.getString("Name");
                String city = resultSet.getString("City");
                String zipcode = resultSet.getString("Zipcode");

                personData.add(new Person(name, city, zipcode));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personData;
    }

    private void displayRecord(int index) {
        if (personData.isEmpty()) {
            nameLabel.setText("No data");
            cityLabel.setText("");
            zipcodeLabel.setText("");
            return;
        }

        Person currentPerson = personData.get(index);
        nameLabel.setText("Name: " + currentPerson.getName());
        cityLabel.setText("City: " + currentPerson.getCity());
        zipcodeLabel.setText("Zipcode: " + currentPerson.getZipcode());
    }

    @FXML
    void nextRecord(ActionEvent event) {
        if (currentIndex < personData.size() - 1) {
            currentIndex++;
            displayRecord(currentIndex);
        }
    }

    @FXML
    void prevRecord(ActionEvent event) {
        if (currentIndex > 0) {
            currentIndex--;
            displayRecord(currentIndex);
        }
    }

    @FXML
    void switchHScene(ActionEvent event) throws IOException {
        App.setRoot("hboxscene"); // This assumes App.java has the setRoot method implemented
    }

    @FXML
    void switchGridScene() throws IOException {
        App.setRoot("gridscene");
    }
    
}
