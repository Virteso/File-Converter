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
import java.io.File;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;


public class Converter {

    public static void main(String[] args) {
        PNGTextToPDFConverter("C:\\Users\\timur02\\Downloads\\images (1).png", "C:\\Users\\timur02\\Downloads\\ssss", "est");
    }
    public static void pdfImage(String inputPath, String outputPath) {
        try {
            PDDocument document = Loader.loadPDF(new File(inputPath));
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int page = 0; page < document.getNumberOfPages(); ++page) {
                BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300);
                String outputFileName = outputPath + File.separator + "page_" + (page + 1) + ".png";
                ImageIO.write(bim, "png", new File(outputFileName));
            }
            document.close();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static void PNGTextToPDFConverter(String inputPath, String outputPath, String language) {
        try {
            ITesseract tesseract = new Tesseract();

            tesseract.setDatapath("src/main/resources/tessdata");
            tesseract.setLanguage(language);

            String text = tesseract.doOCR(new File(inputPath));

            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            PDType1Font font = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            contentStream.setFont(font, 10);
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
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
