package org.openjfx;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.converter.Converter;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressIndicator;



import java.io.File;

public class App extends Application {

    private TextField inputFileField;
    private TextField outputFileField;
    private ComboBox<String> inputFormatComboBox;
    private ComboBox<String> outputFormatComboBox;
    private ProgressIndicator progressIndicator;

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

        Label inputFormatLabel = new Label("Input Format:");
        grid.add(inputFormatLabel, 0, 1);

        inputFormatComboBox = new ComboBox<>(FXCollections.observableArrayList("Text", "CSV", "PDF", "PNG", "Extract text from PNG. English", "Extract text from PNG. Estonian"));
        grid.add(inputFormatComboBox, 1, 1);

        Label outputFileLabel = new Label("Output File:");
        grid.add(outputFileLabel, 0, 2);

        outputFileField = new TextField();
        grid.add(outputFileField, 1, 2);

        Button outputBrowseButton = new Button("Browse");
        outputBrowseButton.setOnAction(this::browseOutputFile);
        grid.add(outputBrowseButton, 2, 2);

        Label outputFormatLabel = new Label("Output Format:");
        grid.add(outputFormatLabel, 0, 3);

        outputFormatComboBox = new ComboBox<>(FXCollections.observableArrayList("Text", "CSV", "PDF", "PNG"));
        grid.add(outputFormatComboBox, 1, 3);

        Button convertButton = new Button("Convert");
        convertButton.setOnAction(this::convertFile);
        grid.add(convertButton, 1, 4);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        grid.add(progressIndicator, 1, 5);


        Scene scene = new Scene(grid, 400, 250);
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
        String inputFilePath = inputFileField.getText();
        String outputFilePath = outputFileField.getText();
        String inputFormat = inputFormatComboBox.getValue();
        String outputFormat = outputFormatComboBox.getValue();


        progressIndicator.setVisible(true);


        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                try {
                    switch (inputFormat) {
                        case "CSV":
                            switch (outputFormat) {
                                case "PDF":
                                    Converter.SCVtoPDF(inputFilePath, outputFilePath);
                                    break;
                                case "Text":
                                    Converter.convertCSVtoText(inputFilePath, outputFilePath);
                                default:
                                    break;
                            }
                            break;
                        case "PDF":
                            switch (outputFormat) {
                                case "PNG":
                                    Converter.PDFToPNG(inputFilePath, outputFilePath);
                                    break;
                                case "Text":
                                    Converter.PDFtoText(inputFilePath, outputFilePath);
                                    break;
                                case "CSV":
                                    Converter.extractPDFspreadsheets(inputFilePath, outputFilePath);
                                    break;
                                default:
                                    //
                                    break;
                            }
                            break;
                        case "PNG":
                            switch (outputFormat) {
                                case "PDF":
                                    // png to pdf
                                    break;

                                default:
                                    //
                                    break;
                            }
                            break;
                        case "Extract text from PNG. English":
                            switch (outputFormat) {
                                case "PDF":
                                    Converter.PNGTextToPDFConverter(inputFilePath, outputFilePath, "eng");
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case "Extract text from PNG. Estonian":
                            switch (outputFormat) {
                                case "PDF":
                                    Converter.PNGTextToPDFConverter(inputFilePath, outputFilePath, "est");
                                    break;
                                default:
                                    break;
                            }
                            break;
                        case "Text":
                            switch (outputFormat) {
                                case "PDF":
                                    Converter.textToPdf(inputFilePath, outputFilePath);
                                    break;
                                case "CSV":
                                    Converter.textToCSV(inputFilePath, outputFilePath);
                                    break;
                                case "PNG":
                                    Converter.textToPNG(inputFilePath, outputFilePath);
                                    break;
                                default:
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                } catch (Exception e) {
                    throw e;
                }

                return null;
            }

            @Override
            protected void succeeded() {
                progressIndicator.setVisible(false);
                showAlert("Success", "File converted successfully.");
            }

            @Override
            protected void failed() {
                progressIndicator.setVisible(false);
                showAlert("Error", "File conversion failed.");
            }
        };

        new Thread(task).start();
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
