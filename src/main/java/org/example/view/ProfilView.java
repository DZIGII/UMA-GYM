package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.util.JDBCUtilities;

import java.time.LocalDate;

public class ProfilView extends VBox {

    public static ProfilView instance;

    private Label firstName;
    private Label lastName;
    private Label dateOfBirth;
    private Label phoneNumber;
    private Label userName;
    private Label isAdmin;
    private Button logOutBtn;
    private Button signInEmployee;

    private ProfilView() {
        initElements();
        showEl();
        controller();
    }

    public static ProfilView getInstance() {
        if (instance == null)
            instance = new ProfilView();
        return instance;
    }

    private void initElements() {
        firstName = new Label();
        lastName = new Label();
        dateOfBirth = new Label();
        phoneNumber = new Label();
        userName = new Label();
        isAdmin = new Label();
        logOutBtn = new Button("Log Out");
        signInEmployee = new Button("Dodaj Zaposlenog");
    }

    private void showEl() {
        Text title = new Text("Profil Trenera");
        title.setFont(Font.font("Arial", 24));

        logOutBtn.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 16 8 16;"
        );
        signInEmployee.setStyle(
                "-fx-background-color: black;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 8 16 8 16;"
        );

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER_LEFT);

        grid.add(new Label("Ime:"), 0, 0);
        grid.add(firstName, 1, 0);

        grid.add(new Label("Prezime:"), 0, 1);
        grid.add(lastName, 1, 1);

        grid.add(new Label("Datum rođenja:"), 0, 2);
        grid.add(dateOfBirth, 1, 2);

        grid.add(new Label("Telefon:"), 0, 3);
        grid.add(phoneNumber, 1, 3);

        grid.add(new Label("Korisničko ime:"), 0, 4);
        grid.add(userName, 1, 4);

        grid.add(new Label("Admin status:"), 0, 5);
        grid.add(isAdmin, 1, 5);

        grid.add(logOutBtn, 0, 6);
        if (JDBCUtilities.currentEmployee.isAdmin()) {
            grid.add(signInEmployee, 1, 6);
        }

        this.getChildren().addAll(title, grid);
        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setAlignment(Pos.TOP_CENTER);

    }

    public void setProfileData(String firstNamee, String lastNamee, String dateOfBirthh,
                               String phoneNumberr, String userNamee, String admin) {
        firstName.setText(firstNamee);
        lastName.setText(lastNamee);
        dateOfBirth.setText(dateOfBirthh.toString());
        phoneNumber.setText(phoneNumberr);
        userName.setText(userNamee);
        isAdmin.setText(admin);
    }

    public void controller() {
        logOutBtn.setOnAction(e -> {
            MainView.getInstance().close();
            LogIn.getInstance().show();
        });

        signInEmployee.setOnMouseClicked(e ->{
            new AddNewEmployeeView().show();
        });
    }

}
