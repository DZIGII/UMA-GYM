package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.model.Payment;
import org.example.model.Subscription;
import org.example.util.JDBCUtilities;

import java.util.List;

public class PaymentsOverviewView extends Stage {

    private Subscription subscription;

    public PaymentsOverviewView(Subscription subscription) {
        this.subscription = subscription;
        setTitle("Pregled uplata za: " + subscription.getName());
        initUI();
        show();
    }

    private void initUI() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f2f2f2;");

        Label title = new Label("Uplate za subskripciju: " + subscription.getName() + "(" + subscription.getPrice() + " din)");
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.DARKBLUE);

        root.getChildren().add(title);

        List<Payment> payments = JDBCUtilities.getPaymentsForSubscription(subscription.getId());

        if (payments.isEmpty()) {
            Label noData = new Label("Nema evidentiranih uplata.");
            noData.setFont(Font.font("Arial", 16));
            root.getChildren().add(noData);
        } else {
            for (Payment p : payments) {
                VBox paymentBox = new VBox(5);
                paymentBox.setPadding(new Insets(10));
                paymentBox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

                Label userLabel = new Label("Korisnik: " + p.getUser().getFirstName() + " " + p.getUser().getLastName());
                userLabel.setFont(Font.font("Arial", 16));

                Label empLabel = new Label("Zaposleni: " + p.getEmployee().getFirstName() + " " + p.getEmployee().getLastNmae());
                empLabel.setFont(Font.font("Arial", 14));

                Label dateLabel = new Label("Datum uplate: " + p.getPaymentDate().toString());
                dateLabel.setFont(Font.font("Arial", 14));

                Label amountLabel = new Label("Iznos: " + p.getAmountPaid() + " din");
                amountLabel.setFont(Font.font("Arial", 16));
                amountLabel.setStyle("-fx-font-weight: bold;");

                paymentBox.getChildren().addAll(userLabel, empLabel, dateLabel, amountLabel);
                root.getChildren().add(paymentBox);
            }
        }

        Scene scene = new Scene(root, 500, 600);
        setScene(scene);
    }
}
