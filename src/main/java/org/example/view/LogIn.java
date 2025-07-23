package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.controller.LogInController;

public class LogIn extends Stage {

    public static LogIn instance;

    private Label emailLbl;
    private Label passwordLbl;

    private TextField emailTf;
    private PasswordField passwordPf;

    private Button loginBtn;

    private LogIn() {
        intitElements();
        showEl();
        controller();
    }

    public static LogIn getInstance() {
        if (instance == null)
            instance = new LogIn();
        return instance;
    }

    void intitElements() {
        emailLbl = new Label("Korisničko ime:");
        passwordLbl = new Label("Lozinka:");

        emailTf = new TextField();
        passwordPf = new PasswordField();

        loginBtn = new Button("Prijavi se");
    }

    void showEl() {
        Text title = new Text("Log In");
        title.setFont(Font.font("Arial", 24));

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(emailLbl, 0, 0);
        grid.add(emailTf, 1, 0);

        grid.add(passwordLbl, 0, 1);
        grid.add(passwordPf, 1, 1);

        HBox buttons = new HBox(10, loginBtn);
        buttons.setAlignment(Pos.CENTER);

        VBox mainLayout = new VBox(20, title, grid, buttons);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(mainLayout, 400, 300);
        this.setTitle("Prijava Trenera");
        this.setScene(scene);
        this.show();
    }

    public void controller() {
        loginBtn.setOnMouseClicked(new LogInController(this, emailTf, passwordPf));

    }
}
