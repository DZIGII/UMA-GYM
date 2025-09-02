package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.controller.RegisterUserController;

public class RegisterNewUser extends BorderPane {

    private static RegisterNewUser instance;
    private TextField firstNameTf;
    private TextField lastNameTf;
    private DatePicker dateOfBirthDp;
    private TextArea notesTa;
    private Button registerBtn;

    private RegisterNewUser() {
        initElements();
        styleElements();
        showElements();
        controller();
    }

    public static RegisterNewUser getInstance() {
        if (instance == null) {
            instance = new RegisterNewUser();
        }
        return instance;
    }

    private void initElements() {
        firstNameTf = new TextField();
        lastNameTf = new TextField();
        dateOfBirthDp = new DatePicker();
        notesTa = new TextArea();
        registerBtn = new Button("Registruj Korisnika");
    }

    private void styleElements() {
        // Stil za pozadinu
        this.setStyle("-fx-background-color: #f8f9fa;");

        // Stil za text field-ove - bez shadowa i focus linija
        String textFieldStyle = "-fx-background-color: #ffffff; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 2; " +
                "-fx-padding: 15; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: normal; " +
                "-fx-text-fill: #000000; " +
                "-fx-focus-color: transparent; " +
                "-fx-faint-focus-color: transparent;";

        firstNameTf.setStyle(textFieldStyle);
        lastNameTf.setStyle(textFieldStyle);

        // Poseban stil za DatePicker da ukloni focus linije
        dateOfBirthDp.setStyle("-fx-background-color: #ffffff; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 2; " +
                "-fx-padding: 8; " + // Manji padding za DatePicker
                "-fx-font-size: 16px; " +
                "-fx-font-weight: normal; " +
                "-fx-text-fill: #000000; " +
                "-fx-focus-color: transparent; " +
                "-fx-faint-focus-color: transparent;");

        // Stil za TextArea - bez shadowa i focus linija
        notesTa.setStyle("-fx-background-color: #ffffff; " +
                "-fx-background-radius: 10; " +
                "-fx-border-radius: 10; " +
                "-fx-border-color: #e0e0e0; " +
                "-fx-border-width: 2; " +
                "-fx-padding: 15; " +
                "-fx-font-size: 16px; " +
                "-fx-font-weight: normal; " +
                "-fx-text-fill: #000000; " +
                "-fx-focus-color: transparent; " +
                "-fx-faint-focus-color: transparent;");

        // Uklanjanje focus linija iz CSS klase za TextArea
        notesTa.setFocusTraversable(false);

        // Stil za dugme
        registerBtn.setStyle("-fx-background-color: #f2c511; " +
                "-fx-background-radius: 15; " +
                "-fx-border-radius: 15; " +
                "-fx-padding: 20 40; " +
                "-fx-font-size: 18px; " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #1c1f1e; " +
                "-fx-cursor: hand;");

        // Hover efekat za dugme
        registerBtn.setOnMouseEntered(e -> registerBtn.setStyle(
                "-fx-background-color: #e6b800; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 20 40; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #1c1f1e; " +
                        "-fx-cursor: hand;"));

        registerBtn.setOnMouseExited(e -> registerBtn.setStyle(
                "-fx-background-color: #f2c511; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 20 40; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #1c1f1e; " +
                        "-fx-cursor: hand;"));

        // Podešavanje veličina
        firstNameTf.setPrefHeight(50);
        lastNameTf.setPrefHeight(50);
        dateOfBirthDp.setPrefHeight(50);
        notesTa.setPrefHeight(120);
        registerBtn.setPrefHeight(60);

        // Dodatno: Uklanjanje focus linija za sva polja
        removeFocusLines(firstNameTf);
        removeFocusLines(lastNameTf);
        removeFocusLines(dateOfBirthDp);
    }

    // Pomocna metoda za uklanjanje focus linija
    private void removeFocusLines(Control control) {
        control.setFocusTraversable(false);
        control.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                control.setStyle(control.getStyle() + "-fx-background-color: #ffffff; -fx-border-color: #e0e0e0;");
            }
        });
    }

    private void showElements() {
        // Naslov - bez shadowa
        Label titleLabel = new Label("REGISTRACIJA NOVOG KORISNIKA");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #1c1f1e;");
        titleLabel.setPadding(new Insets(0, 0, 30, 0));

        // Forma
        GridPane formGrid = new GridPane();
        formGrid.setHgap(20);
        formGrid.setVgap(25);
        formGrid.setPadding(new Insets(30));
        formGrid.setAlignment(Pos.CENTER);

        // Label style - jasan tekst bez mutnoće
        String labelStyle = "-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1c1f1e;";

        Label firstNameLabel = new Label("Ime:");
        firstNameLabel.setStyle(labelStyle);
        formGrid.add(firstNameLabel, 0, 0);
        formGrid.add(firstNameTf, 1, 0);

        Label lastNameLabel = new Label("Prezime:");
        lastNameLabel.setStyle(labelStyle);
        formGrid.add(lastNameLabel, 0, 1);
        formGrid.add(lastNameTf, 1, 1);

        Label dobLabel = new Label("Datum rođenja:");
        dobLabel.setStyle(labelStyle);
        formGrid.add(dobLabel, 0, 2);
        formGrid.add(dateOfBirthDp, 1, 2);

        Label notesLabel = new Label("Napomene:");
        notesLabel.setStyle(labelStyle);
        formGrid.add(notesLabel, 0, 3);
        formGrid.add(notesTa, 1, 3);

        // Postavljanje constraint-ova za širinu
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPrefWidth(150);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPrefWidth(400);
        formGrid.getColumnConstraints().addAll(col1, col2);

        // Centriranje dugmeta
        HBox buttonContainer = new HBox(registerBtn);
        buttonContainer.setAlignment(Pos.CENTER);
        buttonContainer.setPadding(new Insets(30, 0, 0, 0));

        // Glavni container - BEZ SHADOWA
        VBox mainContainer = new VBox(20, titleLabel, formGrid, buttonContainer);
        mainContainer.setAlignment(Pos.TOP_CENTER);
        mainContainer.setPadding(new Insets(40));
        mainContainer.setMaxWidth(800);
        mainContainer.setStyle("-fx-background-color: #ffffff; " +
                "-fx-background-radius: 20;");

        // Centriranje celog sadržaja
        StackPane centerContainer = new StackPane(mainContainer);
        centerContainer.setPadding(new Insets(40));

        this.setCenter(centerContainer);
    }

    private void controller() {
        registerBtn.setOnMouseClicked(new RegisterUserController(firstNameTf, lastNameTf, dateOfBirthDp, notesTa));
    }

    public void clearForm() {
        firstNameTf.clear();
        lastNameTf.clear();
        dateOfBirthDp.setValue(null);
        notesTa.clear();
    }
}