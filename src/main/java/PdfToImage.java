import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class PdfToImage {
    public static void pdfImage(String inputPath, String outputPath) {
        try {
            PDDocument document = PDDocument.load(new File(inputPath));
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
}
