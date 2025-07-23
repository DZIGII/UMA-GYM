package org.example.controller;

import javafx.event.EventHandler;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.example.model.User;
import org.example.view.AddSubscriptionView;
import org.example.view.UserOptionView;

public class AddUserSubscription implements EventHandler<MouseEvent> {

    private TableView<User> tableView;

    public AddUserSubscription(TableView<User> tableView) {
        this.tableView = tableView;
    }

    @Override
    public void handle(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {

            if (tableView != null) {
                new UserOptionView(tableView);
            }
        }
    }
}
