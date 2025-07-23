package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.model.Employee;
import org.example.model.MembershipHistory;
import org.example.model.Subscription;
import org.example.model.User;
import org.example.util.JDBCUtilities;

import java.time.Month;
import java.util.List;

public class MembershipHistoryView extends Stage {

    private final User user;

    public MembershipHistoryView(User user) {
        this.user = user;
        setTitle("Istorija članstva za: " + user.getFirstName() + " " + user.getLastName());
        initUI();
        show();
    }

    private void initUI() {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setStyle("-fx-background-color: #f2f2f2;");

        scrollPane.setContent(root);

        root.setStyle("-fx-background-color: #f2f2f2;");

        Label title = new Label("Istorija članstva za: " + user.getFirstName() + " " + user.getLastName());
        title.setFont(Font.font("Arial", 20));
        title.setTextFill(Color.DARKBLUE);

        root.getChildren().add(title);

        List<MembershipHistory> historyList = JDBCUtilities.getMembershipHistoryForUser(user.getId());

        if (historyList.isEmpty()) {
            Label noData = new Label("Nema evidentiranih članstava.");
            noData.setFont(Font.font("Arial", 16));
            root.getChildren().add(noData);
        } else {
            for (MembershipHistory mh : historyList) {
                VBox historyBox = new VBox(5);
                historyBox.setPadding(new Insets(10));
                historyBox.setStyle("-fx-background-color: white; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5;");

                Employee employee = mh.getEmployee();
                Subscription subscription = mh.getSubscription();

                Label userLabel = new Label("Korisnik: " + user.getFirstName() + " " + user.getLastName());
                userLabel.setFont(Font.font("Arial", 16));

                Label empLabel = new Label("Zaposleni: " + employee.getFirstName() + " " + employee.getLastNmae());
                empLabel.setFont(Font.font("Arial", 14));

                Label dateLabel = new Label("Mesec i godina: " + Month.of(mh.getMonth()).name() + " " + mh.getYear());
                dateLabel.setFont(Font.font("Arial", 14));

                Label priceLabel = new Label("Cena: " + subscription.getPrice() + " din");
                priceLabel.setFont(Font.font("Arial", 14));

                Label typeLabel = new Label("Tip pretplate: " + subscription.getName());
                typeLabel.setFont(Font.font("Arial", 14));

                historyBox.getChildren().addAll(userLabel, empLabel, dateLabel, priceLabel, typeLabel);
                root.getChildren().add(historyBox);
            }
        }

        Scene scene = new Scene(scrollPane, 500, 600);
        setScene(scene);
    }
}
