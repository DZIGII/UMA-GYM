package org.example.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.controller.AddUserSubscription;
import org.example.model.User;
import org.example.util.JDBCUtilities;

import java.util.Optional;

public class UserOptionView {

    private final TableView<User> tableUser;

    public UserOptionView(TableView<User> tableUser) {
        this.tableUser = tableUser;
        showView();
    }


    private void showView() {
        User user = tableUser.getSelectionModel().getSelectedItem();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UTILITY);
        stage.setTitle("Opcije Korisnika");

        Button addSubscriptionBtn = new Button("Dodaj Pretplatu");
        Button pastSubscriptionsBtn = new Button("Aktivnost Korisnika");
        Button deleteUserBtn = new Button("Obrisi Korisnika");

        Font font = new Font("Arial", 18);
        addSubscriptionBtn.setFont(font);
        pastSubscriptionsBtn.setFont(font);
        deleteUserBtn.setFont(font);

        addSubscriptionBtn.setStyle("""
            -fx-background-color: #1e90ff;
            -fx-text-fill: white;
            -fx-border-color: white;
            -fx-border-width: 0 1 0 0;
        """);

        pastSubscriptionsBtn.setStyle("""
            -fx-background-color: #28a745;
            -fx-text-fill: white;
        """);
        deleteUserBtn.setStyle("""
            -fx-background-color: #FF0000;
            -fx-text-fill: white;
        """);

        addSubscriptionBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pastSubscriptionsBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        deleteUserBtn.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        HBox hbox = new HBox(addSubscriptionBtn, pastSubscriptionsBtn, deleteUserBtn);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefSize(500, 80);
        hbox.setSpacing(0);

        Scene scene = new Scene(hbox);
        stage.setScene(scene);

        addSubscriptionBtn.setOnAction(e -> {
            stage.close();
            AddSubscriptionView.getInstance(user).show();
        });

        pastSubscriptionsBtn.setOnAction(e -> {
            stage.close();
            new MembershipHistoryView(user);
        });

        deleteUserBtn.setOnAction(e -> {
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
                    stage.close();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Greška");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Došlo je do greške pri brisanju korisnika.");
                    errorAlert.showAndWait();
                }
            }
        });

        stage.showAndWait();
    }
}
