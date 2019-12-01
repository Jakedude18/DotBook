package com.Band.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        PostScriptGenerator postScriptGenerator = new PostScriptGenerator();
        Dot dot = new Dot("on 50 12 bfs", 1, 3);
        postScriptGenerator.addDot(dot);

        File file = new File("/home/fuzzy/src/Java/DotBook/src/main/resources/KanyRest-CodeChallenge.pdf");

        OCRService ocrService = new OCRService();
        List<File> pdfImages = pdfToImage(file);
        String s = ocrService.parseImage(pdfImages.get(0));

        System.out.println(s);
        System.out.println(postScriptGenerator.showPage());
    }

    public static List<File> pdfToImage(File pdf) throws IOException {
        List pageImages = new ArrayList<>();
        final PDDocument pdfDocument = PDDocument.load(pdf);
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
        for (int page = 0; page < pdfDocument.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            File outputFile = new File(String.format("%s(%d).png", pdf.getName(), page));
            ImageIO.write(bim, "png", outputFile);
            pageImages.add(outputFile);
        }
        pdfDocument.close();
        return pageImages;
    }
}
