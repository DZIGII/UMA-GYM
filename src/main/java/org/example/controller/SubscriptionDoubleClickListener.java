package org.example.controller;

import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.example.model.Subscription;
import org.example.view.AddNewPaymentView;
import org.example.view.PaymentOptionsView;
import org.example.view.PaymentsOverviewView;

public class SubscriptionDoubleClickListener implements EventHandler<MouseEvent> {

    private TableView<Subscription> subscriptionTable;

    public SubscriptionDoubleClickListener(TableView<Subscription> subscriptionTable) {
        this.subscriptionTable = subscriptionTable;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            Subscription subscription = subscriptionTable.getSelectionModel().getSelectedItem();

            if (subscription != null) {
                new PaymentOptionsView(subscription);
            }
        }
    }
}
