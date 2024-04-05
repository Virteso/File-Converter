package org.openjfx;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class App extends Application {

    private TextField inputFileField;
    private TextField outputFileField;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("File Converter");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        Label inputFileLabel = new Label("Input File:");
        grid.add(inputFileLabel, 0, 0);

        inputFileField = new TextField();
        grid.add(inputFileField, 1, 0);

        Button inputBrowseButton = new Button("Browse");
        inputBrowseButton.setOnAction(this::browseInputFile);
        grid.add(inputBrowseButton, 2, 0);

        Label outputFileLabel = new Label("Output File:");
        grid.add(outputFileLabel, 0, 1);

        outputFileField = new TextField();
        grid.add(outputFileField, 1, 1);

        Button outputBrowseButton = new Button("Browse");
        outputBrowseButton.setOnAction(this::browseOutputFile);
        grid.add(outputBrowseButton, 2, 1);

        Button convertButton = new Button("Convert");
        convertButton.setOnAction(this::convertFile);
        grid.add(convertButton, 1, 2);

        Scene scene = new Scene(grid, 400, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void browseInputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            inputFileField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void browseOutputFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            outputFileField.setText(selectedFile.getAbsolutePath());
        }
    }

    private void convertFile(ActionEvent event) {
        // converter
    }

    public static void main(String[] args) {
        launch(args);
    }
}
