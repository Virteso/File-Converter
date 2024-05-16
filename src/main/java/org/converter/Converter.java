package org.converter;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.rendering.PDFRenderer;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import org.apache.pdfbox.text.PDFTextStripper;

public class Converter {
    

    public static void textToPdf(String inputpath, String outputpath) throws Exception{
        try (BufferedReader reader = new BufferedReader(new FileReader(inputpath));
             PDDocument document = new PDDocument()) {
            File output = new File(outputpath);
            if (!output.exists()) output.mkdirs();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.TIMES_ROMAN);
            contentStream.setFont(font, 12);

            String line;
            float y = page.getMediaBox().getHeight() - 20;
            while ((line = reader.readLine()) != null) {
                contentStream.beginText();
                contentStream.newLineAtOffset(20, y);
                contentStream.showText(line);
                contentStream.endText();
                y -= 15;
            }
            contentStream.close();
            document.save(outputpath + File.separator + "output.pdf");
        } catch (Exception e) {
            throw e;
        }
    }

    public static void PDFtoText(String inputpath, String outputpath) throws Exception{
        File pdf = new File(inputpath);

        try (PDDocument document = Loader.loadPDF(pdf)) {
            PDFTextStripper textStripper = new PDFTextStripper();
            String text = textStripper.getText(document);

            File output = new File(outputpath);
            if (!output.exists()) output.mkdir();
            File outputFile = new File(output, "output.txt");
            try (Writer writer = new FileWriter(outputFile)) {
                writer.write(text);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            throw e;
        }
    }
    public static void SCVtoPDF(String inputpath, String outputpath) throws Exception{
        try {
            BufferedReader csvReader = new BufferedReader(new FileReader(inputpath));

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            File output = new File(outputpath);
            if (!output.exists()) output.mkdirs();

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA);
            int fontsize = 12;

            float y = page.getMediaBox().getHeight() - 50;
            double biggestCell = 0;
            String row;
            String pikimRida = "";
            double maxWidth = 0;
            Map<Integer, Float> columnAndSize = new HashMap<>();
            while ((row = csvReader.readLine()) != null) {
                String[] rida = row.split(",");
                for (int i = 0; i < rida.length; i++) {
                    if (columnAndSize.containsKey(i)) {
                        if (font.getStringWidth(rida[i]) > columnAndSize.get(i)) {
                            columnAndSize.put(i, font.getStringWidth(rida[i]));
                        }
                    } else {
                        columnAndSize.put(i, font.getStringWidth(rida[i]));
                    }
                }
                double lineWidth = 0;
                for (String s : rida) {
                    double cellSize = font.getStringWidth(s);
                    if (cellSize > biggestCell) biggestCell = cellSize;
                    lineWidth += cellSize;
                }
                if (lineWidth > maxWidth) {
                    maxWidth = lineWidth;
                    pikimRida = row;
                }

            }
            double pageWidth = page.getMediaBox().getWidth() - 100;
            while(maxWidth >= pageWidth && fontsize > 1) {
                fontsize --;
                maxWidth = font.getStringWidth(pikimRida) / 1000 * fontsize;
            }
            contentStream.setFont(font, fontsize);
            csvReader.close();
            csvReader = new BufferedReader(new FileReader(inputpath));


            while ((row = csvReader.readLine()) != null) {
                String[] cells = row.split(",");
                float x = 20;
                int column = 0;

                if (y - (fontsize + 10) < 20) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(font, fontsize);
                    y = page.getMediaBox().getHeight() - 50;
                }

                for (String cell : cells) {
                    contentStream.beginText();
                    contentStream.newLineAtOffset(x, y);
                    contentStream.showText(cell);
                    contentStream.endText();
                    x += columnAndSize.get(column)/1000 * fontsize + 10;
                    //x += biggestCell / 1000 * fontsize + 10;
                    //contentStream.moveTo(x, page.getMediaBox().getHeight() - 30);
                    //contentStream.lineTo(x - 5, y - font.getHeight(0) * cells.length - 5);
                    //contentStream.stroke();
                    column ++;
                }
                //contentStream.lineTo(x - 5, y - font.getHeight(0) * cells.length - 5);
                //contentStream.stroke();
                //contentStream.moveTo(45, y - font.getHeight(0) * cells.length - 5);
                //contentStream.lineTo(x - 65, y - font.getHeight(0) * cells.length - 5);
                //contentStream.stroke();
                y -= 20;

            }


            contentStream.close();
            document.save(outputpath + File.separator + "output.pdf");
            document.close();
            csvReader.close();



        } catch (IOException e) {
            throw e;
        }
    }
    public static void extractPDFspreadsheets (String inputpath, String outputpath) throws Exception{
        try (PDDocument document = Loader.loadPDF(new File(inputpath))){
            PDFTextStripper pdfStripper = new PDFTextStripper();
            File output = new File(outputpath);
            if (!output.exists()) output.mkdirs();
            String text = pdfStripper.getText(document);
            String[] lines = text.split("\\r?\\n");
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputpath + File.separator + "output.csv"))) {
                for (String line : lines) {
                    String[] columns = line.split(" ");
                    for (int i = 0; i < columns.length; i++) {
                        writer.write(columns[i]);
                        if (i < columns.length - 1) {
                            writer.write(",");
                        }
                    }
                    writer.newLine();
                }
            } catch (IOException e) {
                throw e;
            }
        } catch (IOException e) {
            throw e;
        }

    }
    
    public static void PDFToPNG(String inputPath, String outputPath) throws Exception {
        try {
            PDDocument document = Loader.loadPDF(new File(inputPath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            File output = new File(outputPath);
            if (!output.exists()) {
                output.mkdirs();
            }

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                String outputFileName = outputPath + File.separator + "page_" + (page + 1) + ".png";
                ImageIO.write(bim, "png", new File(outputFileName));
            }
            document.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public static void PNGTextToPDFConverter(String inputPath, String outputPath, String language) throws Exception{
        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("src/main/resources/tessdata");
            tesseract.setLanguage(language);

            String text = tesseract.doOCR(new File(inputPath));

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            File output = new File(outputPath);
            if (!output.exists()) output.mkdirs();

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            contentStream.setFont(font, 12);
            contentStream.newLineAtOffset(50, 700);
            String[] lines = text.split("\\r?\\n");
            float leading = 15;
            for (String line : lines) {
                if (line.isEmpty()) continue;
                contentStream.showText(line);
                contentStream.newLine();
                contentStream.newLineAtOffset(0, -leading);
            }
            contentStream.endText();
            contentStream.close();

            String outputFilePath = outputPath + File.separator + "output.pdf";
            document.save(outputFilePath);
            document.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
