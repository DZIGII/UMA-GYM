package org.example.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.model.Employee;
import org.example.util.JDBCUtilities;

import java.util.Optional;

public class EmployeeView extends VBox {

    private static EmployeeView instance;

    private Text title;
    private TextField search;
    private TableView<Employee> tableEmployee;

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

        title = new Text("Zaposleni");
        title.setFont(Font.font("Arial", 24));

        tableEmployee = new TableView<>();

        TableColumn<Employee, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Employee, String> firstNameCol = new TableColumn<>("Ime");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Prezime");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastNmae"));

        TableColumn<Employee, String> phoneNumberCol = new TableColumn<>("Telefon");
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        TableColumn<Employee, String> dateOfBirthCol = new TableColumn<>("Datum rodjenja");
        dateOfBirthCol.setCellValueFactory(new PropertyValueFactory<>("DateOfBirth"));

        TableColumn<Employee, String> isAdminCol = new TableColumn<>("Admin");
        isAdminCol.setCellValueFactory(new PropertyValueFactory<>("admin"));

        tableEmployee.setItems(FXCollections.observableArrayList(JDBCUtilities.selectAllEmployees()));

        tableEmployee.getColumns().addAll(idCol, firstNameCol, lastNameCol, phoneNumberCol, dateOfBirthCol, isAdminCol);
    }

    private void showElements() {
        HBox hb = new HBox(10);
        HBox.setHgrow(search, Priority.ALWAYS);
        search.setMaxWidth(Double.MAX_VALUE);
        hb.getChildren().add(search);

        this.getChildren().addAll(title, hb, tableEmployee);
        this.setPadding(new Insets(20));
        this.setAlignment(Pos.CENTER);
        this.setSpacing(20);
    }

    private void controller() {
        tableEmployee.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && tableEmployee.getSelectionModel().getSelectedItem() != null) {
                Employee selected = tableEmployee.getSelectionModel().getSelectedItem();

                if (JDBCUtilities.currentEmployee != null && JDBCUtilities.currentEmployee.isAdmin()) {
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Brisanje zaposlenog");
                    confirm.setHeaderText(null);
                    confirm.setContentText("Da li ste sigurni da želite da obrišete zaposlenog: " +
                            selected.getFirstName() + " " + selected.getLastNmae() + "?");

                    Optional<ButtonType> result = confirm.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        boolean deleted = JDBCUtilities.deleteEmployeeById(selected.getId());
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
        });

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterEmployees(newValue);
        });
    }

    private void filterEmployees(String text) {
        if (text == null || text.isEmpty()) {
            tableEmployee.setItems(FXCollections.observableArrayList(JDBCUtilities.selectAllEmployees()));
            return;
        }

        String lowerCaseFilter = text.toLowerCase();

        ObservableList<Employee> filteredList = FXCollections.observableArrayList();
        for (Employee emp : JDBCUtilities.selectAllEmployees()) {
            if ((emp.getFirstName() != null && emp.getFirstName().toLowerCase().contains(lowerCaseFilter)) ||
                    (emp.getLastNmae() != null && emp.getLastNmae().toLowerCase().contains(lowerCaseFilter))) {
                filteredList.add(emp);
            }
        }

        tableEmployee.setItems(filteredList);
    }


    public void refreshData() {
        tableEmployee.setItems(FXCollections.observableArrayList(JDBCUtilities.selectAllEmployees()));
    }

}


