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
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        PostScriptGenerator postScriptGenerator = new PostScriptGenerator();


        File file = new File("/home/fuzzy/src/Java/DotBook/src/main/resources/Eternal Part 1- p. 1- 17 coordinates.pdf");

        /*OCRService ocrService = new OCRService();
        List<File> pdfImages = pdfToImage(file);
        String s = ocrService.parseImage(pdfImages.get(0));
        System.out.println(s);
        Dot dot = new Dot(1,3,"s1 3 out 50 10 bbh");
        Dot dot1 = new Dot(2,8, "on 50 3 bbh");
        postScriptGenerator.addDot(dot);
        postScriptGenerator.addDot(dot1);*/
        Scanner scanner = new Scanner("1 3 Side 2:03.75 steps inside 20 yd ln On Back Hash");
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            if(line.equals("")) continue;
            //because tesseract isn't perfect
            if(line.charAt(line.indexOf(":") + 1) == '0') line = line.replaceFirst("0"," ");
            postScriptGenerator.addDot(formatDot(line));
        }
    }

    private static Dot formatDot(String dot){
        Scanner dotScanner = new Scanner(dot);
        int set = dotScanner.nextInt();
        int counts = dotScanner.nextInt();
        StringBuilder formattedDot = new StringBuilder();
        if(!dot.contains("on")){
            formattedDot.append(String.format())
        }
        else if(dot.contains("Side")){

        }
        else{

        }
        formattedDot.append(String.format(""))
        Pattern pattern = Pattern.compile("50 |5 |0 ");
        Matcher m = pattern.matcher(dot);
        m.find();
        return new Dot(set,counts,formattedDot.toString());
    }
    private static List<File> pdfToImage(File pdf) throws IOException {
        List<File> pageImages = new ArrayList<>();
        final PDDocument pdfDocument = PDDocument.load(pdf);
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
        for (int page = 0; page < pdfDocument.getNumberOfPages(); ++page) {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            File outputFile = new File(String.format("%s(%d).png", pdf.getName(), page));
            ImageIO.write(bim.getSubimage(95,190,950,560), "png", outputFile);
            pageImages.add(outputFile);
        }
        pdfDocument.close();
        return pageImages;
    }
}
