package org.example.controller;

import javafx.event.EventHandler;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.example.model.Employee;
import org.example.util.JDBCUtilities;
import org.example.view.MainView;
import org.example.view.ProfilView;

import java.awt.*;
import java.time.LocalDate;

public class LogInController implements EventHandler<MouseEvent> {

    private Stage logInStage;
    private TextField userNameTf;
    private PasswordField passwordF;

    public LogInController(Stage logInStage, TextField userNameTf, PasswordField passwordF) {
        this.logInStage = logInStage;
        this.userNameTf = userNameTf;
        this.passwordF = passwordF;
    }


    @Override
    public void handle(MouseEvent mouseEvent) {
        Employee employee = JDBCUtilities.selectEmployee(userNameTf.getText(), passwordF.getText());
        if (employee != null) {
            JDBCUtilities.currentEmployee = employee;

            String admin;
            if (employee.isAdmin()) admin = "Admin";
            else admin = "Nije admin";

            ProfilView.getInstance().setProfileData(employee.getFirstName(), employee.getLastNmae(),
                    employee.getDateOfBirth().toString(), employee.getPhoneNumber(), employee.getUserName(), admin);
            logInStage.close();
            MainView.getInstance().show();
        }
        else {
            // Pogresna sifra, userName
        }
    }
}
