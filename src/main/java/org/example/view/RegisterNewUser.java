package org.example.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.controller.RegisterUserController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RegisterNewUser extends VBox {

    private static RegisterNewUser instance;

    private Text title;
    private TextField firstNameField;
    private TextField lastNameField;
    private DatePicker dateOfBirthPicker;
    private TextField phoneField;
    private TextArea noteArea;
    private Button registerBtn;

    private RegisterNewUser() {
        initElements();
        showElements();
        contoller();
    }

    public static RegisterNewUser getInstance() {
        if (instance == null)
            instance = new RegisterNewUser();
        return instance;
    }

    private void initElements() {
        title = new Text("Registruj Novog Korisnika");
        firstNameField = new TextField();
        lastNameField = new TextField();
        dateOfBirthPicker = new DatePicker();
        //phoneField = new TextField();
        noteArea = new TextArea();
        noteArea.setPrefRowCount(4);

        registerBtn = new Button("Registruj korisnika");

        firstNameField.setPromptText("Unesite ime");
        lastNameField.setPromptText("Unesite prezime");
        //phoneField.setPromptText("Unesite broj telefona");
        noteArea.setPromptText("Napomena (opciono)");
    }

    private void showElements() {
        registerBtn.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 16 8 16;"
        );

        this.setSpacing(15);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.TOP_CENTER);

        title.setFont(Font.font("Arial", 24));
        HBox hb = new HBox();
        hb.getChildren().add(title);
        hb.setAlignment(Pos.CENTER);

        this.getChildren().addAll(
                hb,
                createFormRow("Ime:", firstNameField),
                createFormRow("Prezime:", lastNameField),
                createFormRow("Datum rođenja:", dateOfBirthPicker),
                //createFormRow("Telefon:", phoneField),
                createFormRow("Napomena:", noteArea),
                registerBtn
        );
    }

    private void contoller() {
        registerBtn.setOnMouseClicked(new RegisterUserController(firstNameField, lastNameField, dateOfBirthPicker, noteArea));
    }

    private HBox createFormRow(String labelText, Control inputControl) {
        Label label = new Label(labelText);
        label.setPrefWidth(120);
        HBox hBox = new HBox(10);
        hBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(inputControl, Priority.ALWAYS);
        inputControl.setMaxWidth(Double.MAX_VALUE);
        hBox.getChildren().addAll(label, inputControl);
        return hBox;
    }
}
