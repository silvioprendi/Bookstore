package application.bookstore.views;

import application.bookstore.controllers.StatsController;
import application.bookstore.models.BookOrder;
import application.bookstore.models.Order;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;


import java.util.ArrayList;


public class StatsView extends View{
    private final StackPane stackPane = new StackPane();
    private final VBox vBox = new VBox();


    public StatsView() {
        new StatsController(this);
    }

    public Parent getView() {
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        Order.getOrders().clear();
        ArrayList<Order> orders = Order.getOrders();

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for(Order order : orders) {
            for (BookOrder bookOrder: order.getBooksOrdered()){
                int match=0;
                for (PieChart.Data data: pieChartData) {
                    if (bookOrder.getTitle().matches(data.getName())) {
                        data.setPieValue(data.getPieValue() + bookOrder.getQuantity());
                        match = 1;
                        break;
                    }
                }
                if (match==0) {
                    pieChartData.add(new PieChart.Data(bookOrder.getTitle(), bookOrder.getQuantity()));
                }
            }
        }

        for (PieChart.Data data: pieChartData){
            System.out.println(data.getName()+" "+  data.getPieValue());
        }

        pieChartData.forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " ", data.pieValueProperty(),  " Pieces Sold"
                    )
            );
        });


        final PieChart chart = new PieChart(pieChartData);


        chart.setTitle("Books Statistics");

        vBox.getChildren().add(chart);
        ImageView imageView = new ImageView(String.valueOf(LoginView.class.getResource("/images/ChangePassBackground.png")));
        imageView.setFitHeight(620);
        imageView.setFitWidth(1200);

        stackPane.getChildren().addAll(imageView, vBox);

        return stackPane;

    }

    public Parent getView2() {
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        ArrayList<Order> orders = Order.getOrders();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for(Order order : orders) {
            int match=0;
            for (PieChart.Data data: pieChartData) {
                if (order.getClientName().matches(data.getName())) {
                    data.setPieValue(data.getPieValue()+ (Math.round(order.getTotal()*100)/100));
                    match=1;
                    break;
                }
            }
            if (match==0) {
                pieChartData.add(new PieChart.Data(order.getClientName(), (Math.round(order.getTotal()*100)/100)));
            }
        }

        pieChartData.forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " ", data.pieValueProperty(),  " $"
                    )
            );
        });

        final PieChart chart = new PieChart(pieChartData);

        chart.setTitle("Clients Statistics");
        chart.setLegendSide(Side.RIGHT);

        vBox.getChildren().addAll(chart);
        ImageView imageView = new ImageView(String.valueOf(LoginView.class.getResource("/images/ChangePassBackground.png")));
        imageView.setFitHeight(620);
        imageView.setFitWidth(1200);

        stackPane.getChildren().addAll(imageView, vBox);

        return stackPane;
    }

    public Parent getView3() {
        vBox.setSpacing(10);
        vBox.setAlignment(Pos.CENTER);

        ArrayList<Order> orders = Order.getOrders();
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

        for(Order order : orders) {
            int match=0;
            for (PieChart.Data data: pieChartData) {
                if (order.getUsername().matches(data.getName())) {
                    data.setPieValue(data.getPieValue()+ Math.round(order.getTotal()*100)/100);
                    match=1;
                    break;
                }
            }
            if (match==0) {
                pieChartData.add(new PieChart.Data(order.getUsername(),  Math.round(order.getTotal()*100)/100));
            }
        }

        pieChartData.forEach(data -> {
            data.nameProperty().bind(
                    Bindings.concat(
                            data.getName(), " ", data.pieValueProperty(),  " $"
                    )
            );
        });

        final PieChart chart = new PieChart(pieChartData);

        chart.setTitle("Users Statistics");
        chart.setLegendSide(Side.LEFT);


        vBox.getChildren().addAll(chart);
        ImageView imageView = new ImageView(String.valueOf(LoginView.class.getResource("/images/ChangePassBackground.png")));
        imageView.setFitHeight(620);
        imageView.setFitWidth(1200);

        stackPane.getChildren().addAll(imageView, vBox);

        return stackPane;
    }
}



