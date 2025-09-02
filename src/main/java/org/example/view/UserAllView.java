package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.model.User;
import org.example.util.JDBCUtilities;

import java.util.ArrayList;
import java.util.List;

public class UserAllView extends VBox {

    private static UserAllView instance;

    private Text title;
    private TextField search;
    private ObservableList<User> originalUsers;
    private VBox cardContainer;

    private UserAllView() {
        initElements();
        showElements();
        controller();
    }

    public static UserAllView getInstance() {
        if (instance == null)
            instance = new UserAllView();
        return instance;
    }

    private void initElements() {
        search = new TextField();
        search.setPromptText("Pretraži korisnike...");
        originalUsers = FXCollections.observableArrayList(JDBCUtilities.selectAllCliens());

        title = new Text("Svi Korisnici");
        title.setFont(Font.font("Arial", 40));
    }

    private void showElements() {
        HBox searchBox = new HBox(10);
        HBox.setHgrow(search, Priority.ALWAYS);
        searchBox.getChildren().addAll(search);
        searchBox.setSpacing(20);

        search.setMaxWidth(Double.MAX_VALUE);
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

        cardContainer = new VBox();
        cardContainer.getChildren().addAll(addAllCards());
        cardContainer.setSpacing(20);
        cardContainer.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane(cardContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;"
        );

        VBox headerBox = new VBox(15, title, searchBox);
        headerBox.setAlignment(Pos.TOP_CENTER);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        VBox mainContent = new VBox();
        mainContent.getChildren().addAll(headerBox, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        this.getChildren().add(mainContent);
        this.setPadding(new Insets(20));
        this.setSpacing(0);
        this.setAlignment(Pos.TOP_CENTER);
    }

    private void controller() {
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            cardContainer.getChildren().clear();

            if (newValue == null || newValue.trim().isEmpty()) {
                cardContainer.getChildren().addAll(addAllCards());
            } else {
                String lowerCaseFilter = newValue.toLowerCase().trim();

                ObservableList<User> filteredList = originalUsers.filtered(user -> {
                    String firstName = user.getFirstName().toLowerCase();
                    String lastName = user.getLastName().toLowerCase();
                    String fullName = firstName + " " + lastName;

                    return firstName.contains(lowerCaseFilter) ||
                            lastName.contains(lowerCaseFilter) ||
                            fullName.contains(lowerCaseFilter);
                });

                for (User user : filteredList) {
                    cardContainer.getChildren().add(new UserCard(user));
                }
            }
        });
    }

    public void refreshData() {
        originalUsers.setAll(JDBCUtilities.selectAllCliens());
        cardContainer.getChildren().setAll(addAllCards());
    }

    public List<UserCard> addAllCards() {
        List<User> users = JDBCUtilities.selectAllCliens();
        List<UserCard> userCards = new ArrayList<>();

        for (User user : users) {
            userCards.add(new UserCard(user));
        }

        return userCards;
    }
}