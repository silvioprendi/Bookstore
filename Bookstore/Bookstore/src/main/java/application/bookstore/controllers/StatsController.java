package application.bookstore.controllers;

import application.bookstore.models.Author;
import application.bookstore.views.AuthorView;
import application.bookstore.views.StatsView;
import javafx.collections.FXCollections;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class StatsController {
    private StatsView statsView;
    public StatsController(StatsView statsView) {
        this.statsView = statsView;
    }


}
