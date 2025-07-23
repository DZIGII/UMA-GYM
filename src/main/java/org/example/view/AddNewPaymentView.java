package org.example.view;

import javafx.stage.Stage;
import org.example.model.Employee;
import org.example.model.Payment;
import org.example.model.Subscription;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.util.JDBCUtilities;

import java.time.LocalDate;

public class AddNewPaymentView extends Stage {

    private final Subscription subscription;

    public AddNewPaymentView(Subscription subscription) {
        this.subscription = subscription;
        setTitle("Plaćanje duga");

        Label debtLabel = new Label("Preostali dug: " + subscription.getDebt());

        Label amountLabel = new Label("Iznos uplate:");
        TextField amountField = new TextField();

        Button payButton = new Button("Plati");
        Label statusLabel = new Label();

        GridPane layout = new GridPane();
        layout.setPadding(new Insets(20));
        layout.setVgap(10);
        layout.setHgap(10);

        layout.add(debtLabel, 0, 0, 2, 1);
        layout.add(amountLabel, 0, 1);
        layout.add(amountField, 1, 1);
        layout.add(payButton, 1, 2);
        layout.add(statusLabel, 1, 3);

        payButton.setOnAction(e -> {
            try {
                int debt = Integer.parseInt(subscription.getDebt().replace(" din", "").trim());
                int amount = Integer.parseInt(amountField.getText().trim());

                if (amount <= 0) {
                    statusLabel.setText("Iznos mora biti pozitivan.");
                    return;
                }

                if (amount > debt) {
                    statusLabel.setText("Ne možete platiti više od duga (" + debt + " din).");
                    return;
                }

                Employee employee = JDBCUtilities.currentEmployee;

                Payment payment = new Payment(
                        subscription.getUser(),
                        subscription,
                        employee,
                        LocalDate.now(),
                        amount
                );


                boolean success = JDBCUtilities.addPayment(subscription.getUser().getId(), subscription.getId(), employee.getId(), LocalDate.now(), amount);
                if (success) {
                    subscription.addPayment(payment);
                    SubscriptionView.getInstance().refreshUserTable();
                    statusLabel.setText("Uplata uspešno zabeležena.");
                    this.close();
                } else {
                    statusLabel.setText("Greška pri unosu uplate u bazu.");
                }

            } catch (NumberFormatException ex) {
                statusLabel.setText("Unesite validan broj.");
            } catch (Exception ex) {
                ex.printStackTrace();
                statusLabel.setText("Došlo je do greške.");
            }
        });

        Scene scene = new Scene(layout);
        setScene(scene);
        show();
    }
}

