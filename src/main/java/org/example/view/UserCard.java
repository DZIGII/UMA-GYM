package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import org.example.model.User;
import org.example.util.JDBCUtilities;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class UserCard extends HBox {

    private User user;
    private ImageView imageView;
    private Button btn1;
    private Button btn2;
    private Button btn3;

    public UserCard(User user) {
        this.user = user;
        initElements();
        showElements();
        controller();
    }

    private void initElements() {
        Image img = new Image(getClass().getResource("/images/dumbbell.png").toExternalForm());
        imageView = new ImageView(img);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);

        btn1 = new Button("Dodaj Pretplatu");
        btn2 = new Button("Aktivnost Korisnika");
        btn3 = new Button("Obriši korisnika");

        btn1.setPrefSize(150, 40);
        btn2.setPrefSize(150, 40);
        btn3.setPrefSize(150, 40);
    }

    private void showElements() {
        this.setAlignment(Pos.CENTER_LEFT);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = user.getDateOfBirth().format(formatter);

        GridPane userInfoGrid = new GridPane();
        userInfoGrid.setPadding(new Insets(20, 20, 20, 20));
        userInfoGrid.setVgap(10);
        userInfoGrid.setHgap(10);
        userInfoGrid.setAlignment(Pos.CENTER_LEFT);

        userInfoGrid.add(makeRightLabel("Ime i prezime:"), 0, 0);
        userInfoGrid.add(makeLeftLabel(user.getFirstName() + " " + user.getLastName()), 1, 0);

        userInfoGrid.add(makeRightLabel("Datum Rodjenja:"), 0, 1);
        userInfoGrid.add(makeLeftLabel(date), 1, 1);

        userInfoGrid.add(makeRightLabel("Napomena:"), 0, 2);
        userInfoGrid.add(makeLeftLabel(user.getNote() == null || user.getNote().isEmpty() ? "Nema" : user.getNote()), 1, 2);

        Line verticalLine = new Line(0, 0, 0, 150);
        verticalLine.setStroke(javafx.scene.paint.Color.WHITE);
        verticalLine.setStrokeWidth(1);

        styleButtons();

        VBox buttonContainer = new VBox(10, btn1, btn2, btn3);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.setPadding(new Insets(20));

        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setStyle(
                "-fx-background-color: #393E41;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #2C2F31;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 2;"
        );

        this.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(userInfoGrid, Priority.ALWAYS);

        this.getChildren().addAll(imageView, userInfoGrid, verticalLine, buttonContainer);
    }

    private void styleButtons() {
        btn1.setStyle(
                "-fx-background-color: #FFCC00;" +
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        );

        btn2.setStyle(
                "-fx-background-color: #4B4C4D;" +
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        );

        btn3.setStyle(
                "-fx-background-color: #d32f2f;" +
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        );

        btn1.setOnMouseEntered(e -> btn1.setStyle(
                "-fx-background-color: #E6B800;" +
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        ));
        btn1.setOnMouseExited(e -> btn1.setStyle(
                "-fx-background-color: #FFCC00;" +
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        ));

        btn2.setOnMouseEntered(e -> btn2.setStyle(
                "-fx-background-color: #3A3B3C;" +
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        ));
        btn2.setOnMouseExited(e -> btn2.setStyle(
                "-fx-background-color: #4B4C4D;" +
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        ));

        btn3.setOnMouseEntered(e -> btn3.setStyle(
                "-fx-background-color: #b71c1c;" + // Tamnija crvena na hover
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        ));
        btn3.setOnMouseExited(e -> btn3.setStyle(
                "-fx-background-color: #d32f2f;" + // Povratak na originalnu crvenu
                        "-fx-background-radius: 5;" +
                        "-fx-text-fill: white;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 14px;"
        ));
    }

    public void controller() {
        btn1.setOnAction(e -> AddSubscriptionView.getInstance(user).show());
        btn2.setOnAction(e -> new MembershipHistoryView(user));
        btn3.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Potvrda brisanja");
            alert.setHeaderText("Da li ste sigurni da želite da obrišete ovog korisnika?");
            alert.setContentText("Ova akcija će obrisati korisnika i sve njegove podatke.");

            ButtonType yesButton = new ButtonType("Da", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Ne", ButtonBar.ButtonData.NO);

            alert.getButtonTypes().setAll(yesButton, noButton);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                boolean success = JDBCUtilities.deleteUserAndDependencies(user);
                if (success) {
                    UserAllView.getInstance().refreshData();
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Brisanje uspešno");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Korisnik i svi njegovi podaci su uspešno obrisani.");
                    successAlert.showAndWait();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Greška");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Došlo je do greške pri brisanju korisnika.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    private javafx.scene.control.Label makeRightLabel(String text) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.setStyle("-fx-text-fill: #CCCCCC; -fx-font-weight: normal; -fx-font-size: 14px;");
        return label;
    }

    private javafx.scene.control.Label makeLeftLabel(String text) {
        javafx.scene.control.Label label = new javafx.scene.control.Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        return label;
    }
}