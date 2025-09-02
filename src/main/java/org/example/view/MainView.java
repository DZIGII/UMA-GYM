package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.model.MonthStats;

public class MainView extends Stage {

    public static MainView instance;

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;

    private Label logoLbl;
    private BorderPane bp;

    private MainView() {
        initElements();
        styleElements();
        showElements();
        controller();
    }

    public static MainView getInstance() {
        if (instance == null)
            instance = new MainView();
        return instance;
    }

    void initElements() {
        logoLbl = new Label("UMA GYM");
        logoLbl.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: #1c1f1e;");

        btn1 = new Button("Profil");
        btn2 = new Button("Aktivni Klijenti");
        btn3 = new Button("Registracija Klijenata");
        btn4 = new Button("Svi Klijenti");
        btn5 = new Button("Zaposleni");
        btn6 = new Button("Statistika");

        bp = new BorderPane();
    }

    void showElements() {
        VBox vb = new VBox();

        // Centriranje logoa
        VBox logoContainer = new VBox(logoLbl);
        logoContainer.setAlignment(Pos.CENTER);
        logoContainer.setPadding(new Insets(0, 0, 20, 0));

        // Prvo dodaj logoContainer, pa onda dugmiće
        vb.getChildren().addAll(logoContainer, btn1, btn2, btn3, btn4, btn5, btn6);

        vb.setPadding(new Insets(30, 40, 30, 40));
        vb.setSpacing(35);
        vb.setStyle("-fx-background-color: #f2c511;");
        vb.setAlignment(Pos.TOP_CENTER);

        bp.setLeft(vb);
        bp.setCenter(ProfilView.getInstance());

        // Inicijalni stil za aktivni dugme
        setActiveButtonStyle(btn1);

        Scene sc = new Scene(bp, 1300, 700);
        this.setScene(sc);
        this.show();
    }

    void controller() {
        btn1.setOnAction(e -> {
            bp.setCenter(ProfilView.getInstance());
            resetButtonStyles();
            setActiveButtonStyle(btn1);
        });
        btn2.setOnAction(e -> {
            bp.setCenter(SubscriptionView.getInstance());
            resetButtonStyles();
            setActiveButtonStyle(btn2);
        });
        btn3.setOnAction(e -> {
            bp.setCenter(RegisterNewUser.getInstance());
            resetButtonStyles();
            setActiveButtonStyle(btn3);
        });
        btn4.setOnAction(e -> {
            bp.setCenter(UserAllView.getInstance());
            resetButtonStyles();
            setActiveButtonStyle(btn4);
        });
        btn5.setOnAction(e -> {
            bp.setCenter(EmployeeView.getInstance());
            resetButtonStyles();
            setActiveButtonStyle(btn5);
        });
        btn6.setOnAction(e -> {
            bp.setCenter(Stats.getInstance());
            resetButtonStyles();
            setActiveButtonStyle(btn6);
        });
    }

    void styleElements() {
        setNormalButtonStyle(btn1);
        setNormalButtonStyle(btn2);
        setNormalButtonStyle(btn3);
        setNormalButtonStyle(btn4);
        setNormalButtonStyle(btn5);
        setNormalButtonStyle(btn6);

        btn1.setPrefSize(220, 50);
        btn2.setPrefSize(220, 50);
        btn3.setPrefSize(220, 50);
        btn4.setPrefSize(220, 50);
        btn5.setPrefSize(220, 50);
        btn6.setPrefSize(220, 50);

        setupButtonHoverEffects(btn1);
        setupButtonHoverEffects(btn2);
        setupButtonHoverEffects(btn3);
        setupButtonHoverEffects(btn4);
        setupButtonHoverEffects(btn5);
        setupButtonHoverEffects(btn6);
    }

    private void setNormalButtonStyle(Button button) {
        button.setStyle(
                "-fx-background-color: #ffffff; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 15px 25px; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #1c1f1e; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 5, 0, 0, 2);"
        );
    }

    private void setActiveButtonStyle(Button button) {
        button.setStyle(
                "-fx-background-color: #1c1f1e; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 15px 25px; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #ffffff; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        );
    }

    private void setHoverButtonStyle(Button button) {
        button.setStyle(
                "-fx-background-color: #e6e6e6; " +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15; " +
                        "-fx-padding: 15px 25px; " +
                        "-fx-font-size: 18px; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: #1c1f1e; " +
                        "-fx-cursor: hand; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 8, 0, 0, 3);"
        );
    }

    private void setupButtonHoverEffects(Button button) {
        // Koristimo custom properties za praćenje stanja
        button.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_ENTERED, e -> {
            if (!isActiveButton(button)) {
                setHoverButtonStyle(button);
            }
        });

        button.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_EXITED, e -> {
            if (!isActiveButton(button)) {
                setNormalButtonStyle(button);
            }
        });
    }

    private boolean isActiveButton(Button button) {
        // Proveravamo da li dugme ima crnu pozadinu (aktivni stil)
        return button.getStyle().contains("background-color: #1c1f1e") ||
                button.getStyle().contains("-fx-background-color: #1c1f1e");
    }

    private void resetButtonStyles() {
        setNormalButtonStyle(btn1);
        setNormalButtonStyle(btn2);
        setNormalButtonStyle(btn3);
        setNormalButtonStyle(btn4);
        setNormalButtonStyle(btn5);
        setNormalButtonStyle(btn6);
    }
}
