package org.example.view;

import javafx.collections.FXCollections;
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
import org.example.controller.SubscriptionDoubleClickListener;
import org.example.model.Subscription;
import org.example.util.JDBCUtilities;

import java.util.Comparator;
import java.util.List;

public class SubscriptionView extends VBox {

    private static SubscriptionView instance;

    private Text title;
    private TextField search;
    private ChoiceBox<String> filterCb;
    private TableView<Subscription> tableUser;


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
        filterCb = new ChoiceBox<>();

        title = new Text("Aktivni Korisnici");
        title.setFont(Font.font("Arial", 24));

        tableUser = new TableView<>();

        TableColumn IDCol = new TableColumn<>("ID");
        IDCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn firstNameCol = new TableColumn<>("Ime");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn lastNameCol = new TableColumn<>("Prezime");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        TableColumn employeeCol = new TableColumn<>("Potpisao");
        employeeCol.setCellValueFactory(new PropertyValueFactory<>("employeeFullName"));

        TableColumn dateStartCol = new TableColumn<>("Početak pretplate");
        dateStartCol.setCellValueFactory(new PropertyValueFactory<>("dateStart"));

        TableColumn dateEndCol = new TableColumn<>("Kraj Pretplate");
        dateEndCol.setCellValueFactory(new PropertyValueFactory<>("dateEnd"));

        TableColumn treadMill = new TableColumn<>("Traka za trčanje");
        treadMill.setCellValueFactory(new PropertyValueFactory<>("treadmillIncluded"));

        TableColumn debt = new TableColumn<>("Dug");
        debt.setCellValueFactory(new PropertyValueFactory<>("debt"));

        TableColumn endIn = new TableColumn<>("Ističe za");
        endIn.setCellValueFactory(new PropertyValueFactory<>("endIn"));

        filterCb.getItems().addAll(List.of("Svi članovi", "Platio", "Nije platio", "Sortirano po kraju članarine"));
        filterCb.setValue("Svi članovi");


        tableUser.setItems(FXCollections.observableArrayList(JDBCUtilities.selectAllSubscriptions()));
        tableUser.getColumns().addAll(firstNameCol, lastNameCol, dateStartCol, dateEndCol, employeeCol, treadMill, debt, endIn);


    }

    private void showElements() {

        HBox hb = new HBox(10);
        HBox.setHgrow(search, Priority.ALWAYS);
        search.setMaxWidth(Double.MAX_VALUE);
        hb.getChildren().addAll(search, filterCb);
        hb.setSpacing(20);
        this.getChildren().addAll(title, hb, tableUser);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
    }

    private void controller() {
        tableUser.setOnMouseClicked(new SubscriptionDoubleClickListener(tableUser));

        search.textProperty().addListener((obs, oldVal, newVal) -> updateFilteredUsers());
        filterCb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateFilteredUsers());
    }

    private void updateFilteredUsers() {
        String searchText = search.getText().toLowerCase().trim();
        String filter = filterCb.getValue();

        List<Subscription> allSubscriptions = JDBCUtilities.selectAllSubscriptions();

        List<Subscription> filteredSubscriptions = allSubscriptions.stream()
                .filter(subscription -> {
                    boolean matchesSearch = searchText.isEmpty()
                            || subscription.getFirstName().toLowerCase().contains(searchText)
                            || subscription.getLastName().toLowerCase().contains(searchText);

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

        tableUser.setItems(FXCollections.observableArrayList(filteredSubscriptions));
    }

    public void refreshUserTable() {
        this.tableUser.setItems(FXCollections.observableArrayList(JDBCUtilities.selectAllSubscriptions()));
    }


}
