package org.example.view;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.controller.SubscriptionDoubleClickListener;
import org.example.model.Subscription;
import org.example.util.JDBCUtilities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SubscriptionView extends VBox {

    private static SubscriptionView instance;

    private Text title;
    private TextField search;
    private ChoiceBox<String> filterCb;
    private TableView<Subscription> tableUser;
    private ScrollPane scrollPane;
    private VBox cardsContainer;


    private SubscriptionView() {
        initElements();
        showElements();
        controller();
    }

    public static SubscriptionView getInstance() {
        if (instance == null)
            instance = new SubscriptionView();
        return instance;
    }

    private void initElements() {

        search = new TextField();
        search.setPromptText("Search...");
        search.setStyle(
                "-fx-font-size: 17px;" +
                        "-fx-padding: 8 12;" +
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #ddd;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);"
        );
        filterCb = new ChoiceBox<>();
        filterCb.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-padding: 6 12;" +
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 12;" +
                        "-fx-border-color: #ddd;" +
                        "-fx-border-radius: 12;" +
                        "-fx-border-width: 1;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 3, 0, 0, 1);"
        );

        title = new Text("Aktivni Korisnici");
        title.setFont(Font.font("Arial", 40));

        tableUser = new TableView<>();



        filterCb.getItems().addAll(List.of("Svi članovi", "Platio", "Nije platio", "Sortirano po kraju članarine"));
        filterCb.setValue("Svi članovi");

    }

    private void showElements() {
        HBox hb = new HBox(10);
        HBox.setHgrow(search, Priority.ALWAYS);
        search.setMaxWidth(Double.MAX_VALUE);
        hb.setSpacing(20);
        hb.getChildren().addAll(search, filterCb);

        cardsContainer = new VBox(10);
        cardsContainer.setPadding(new Insets(10));
        cardsContainer.getChildren().addAll(getSubscriptionCards());

        scrollPane = new ScrollPane(cardsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;"
        );

        this.setPadding(new Insets(20));
        this.setSpacing(20);
        this.setAlignment(Pos.TOP_CENTER);
        this.getChildren().addAll(title, hb, scrollPane);
    }


    private void controller() {
        search.textProperty().addListener((obs, oldVal, newVal) -> updateFilteredUsers());
        filterCb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilteredUsers());
    }

    private void updateFilteredUsers() {
        String searchText = search.getText().toLowerCase().trim();
        String filter = filterCb.getValue();

        List<Subscription> allSubscriptions = JDBCUtilities.selectAllSubscriptions();

        List<Subscription> filteredSubscriptions = allSubscriptions.stream()
                .filter(subscription -> {
                    String fullName = subscription.getFirstName().toLowerCase() + " " + subscription.getLastName().toLowerCase();
                    boolean matchesSearch = searchText.isEmpty()
                            || subscription.getFirstName().toLowerCase().contains(searchText)
                            || subscription.getLastName().toLowerCase().contains(searchText)
                            || fullName.contains(searchText);

                    if (!matchesSearch) return false;

                    switch (filter) {
                        case "Platio":
                            return Integer.parseInt(subscription.getDebt().replaceAll("[^0-9]", "")) == 0;
                        case "Nije platio":
                            return Integer.parseInt(subscription.getDebt().replaceAll("[^0-9]", "")) > 0;
                        case "Sortirano po kraju članarine":
                            return true;
                        default:
                            return true;
                    }
                })
                .toList();

        if ("Sortirano po kraju članarine".equals(filter)) {
            filteredSubscriptions = filteredSubscriptions.stream()
                    .sorted(Comparator.comparing(Subscription::getDateEnd,
                            Comparator.nullsLast(Comparator.naturalOrder())))
                    .toList();
        }

        cardsContainer.getChildren().clear();
        for (Subscription s : filteredSubscriptions) {
            cardsContainer.getChildren().add(new SubscriptionCard(s));
        }
    }


    public void refreshUserTable() {
        cardsContainer.getChildren().clear();
        cardsContainer.getChildren().addAll(getSubscriptionCards());
    }


    public List<SubscriptionCard> getSubscriptionCards() {
        List<Subscription> subscriptions = JDBCUtilities.selectAllSubscriptions();
        List<SubscriptionCard> subscriptionCards = new ArrayList<>();

        for (Subscription s : subscriptions) {
            subscriptionCards.add(new SubscriptionCard(s));
        }

        return subscriptionCards;
    }

}
