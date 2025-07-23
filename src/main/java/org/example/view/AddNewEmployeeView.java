package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.Employee;
import org.example.util.JDBCUtilities;

import java.time.LocalDate;

public class AddNewEmployeeView extends Stage {

    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dobPicker;
    private TextField usernameField;
    private CheckBox adminCheckBox;
    private TextField phoneField;
    private Button saveButton;
    private Button cancelButton;
    private PasswordField passwordField;

    public AddNewEmployeeView() {
        setTitle("Dodaj novog zaposlenog");
        initModality(Modality.APPLICATION_MODAL);
        initElements();
        showElements();
        controller();
    }

    private void initElements() {
        firstNameField = new TextField();
        firstNameField.setPromptText("Ime");

        lastNameField = new TextField();
        lastNameField.setPromptText("Prezime");

        dobPicker = new DatePicker();
        dobPicker.setPromptText("Datum rođenja");

        usernameField = new TextField();
        usernameField.setPromptText("Korisničko ime");

        passwordField = new PasswordField();
        passwordField.setPromptText("Lozinka");

        adminCheckBox = new CheckBox("Administrator");

        phoneField = new TextField();
        phoneField.setPromptText("Broj telefona");

        saveButton = new Button("Sačuvaj");
        cancelButton = new Button("Otkaži");
    }

    private void showElements() {
        GridPane formGrid = new GridPane();
        formGrid.setVgap(10);
        formGrid.setHgap(15);
        formGrid.setPadding(new Insets(20));

        formGrid.add(new Label("Ime:"), 0, 0);
        formGrid.add(firstNameField, 1, 0);

        formGrid.add(new Label("Prezime:"), 0, 1);
        formGrid.add(lastNameField, 1, 1);

        formGrid.add(new Label("Datum rođenja:"), 0, 2);
        formGrid.add(dobPicker, 1, 2);

        formGrid.add(new Label("Korisničko ime:"), 0, 3);
        formGrid.add(usernameField, 1, 3);

        formGrid.add(new Label("Šifra:"), 0, 4);
        formGrid.add(passwordField, 1, 4);

        formGrid.add(new Label("Broj telefona"), 0, 5);
        formGrid.add(phoneField, 1, 5);

        formGrid.add(adminCheckBox, 1, 6);

        HBox buttonBox = new HBox(15, saveButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setStyle("-fx-border-color: #888; -fx-border-width: 1; -fx-border-radius: 5px;");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        cancelButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

        VBox mainLayout = new VBox(20, formGrid, buttonBox);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: #f0f8ff;");
        mainLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(mainLayout, 400, 450);
        scene.getRoot().setStyle("-fx-font-size: 14px; -fx-font-family: 'Segoe UI';");
        setScene(scene);
    }

    private void controller() {
        saveButton.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            LocalDate dob = dobPicker.getValue();
            String username = usernameField.getText().trim();
            boolean isAdmin = adminCheckBox.isSelected();
            String phone = phoneField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty() || dob == null || username.isEmpty() || phone.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Sva polja moraju biti popunjena!");
                return;
            }

            Employee emp = new Employee(1, firstName, lastName, dob, username, isAdmin, phone);

            boolean success = JDBCUtilities.insertEmployee(emp, passwordField.getText());
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Zaposleni je uspešno dodat.");
                EmployeeView.getInstance().refreshData();
                close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Greška prilikom dodavanja zaposlenog.");
            }
        });

        cancelButton.setOnAction(e -> close());
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(this);
        alert.showAndWait();
    }
}
