package org.example.view;

import javafx.geometry.Insets;
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
        logoLbl = new javafx.scene.control.Label("UMA GYM");
        logoLbl.setStyle("-fx-font-size: 31px;");

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
        vb.getChildren().addAll(logoLbl, btn1, btn2, btn3, btn4, btn5, btn6);

        vb.setPadding(new Insets(20, 30, 20, 30));
        vb.setSpacing(30);
        vb.setStyle("-fx-background-color: #f2c511;");

        bp.setLeft(vb);
        bp.setCenter(ProfilView.getInstance());

        btn1.setStyle("-fx-background-color: #1c1f1e; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #ffffff;");

        Scene sc = new Scene(bp, 1050, 600);
        this.setScene(sc);
        this.show();
    }

    void controller() {
        btn1.setOnAction(e -> {
            bp.setCenter(ProfilView.getInstance());
            styleElements();
            btn1.setStyle("-fx-background-color: #1c1f1e; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #ffffff;");
        });
        btn2.setOnAction(e -> {
            bp.setCenter(SubscriptionView.getInstance());
            styleElements();
            btn2.setStyle("-fx-background-color: #1c1f1e; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #ffffff;");
        });
        btn3.setOnAction(e -> {
            bp.setCenter(RegisterNewUser.getInstance());
            styleElements();
            btn3.setStyle("-fx-background-color: #1c1f1e; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #ffffff;");
        });
        btn4.setOnAction(e -> {
            bp.setCenter(UserAllView.getInstance());
            styleElements();
            btn4.setStyle("-fx-background-color: #1c1f1e; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #ffffff;");
        });
        btn5.setOnAction(e -> {
            bp.setCenter(EmployeeView.getInstance());
            styleElements();
            btn5.setStyle("-fx-background-color: #1c1f1e; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #ffffff;");
        });
        btn6.setOnAction(e -> {
            bp.setCenter(Stats.getInstance());
            styleElements();
            btn6.setStyle("-fx-background-color: #1c1f1e; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #ffffff;");
        });
    }

    void styleElements() {
        btn1.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #1c1f1e;");
        btn2.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #1c1f1e;");
        btn3.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #1c1f1e;");
        btn4.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #1c1f1e;");
        btn5.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #1c1f1e;");
        btn6.setStyle("-fx-background-color: #ffffff; -fx-padding: 10px 20px; -fx-font-size: 16px; -fx-text-fill: #1c1f1e;");

        btn1.setPrefWidth(200);
        btn2.setPrefWidth(200);
        btn3.setPrefWidth(200);
        btn4.setPrefWidth(200);
        btn5.setPrefWidth(200);
        btn6.setPrefWidth(200);

    }

}
