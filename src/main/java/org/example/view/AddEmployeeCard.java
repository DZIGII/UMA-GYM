package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class AddEmployeeCard extends VBox {

    public AddEmployeeCard() {
        this.setOnMouseEntered(e -> {
            this.setCursor(javafx.scene.Cursor.HAND);
        });
        this.setOnMouseExited(e -> {
            this.setCursor(javafx.scene.Cursor.DEFAULT);
        });
        this.setPadding(new Insets(100, 25, 100, 25));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.setMaxWidth(Region.USE_PREF_SIZE);
        this.setStyle(
                "-fx-background-color: #393030;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #393030;" +
                        "-fx-border-width: 2;"
        );

        ImageView imageView = new ImageView();
        try {
            Image img = new Image(getClass().getResource("/images/plus.png").toExternalForm());
            imageView = new ImageView(img);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            this.getChildren().add(imageView);
        } catch (Exception e) {
            Label imagePlaceholder = new Label("🏋️");
            imagePlaceholder.setStyle("-fx-font-size: 40px;");
            this.getChildren().add(imagePlaceholder);
        }

        Label lbl = new Label("Dodaj Novog Zaposlenog");
        lbl.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-padding: 10 0 10 0;");

        this.getChildren().add(lbl);

        this.setOnMouseClicked(e -> {
            new AddNewEmployeeView().show();
        });
    }
}
