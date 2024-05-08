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

import java.io.File;

public class App extends Application {

    private TextField inputFileField;
    private TextField outputFileField;
    private ComboBox<String> inputFormatComboBox;
    private ComboBox<String> outputFormatComboBox;
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

        inputFormatComboBox = new ComboBox<>(FXCollections.observableArrayList("Text", "CSV", "PDF", "PNG", "Extract text from PNG. English", "Extract text from PNG. Estonian")); // Add more options as needed
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

        outputFormatComboBox = new ComboBox<>(FXCollections.observableArrayList("Text", "CSV", "PDF", "PNG")); // Add more options as needed
        grid.add(outputFormatComboBox, 1, 3);

        Button convertButton = new Button("Convert");
        convertButton.setOnAction(this::convertFile);
        grid.add(convertButton, 1, 4);

        Scene scene = new Scene(grid, 400, 200);
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

        switch (inputFormat) {
            case "CSV":
                switch (outputFormat) {
                    case "PDF":
                        Converter.SCVtoPDF(inputFilePath, outputFilePath);
                        break;
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
            case "Extract text from PNG. Estonian":
                switch (outputFormat) {
                    case "PDF":
                        Converter.PNGTextToPDFConverter(inputFilePath, outputFilePath, "est");
                        break;
                    default:
                        break;
                }
            case "Text":
                switch (outputFormat) {
                    case "PDF":
                        Converter.textToPdf(inputFilePath, outputFilePath);
                        break;
                    default:
                        break;
                }
            default:

                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
