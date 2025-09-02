package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.example.model.Employee;
import org.example.model.Subscription;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



public class SubscriptionCard extends HBox {

    private Subscription subscription;

    private ImageView imageView;
    private Button btn1;
    private Button btn2;

    public SubscriptionCard(Subscription subscription) {
        this.subscription = subscription;
        initElements();
        showElements();
        controller();
    }

    private void initElements() {
        Image img = new Image(getClass().getResource("/images/dumbbell.png").toExternalForm());
        imageView = new ImageView(img);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);

        btn1 = new Button("Dodaj uplatu");
        btn2 = new Button("Detalji o uplatama");

        btn1.setPrefSize(150, 40);
        btn2.setPrefSize(150, 40);
    }

    private void showElements() {
        this.setAlignment(Pos.CENTER_LEFT);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = subscription.getDateStart().format(formatter) + " - " + subscription.getDateEnd().format(formatter);

        GridPane g1 = new GridPane();
        g1.setPadding(new Insets(20, 20, 20, 20));
        g1.setVgap(10);
        g1.setHgap(10);
        g1.setAlignment(Pos.CENTER_LEFT);

        g1.add(makeRightLabel("Ime i prezime:"), 0, 0);
        g1.add(makeLeftLabel(subscription.getUser().getFirstName() + " " + subscription.getUser().getLastName()), 1, 0);

        g1.add(makeRightLabel("Početak/Kraj:"), 0, 1);
        g1.add(makeLeftLabel(date), 1, 1);

        g1.add(makeRightLabel("Traka za trčanje:"), 0, 2);
        g1.add(makeLeftLabel(subscription.isTreadmillIncluded()), 1, 2);

        GridPane g2 = new GridPane();
        g2.setPadding(new Insets(20, 20, 20, 20));
        g2.setVgap(10);
        g2.setHgap(10);
        g2.setAlignment(Pos.CENTER_LEFT);

        g2.add(makeRightLabel("Potpisao:"), 0, 0);
        g2.add(makeLeftLabel(subscription.getEmployeeFullName()), 1, 0);

        g2.add(makeRightLabel("Dug:"), 0, 1);
        g2.add(makeLeftLabel(subscription.getDebt()), 1, 1);

        g2.add(makeRightLabel("Ističe za:"), 0, 2);
        long daysRemaining = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), subscription.getDateEnd());
        g2.add(makeLeftLabel(daysRemaining + " dana"), 1, 2);

        Line line1 = new Line(0, 0, 0, 150);
        line1.setStroke(Color.WHITE);
        line1.setStrokeWidth(1);

        Line line2 = new Line(0, 0, 0, 150);
        line2.setStroke(Color.LIGHTGRAY);
        line2.setStrokeWidth(1);

        VBox buttonContainer = new VBox(10, btn1, btn2);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);
        buttonContainer.setPadding(new Insets(20, 20, 20, 20));

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



        VBox centerContainer = new VBox(g2);
        centerContainer.setAlignment(Pos.CENTER_LEFT);

        VBox rightContainer = new VBox(buttonContainer);
        rightContainer.setAlignment(Pos.CENTER_LEFT);

        this.setSpacing(15);
        this.setPadding(new Insets(15));
        this.setStyle(
                "-fx-background-color: #393E41;" +
                        "-fx-background-radius: 10;" +
                        "-fx-border-color: #2C2F31;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 2;"
        );

        // Omogućava širini da se prilagodi parent kontejneru
        this.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(imageView, Priority.ALWAYS);
        HBox.setHgrow(centerContainer, Priority.ALWAYS);
        HBox.setHgrow(rightContainer, Priority.ALWAYS);

        //imageView.setMaxWidth(Double.MAX_VALUE);
        centerContainer.setMaxWidth(Double.MAX_VALUE);
        rightContainer.setMaxWidth(Double.MAX_VALUE);

        this.getChildren().addAll(imageView, g1, line1, centerContainer, line2, rightContainer);
    }

    public void controller() {
        btn1.setOnAction(e -> new AddNewPaymentView(subscription));
        btn2.setOnAction(e -> new PaymentsOverviewView(subscription));
    }

    private Label makeRightLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #CCCCCC; -fx-font-weight: normal; -fx-font-size: 14px;");
        return label;
    }

    private Label makeLeftLabel(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
        return label;
    }
}
