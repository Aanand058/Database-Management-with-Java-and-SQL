// package com.example;

// import java.io.IOException;
// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.util.ArrayList;
// import java.util.List;

// import javafx.event.ActionEvent;
// import javafx.fxml.FXML;
// import javafx.scene.control.Button;
// import javafx.scene.control.Label;

// public class GridController {
// /*TODO 29: Paste the @FXML annotations from gridscene.fxml */

//     private List<String[]> personData;  // List to store fetched records
//     private int currentIndex = 0;

//   /** TODO 30: Copy the code of fetchPersonData(), displayRecord(), initialize() and 
//    nextRecord() methods from VBoxController.java**/

//     @FXML
//     void prevRecord(ActionEvent event) {

//     }    

//          @FXML
//     void switchVScene(ActionEvent event) throws IOException{
// /*TODO 31: set the vboxscene.fxml as the root of application window*/

//     }

// }

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

public class GridController {

    // TODO 29: Paste the @FXML annotations from gridscene.fxml
    @FXML
    private Label nameLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label zipcodeLabel;
    @FXML
    private Button nextButton;
    @FXML
    private Button switchVBoxSceneButton;

    private List<Person> personData = new ArrayList<>();
    private int currentIndex = 0;

    // TODO 30: initialize() method
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

    // TODO 31: Add navigation to switch back to VBox scene
    @FXML
    void switchVBoxScene(ActionEvent event) throws IOException {
        App.setRoot("vboxscene");
    }
}
