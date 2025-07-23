package org.example.controller;

import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import org.example.util.JDBCUtilities;
import org.example.view.UserAllView;

import java.time.LocalDate;


public class RegisterUserController implements EventHandler<MouseEvent> {

    private TextField firstNameTf;
    private TextField lastNameTf;
    private DatePicker dateOfBirthDp;
    private javafx.scene.control.TextArea notesTa;

    public RegisterUserController(TextField firstNameTf, TextField lastNameTf, DatePicker dateOfBirthDp, javafx.scene.control.TextArea notesTa) {
        this.firstNameTf = firstNameTf;
        this.lastNameTf = lastNameTf;
        this.dateOfBirthDp = dateOfBirthDp;
        this.notesTa = notesTa;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        String firstName = firstNameTf.getText().trim();
        String lastName = lastNameTf.getText().trim();
        LocalDate dateOfBirth = dateOfBirthDp.getValue();
        String notes = notesTa.getText().trim();

        if (firstName.isEmpty() || lastName.isEmpty() || dateOfBirth == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Upozorenje");
            alert.setHeaderText(null);
            alert.setContentText("Molimo popunite ime, prezime i datum rođenja.");
            alert.showAndWait();
            return;
        }

        boolean isSuccess = JDBCUtilities.addNewUser(firstName, lastName, dateOfBirth, notes);

        if (isSuccess) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uspeh");
            alert.setHeaderText(null);
            alert.setContentText("Uspešno ste dodali novog korisnika.");
            alert.showAndWait();

            firstNameTf.clear();
            lastNameTf.clear();
            dateOfBirthDp.getEditor().clear();
            notesTa.clear();
            UserAllView.getInstance().refreshData();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Greška");
            alert.setHeaderText(null);
            alert.setContentText("Korisnik već postoji u bazi ili je došlo do greške prilikom dodavanja.");
            alert.showAndWait();
        }
    }

}
