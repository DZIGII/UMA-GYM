package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.controller.AddUserSubscription;
import org.example.model.Subscription;
import org.example.model.User;
import org.example.util.JDBCUtilities;

import java.util.Comparator;
import java.util.List;

public class UserAllView extends VBox {

    private static UserAllView instance;

    private Text title;
    private TextField search;
    private ChoiceBox<String> filterCb;
    private TableView<User> tableUser;
    private ObservableList<User> originalUsers;

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
        title.setFont(Font.font("Arial", 24));

        tableUser = new TableView<>();

        TableColumn<User, Integer> IDCol = new TableColumn<>("ID");
        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<User, String> firstNameCol = new TableColumn<>("Ime");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<User, String> lastNameCol = new TableColumn<>("Prezime");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn<User, String> dateOfBirthCol = new TableColumn<>("Datum Rođenja");
        dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));

        TableColumn<User, String> noteCol = new TableColumn<>("Beleške");
        noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));

        ObservableList<User> users = FXCollections.observableArrayList(JDBCUtilities.selectAllCliens());

        tableUser.setItems(users);

        tableUser.getColumns().addAll(IDCol, firstNameCol, lastNameCol, dateOfBirthCol, noteCol);
    }

    private void showElements() {
        HBox hb = new HBox(10);
        HBox.setHgrow(search, Priority.ALWAYS);
        search.setMaxWidth(Double.MAX_VALUE);
        hb.getChildren().addAll(search);
        hb.setSpacing(20);


        this.getChildren().addAll(title, hb, tableUser);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
    }

    private void controller() {
        tableUser.setOnMouseClicked(new AddUserSubscription(tableUser));

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                tableUser.setItems(originalUsers);
            } else {
                String lowerCaseFilter = newValue.toLowerCase();

                ObservableList<User> filteredList = originalUsers.filtered(user ->
                        user.getFirstName().toLowerCase().contains(lowerCaseFilter) ||
                                user.getLastName().toLowerCase().contains(lowerCaseFilter)
                );

                tableUser.setItems(filteredList);
            }
        });

    }



    public void refreshData() {
        tableUser.setItems(FXCollections.observableArrayList(JDBCUtilities.selectAllCliens()));
    }

}
