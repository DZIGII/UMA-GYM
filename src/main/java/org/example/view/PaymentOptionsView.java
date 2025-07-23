package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.model.Subscription;

public class PaymentOptionsView {

    private final Subscription subscription;

    public PaymentOptionsView(Subscription subscription) {
        this.subscription = subscription;
        showView();
    }

    private void showView() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Opcije plaćanja");

        Button addPaymentBtn = new Button("Dodaj Uplatu");
        Button viewPaymentsBtn = new Button("Detalji o uplatama");

        Font font = new Font("Arial", 18);
        addPaymentBtn.setFont(font);
        viewPaymentsBtn.setFont(font);

        addPaymentBtn.setStyle("""
            -fx-background-color: #1e90ff;
            -fx-text-fill: white;
            -fx-border-color: white;
            -fx-border-width: 0 1 0 0;
        """);

        viewPaymentsBtn.setStyle("""
            -fx-background-color: #28a745;
            -fx-text-fill: white;
        """);

        addPaymentBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        viewPaymentsBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        HBox hbox = new HBox(addPaymentBtn, viewPaymentsBtn);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefSize(300, 150);
        hbox.setSpacing(0);

        Scene scene = new Scene(hbox);
        stage.setScene(scene);

        addPaymentBtn.setOnAction(e -> {
            stage.close();
            new AddNewPaymentView(subscription);
        });

        viewPaymentsBtn.setOnAction(e -> {
            stage.close();
            new PaymentsOverviewView(subscription);
        });

        stage.showAndWait();
    }
}
