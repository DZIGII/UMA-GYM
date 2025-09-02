package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import org.example.model.Employee;

import java.time.format.DateTimeFormatter;

public class EmployeeCard extends VBox {

    private Employee employee;

    public EmployeeCard(Employee employee) {
        this.employee = employee;
        showElements();
    }

    private void showElements() {
        this.setPadding(new Insets(25));
        this.setSpacing(15);
        this.setAlignment(Pos.TOP_CENTER);
        this.setMaxWidth(Region.USE_PREF_SIZE);
        this.setStyle(
                "-fx-background-color: #FFCC00;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-radius: 15;" +
                        "-fx-border-color: #E6B800;" +
                        "-fx-border-width: 2;"
        );

        ImageView imageView = new ImageView();
        try {
            Image img = new Image(getClass().getResource("/images/logo.png").toExternalForm());
            imageView = new ImageView(img);
            imageView.setFitHeight(60);
            imageView.setFitWidth(60);
            this.getChildren().add(imageView);
        } catch (Exception e) {
            Label imagePlaceholder = new Label("🏋️");
            imagePlaceholder.setStyle("-fx-font-size: 40px;");
            this.getChildren().add(imagePlaceholder);
        }

        Label nameLabel = new Label(employee.getFirstName() + " " + employee.getLastNmae());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 20px; -fx-text-fill: #1c1f1e;");
        this.getChildren().add(nameLabel);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER_LEFT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String date = employee.getDateOfBirth().format(formatter);

        grid.add(createGrayLabel("Datum rodjenja:"), 0, 0);
        grid.add(createValueLabel(date), 1, 0);

        grid.add(createGrayLabel("Broj telefona:"), 0, 1);
        grid.add(createValueLabel(employee.getPhoneNumber()), 1, 1);

        grid.add(createGrayLabel("Korisničko ime:"), 0, 2);
        grid.add(createValueLabel(employee.getUserName()), 1, 2);

        grid.add(createGrayLabel("Admin status:"), 0, 3);
        grid.add(createValueLabel(employee.isAdmin() ? "Admin/Active" : "Ne"), 1, 3);

        this.getChildren().add(grid);

    }

    private Label createGrayLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #666666; -fx-padding: 0 0 5 0;");
        return lbl;
    }

    private Label createValueLabel(String text) {
        Label lbl = new Label(text);
        lbl.setStyle("-fx-font-size: 16px; -fx-padding: 0 0 5 0;");

        return lbl;
    }

    private Label makeBoldLbl(String str) {
        Label lbl = new Label(str);
        lbl.setStyle("-fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #333333;");
        return lbl;
    }
}