package com.example;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class HBoxController {
    /* TODO 26: Paste the @FXML annotations from hboxscene.fxml */

    @FXML
    private Label nameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label zipcodeLabel;
    @FXML
    private Button nextButton;
    @FXML
    private Button switchGridSceneButton;

    private List<Person> personData; // List to store fetched records
    private int currentIndex = 0;

    /*
     * TODO 27: Copy the code of fetchPersonData(), displayRecord(), initialize()
     * and nextRecord() methods
     * from VBoxController.java code to HBoxController.java file
     */

    @FXML
    public void initialize() {
        personData = fetchPersonData();
        if (!personData.isEmpty()) {
            displayRecord(currentIndex);
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

    // TODO 28: Add navigation to gridscene.fxml
    @FXML
    void switchGridScene(ActionEvent event) throws IOException {
        App.setRoot("gridscene");
    }
}
