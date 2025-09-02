package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.util.JDBCUtilities;

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
        title.setFont(Font.font("Arial", 40));

        if (JDBCUtilities.currentEmployee.isAdmin()) {
            //grid.add(signInEmployee, 1, 6);
        }

        HBox hb = new HBox();
        hb.setSpacing(30);
        hb.setPadding(new Insets(70, 20, 20, 70));
        hb.getChildren().addAll(new EmployeeCard(JDBCUtilities.currentEmployee), new AddEmployeeCard());

        Button logOutBtn = new Button("Log Out");
        logOutBtn.setStyle(
                "-fx-background-color: #d32f2f;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 16px;" +
                        "-fx-background-radius: 8;" +
                        "-fx-padding: 12 24;" +
                        "-fx-cursor: hand;"
        );

        logOutBtn.setOnMouseEntered(e -> {
            logOutBtn.setStyle(
                    "-fx-background-color: #b71c1c;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 12 24;" +
                            "-fx-cursor: hand;"
            );
        });

        logOutBtn.setOnMouseExited(e -> {
            logOutBtn.setStyle(
                    "-fx-background-color: #d32f2f;" +
                            "-fx-text-fill: white;" +
                            "-fx-font-weight: bold;" +
                            "-fx-font-size: 16px;" +
                            "-fx-background-radius: 8;" +
                            "-fx-padding: 12 24;" +
                            "-fx-cursor: hand;"
            );
        });

        logOutBtn.setOnAction(e -> {
            MainView.getInstance().close();
            LogIn.getInstance().show();
        });

        HBox buttonContainer = new HBox(logOutBtn);
        buttonContainer.setAlignment(Pos.TOP_RIGHT);
        buttonContainer.setPadding(new Insets(30, 50, 0, 0));

        this.getChildren().addAll(title, hb, buttonContainer);
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
