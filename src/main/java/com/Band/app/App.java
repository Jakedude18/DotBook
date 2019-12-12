package com.Band.app;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        PostScriptGenerator postScriptGenerator = new PostScriptGenerator();
        Scanner in = new Scanner(System.in);
        File file = new File("/home/fuzzy/src/Java/DotBook/src/main/resources/Eternal Part 1- p. 1- 17 coordinates.pdf");
        OCRService ocrService = new OCRService();
        File pdfImage = pdfToImage(file, 4, 0);
        String s = ocrService.parseImage(pdfImage);
        System.out.println(s);
        System.out.println("let's go through this line by line because tesseract isn't perfect ):");
        String[] ary = s.split("\n");
        StringBuilder fixedS = new StringBuilder();
        for(int i = 0; i < ary.length; i++){
            String dot = ary[i];
            if(!dot.toLowerCase().matches("^(\\w+|\")+ *\\d+ *(\\w+ *[1,2]: *(([1-4](\\.[0-9]+)? *steps *(inside|outside))|on)|on) *([1-4]?5|([1-5]0)) *y\\w+ *\\w+ *((16(\\.0)?|([1-9](\\.[0-9]+)?|1[0-5](\\.[0-9]+)?)) *steps *(\\w+ *front \\w+|behind)|on) +(back|front) [hs].*") && !dot.equals("")) {
                System.out.println("gotta fix this dot : " + dot.toLowerCase());
                System.out.println("new dot is?");
                dot = in.nextLine();
            }
            fixedS.append(dot + "\n");
        }
        s = fixedS.toString();
        System.out.println(s);
        Scanner scanner = new Scanner(s);
        int iteration = 0;
        while (scanner.hasNextLine()) {
            StringBuilder line = new StringBuilder(scanner.nextLine());
            if (line.toString().equals("")) continue;
            iteration++;
            //because tesseract isn't perfect
            line.replace(line.indexOf(":") + 1, line.indexOf(":") + 1, " ");
            line.replace(4, 5, "S");
            postScriptGenerator.addDot(formatDot(line.toString().toLowerCase(), iteration));
        }
    }

    private static Dot formatDot(String dot, int iteration) {
        Pattern pattern = Pattern.compile("(16\\.0|([0-9]+|1[1-5])(\\.[0-9]+)?|inside|s(?! )|outside|y|front|behind|on|((back|front) [hs]))");
        Matcher matcher = pattern.matcher(dot);
        ArrayList<String> meta = new ArrayList<>();
        ArrayList<String> lr = new ArrayList<>();
        ArrayList<String> fb = new ArrayList<>();
        ArrayList<String> current = meta;
        while (matcher.find()) {
            switch (matcher.group(1)) {
                case "s":
                    if (current == meta) current = lr;
                    continue;
                case "y":
                    if (current == lr) current = fb;
                    continue;
                case "On":
                    if (current == meta) current = lr;
            }
            current.add(matcher.group(1));
        }
        StringBuilder formattedDot = new StringBuilder();
        //fixing meta data if tesseract make a oopsie
        if (meta.size() == 1) {
            meta.add(0, String.valueOf(iteration));
        }
        //left to right half of dot
        switch (lr.size()) {
            case (4):
                lr.set(2, (lr.get(2).equals("inside") ? "in" : "out"));
                formattedDot.append("s");
                break;
            case (3):
                formattedDot.append("s");
        }
        for (String str : lr) {
            formattedDot.append(str + " ");
        }
        //front to back half of dot
        formattedDot.append(fb.get(0) + " ");
        StringBuilder fBRelative = new StringBuilder();
        String[] ary = fb.get(fb.size() - 1).split(" ");
        if (fb.size() == 3) {
            fBRelative.append(fb.get(1).substring(0, 1));
        }
        for (String str : ary) {
            fBRelative.append(str.substring(0, 1));
        }
        formattedDot.append(fBRelative);

        return new Dot(meta.get(0), meta.get(1), formattedDot.toString());
    }

    private static File pdfToImage(File pdf, int quadrant, int page) throws IOException {
        File pageImage;
        final PDDocument pdfDocument = PDDocument.load(pdf);
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
        {
            Point topLeft = new Point();
            int dpi = 300;
            switch (quadrant) {
                case (1):
                    topLeft.setLocation(4.33 * dpi, .633 * dpi);
                    break;
                case (2):
                    topLeft.setLocation(.3165 * dpi, .633 * dpi);
                    break;
                case (3):
                    topLeft.setLocation(.3165 * dpi, 6.066 * dpi);
                    break;
                case (4):
                    topLeft.setLocation(4.33 * dpi, 6.066 * dpi);
            }
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, dpi, ImageType.RGB);
            File outputFile = new File(String.format("%s(%d).png", pdf.getName(), page));
            ImageIO.write(bim.getSubimage(topLeft.x, topLeft.y, 950, 1350), "png", outputFile);
            pageImage = outputFile;
        }
        pdfDocument.close();
        return pageImage;
    }
}
