package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.model.Employee;
import org.example.util.JDBCUtilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmployeeView extends VBox {

    private static EmployeeView instance;

    private Text title;
    private TextField search;
    private FlowPane cardContainer;
    private ObservableList<Employee> originalEmployees;

    private EmployeeView() {
        initElements();
        showElements();
        controller();
    }

    public static EmployeeView getInstance() {
        if (instance == null)
            instance = new EmployeeView();
        return instance;
    }

    private void initElements() {
        search = new TextField();
        search.setPromptText("Pretraži zaposlene...");
        originalEmployees = FXCollections.observableArrayList(JDBCUtilities.selectAllEmployees());

        title = new Text("Zaposleni");
        title.setFont(Font.font("Arial", 40));
    }

    private void showElements() {
        HBox searchBox = new HBox();
        HBox.setHgrow(search, Priority.ALWAYS);
        searchBox.getChildren().add(search);
        searchBox.setPadding(new Insets(0, 0, 20, 0));

        search.setMaxWidth(Double.MAX_VALUE);
        search.setPromptText("Pretraži zaposlene...");
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

        cardContainer = new FlowPane();
        cardContainer.setHgap(20);
        cardContainer.setVgap(20);
        cardContainer.setAlignment(Pos.TOP_CENTER);
        cardContainer.getChildren().addAll(addAllCards());

        ScrollPane scrollPane = new ScrollPane(cardContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle(
                "-fx-background-color: transparent;" +
                        "-fx-border-color: transparent;" +
                        "-fx-padding: 10;"
        );

        // VBox za header (title + search)
        VBox headerBox = new VBox(15, title, searchBox);
        headerBox.setAlignment(Pos.TOP_CENTER);
        headerBox.setPadding(new Insets(0, 0, 20, 0));

        // Glavni layout
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
            filterEmployees(newValue);
        });
    }

    private void filterEmployees(String text) {
        cardContainer.getChildren().clear();

        if (text == null || text.isEmpty()) {
            cardContainer.getChildren().addAll(addAllCards());
            return;
        }

        String lowerCaseFilter = text.toLowerCase().trim();

        for (Employee emp : originalEmployees) {
            if ((emp.getFirstName() != null && emp.getFirstName().toLowerCase().contains(lowerCaseFilter)) ||
                    (emp.getLastNmae() != null && emp.getLastNmae().toLowerCase().contains(lowerCaseFilter)) ||
                    (emp.getUserName() != null && emp.getUserName().toLowerCase().contains(lowerCaseFilter))) {

                cardContainer.getChildren().add(new EmployeeCard(emp));
            }
        }
    }

    public void refreshData() {
        originalEmployees.setAll(JDBCUtilities.selectAllEmployees());
        cardContainer.getChildren().setAll(addAllCards());
    }

    private List<EmployeeCard> addAllCards() {
        List<EmployeeCard> employeeCards = new ArrayList<>();
        for (Employee emp : originalEmployees) {
            EmployeeCard card = new EmployeeCard(emp);

            card.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2) {
                    handleEmployeeCardClick(emp);
                }
            });

            employeeCards.add(card);
        }
        return employeeCards;
    }

    private void handleEmployeeCardClick(Employee employee) {
        if (JDBCUtilities.currentEmployee != null && JDBCUtilities.currentEmployee.isAdmin()) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Brisanje zaposlenog");
            confirm.setHeaderText(null);
            confirm.setContentText("Da li ste sigurni da želite da obrišete zaposlenog: " +
                    employee.getFirstName() + " " + employee.getLastNmae() + "?");

            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean deleted = JDBCUtilities.deleteEmployeeById(employee.getId());
                if (deleted) {
                    refreshData();
                    Alert success = new Alert(Alert.AlertType.INFORMATION);
                    success.setTitle("Uspešno");
                    success.setHeaderText(null);
                    success.setContentText("Zaposleni je uspešno obrisan.");
                    success.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Greška");
                    error.setHeaderText(null);
                    error.setContentText("Došlo je do greške prilikom brisanja zaposlenog.");
                    error.showAndWait();
                }
            }
        }
    }
}