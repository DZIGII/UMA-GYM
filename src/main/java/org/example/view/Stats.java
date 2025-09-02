package org.example.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;

import javafx.scene.text.Font;
import org.example.model.MonthStats;
import org.example.util.JDBCUtilities;

import java.util.List;

public class Stats extends VBox {

    private static Stats instance;

    private final CategoryAxis xAxisZarada = new CategoryAxis();
    private final NumberAxis yAxisZarada = new NumberAxis();
    private final BarChart<String, Number> chartZarada = new BarChart<>(xAxisZarada, yAxisZarada);
    private final XYChart.Series<String, Number> seriesZarada = new XYChart.Series<>();

    private final CategoryAxis xAxisVezbaci = new CategoryAxis();
    private final NumberAxis yAxisVezbaci = new NumberAxis();
    private final BarChart<String, Number> chartVezbaci = new BarChart<>(xAxisVezbaci, yAxisVezbaci);
    private final XYChart.Series<String, Number> seriesVezbaci = new XYChart.Series<>();

    public static Stats getInstance() {
        if (instance == null)
            instance = new Stats();
        return instance;
    }

    private Stats() {
        setSpacing(20);
        setPadding(new Insets(20));
        setAlignment(Pos.TOP_CENTER);

        initZaradaChart();
        initVezbaciChart();

        getChildren().addAll(chartZarada, chartVezbaci);
    }

    private void initZaradaChart() {
        chartZarada.setTitle("Ukupna zarada po mesecu");
        xAxisZarada.setLabel("Mesec");
        yAxisZarada.setLabel("Zarada (RSD)");

        List<MonthStats> podaci = JDBCUtilities.getLast12Months();
        for (MonthStats stat : podaci) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(stat.getMonth(), stat.getProfit());
            seriesZarada.getData().add(data);
        }

        chartZarada.getData().add(seriesZarada);
        chartZarada.lookupAll(".default-color0.chart-bar")
                .forEach(n -> n.setStyle("-fx-bar-fill: #4caf50;"));

        addTooltips(seriesZarada);
    }

    private void initVezbaciChart() {
        chartVezbaci.setTitle("Broj vežbača po mesecu");
        xAxisVezbaci.setLabel("Mesec");
        yAxisVezbaci.setLabel("Broj vežbača");

        List<MonthStats> podaci = JDBCUtilities.getLast12Months();
        for (MonthStats stat : podaci) {
            XYChart.Data<String, Number> data = new XYChart.Data<>(stat.getMonth(), stat.getNumOfClients());
            seriesVezbaci.getData().add(data);
        }

        chartVezbaci.getData().add(seriesVezbaci);
        chartVezbaci.lookupAll(".default-color0.chart-bar")
                .forEach(n -> n.setStyle("-fx-bar-fill: #2196f3;"));

        addTooltips(seriesVezbaci);
    }

    private void addTooltips(XYChart.Series<String, Number> series) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            Tooltip tooltip = new Tooltip(data.getXValue() + ": " + data.getYValue());
            Tooltip.install(data.getNode(), tooltip);
        }
    }


}
