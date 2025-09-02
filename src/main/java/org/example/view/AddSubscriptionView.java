package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.model.*;
import org.example.util.JDBCUtilities;

import java.time.LocalDate;

public class AddSubscriptionView extends Stage {

    private static AddSubscriptionView instance;

    private final User user;

    private ChoiceBox<String> typeCb;
    private TextField priceTf;
    private TextField amountPaidTf;
    private DatePicker startDatePicker;
    private Label endDateLabel;
    private DatePicker customEndDatePicker;
    private Label remainingAmountLabel;
    private CheckBox treadmillCb;
    private Button addBtn;

    private AddSubscriptionView(User user) {
        this.user = user;
        initUI();
        this.setTitle("Dodaj Pretplatu");
        this.initModality(Modality.APPLICATION_MODAL);
        this.setResizable(false);
    }

    public static AddSubscriptionView getInstance(User user) {
        if (instance == null || !instance.getUser().equals(user)) {
            instance = new AddSubscriptionView(user);
        }
        return instance;
    }

    public User getUser() {
        return user;
    }

    private void initUI() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Dodaj Pretplatu");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        typeCb = new ChoiceBox<>();
        typeCb.getItems().addAll("Dnevna", "Nedeljna", "Polumesečna", "Mesečna", "Custom");
        typeCb.setValue("Mesečna");

        priceTf = new TextField();
        priceTf.setPromptText("Cena");

        amountPaidTf = new TextField();
        amountPaidTf.setPromptText("Plaćeno");

        startDatePicker = new DatePicker(LocalDate.now());

        endDateLabel = new Label("Datum završetka: ");
        customEndDatePicker = new DatePicker();
        customEndDatePicker.setVisible(false);

        remainingAmountLabel = new Label("Preostalo za platiti: 0 RSD");

        treadmillCb = new CheckBox("Traka za trčanje uključena");

        addBtn = new Button("Dodaj pretplatu");

        // Event listeners
        typeCb.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> updateEndDateDisplay());
        startDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> updateEndDateDisplay());

        amountPaidTf.textProperty().addListener((obs, oldVal, newVal) -> updateRemainingAmount());
        priceTf.textProperty().addListener((obs, oldVal, newVal) -> updateRemainingAmount());

        addBtn.setOnAction(e -> handleAddSubscription());

        GridPane form = new GridPane();
        form.setVgap(10);
        form.setHgap(10);
        form.setAlignment(Pos.CENTER);

        VBox endDateBox = new VBox(5, endDateLabel, customEndDatePicker);
        endDateBox.setAlignment(Pos.CENTER_LEFT);

        form.addRow(0, new Label("Tip pretplate:"), typeCb);
        form.addRow(1, new Label("Cena:"), priceTf);
        form.addRow(2, new Label("Plaćeno:"), amountPaidTf);
        form.addRow(3, new Label("Početak:"), startDatePicker);
        form.addRow(4, new Label("Kraj:"), endDateBox);
        form.addRow(5, new Label("Opcije:"), treadmillCb);
        form.addRow(6, new Label("Stanje:"), remainingAmountLabel);

        root.getChildren().addAll(title, form, addBtn);

        Scene scene = new Scene(root, 420, 420);
        this.setScene(scene);

        updateEndDateDisplay();
    }

    private void updateEndDateDisplay() {
        LocalDate start = startDatePicker.getValue();
        String type = typeCb.getValue();

        if (type.equals("Custom")) {
            endDateLabel.setText("Izaberite kraj:");
            customEndDatePicker.setVisible(true);
        } else {
            customEndDatePicker.setVisible(false);
            LocalDate end = switch (type) {
                case "Dnevna" -> start.plusDays(1);
                case "Nedeljna" -> start.plusWeeks(1);
                case "Polumesečna" -> start.plusDays(15);
                case "Mesečna" -> start.plusMonths(1);
                default -> start;
            };
            endDateLabel.setText("Datum završetka: " + end.toString());
        }
    }

    private void updateRemainingAmount() {
        try {
            int total = Integer.parseInt(priceTf.getText().trim());
            int paid = Integer.parseInt(amountPaidTf.getText().trim());
            int remaining = Math.max(total - paid, 0);
            remainingAmountLabel.setText("Preostalo za platiti: " + remaining + " RSD");
        } catch (NumberFormatException e) {
            remainingAmountLabel.setText("Preostalo za platiti: --");
        }
    }

    private void handleAddSubscription() {
        String type = typeCb.getValue();
        String priceStr = priceTf.getText().trim();
        String paidStr = amountPaidTf.getText().trim();

        if (priceStr.isEmpty() || paidStr.isEmpty()) {
            showAlert("Greška", "Unesite cenu i iznos koji je plaćen.");
            return;
        }

        int price, paid;
        try {
            price = Integer.parseInt(priceStr);
            paid = Integer.parseInt(paidStr);

            if (paid < 0) {
                showAlert("Greška", "Plaćeni iznos ne može biti negativan.");
                return;
            }

            if (paid > price) {
                showAlert("Greška", "Plaćeni iznos ne može biti veći od cene pretplate (" + price + " RSD).");
                return;
            }

        } catch (NumberFormatException e) {
            showAlert("Greška", "Cena i plaćeni iznos moraju biti brojevi.");
            return;
        }

        LocalDate start = startDatePicker.getValue();
        LocalDate end;

        if (type.equals("Custom")) {
            end = customEndDatePicker.getValue();
            if (end == null || end.isBefore(start)) {
                showAlert("Greška", "Molimo izaberite validan datum završetka.");
                return;
            }
        } else {
            end = switch (type) {
                case "Dnevna" -> start.plusDays(1);
                case "Nedeljna" -> start.plusWeeks(1);
                case "Polumesečna" -> start.plusDays(15);
                case "Mesečna" -> start.plusMonths(1);
                default -> start;
            };
        }

        boolean treadmill = treadmillCb.isSelected();

        Employee employee = JDBCUtilities.currentEmployee;

        int subscriptionId = JDBCUtilities.addSubscription(type, price, treadmill, start, end, user.getId(), employee.getId());

        if (subscriptionId != -1) {
            Subscription subscription = new Subscription(subscriptionId, type, price, treadmill, start, end, user, employee);
            subscription.setId(subscriptionId);

            if (paid > 0){
                JDBCUtilities.addPayment(user.getId(), subscriptionId, employee.getId(), LocalDate.now(), paid);
            }

            MembershipHistory membershipHistory = JDBCUtilities.addMembershipHistory(user, start.getMonthValue(), start.getYear(), subscription, employee);
            System.out.println(membershipHistory);

            SubscriptionView.getInstance().refreshUserTable();
            showAlert("Uspeh", "Pretplata i uplata uspešno dodate.");
            this.close();
        }


        this.close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.initOwner(this);
        alert.showAndWait();
    }
}
