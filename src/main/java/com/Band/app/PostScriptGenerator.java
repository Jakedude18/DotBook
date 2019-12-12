package com.Band.app;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.io.File;
import java.io.FileWriter;

class PostScriptGenerator {

    private double setX,setY,countsX,countsY, coordinateX, coordinateY, bottomLeftCornerX, bottomLeftCornerY, leftByRightX, leftByRightY, interval, padding, dotRadius;
    private int fontSize, squaresNum;
    private int pageCount = 1;
    private FileWriter fileWriter;

    PostScriptGenerator () throws Exception{
        InputStream is;
        Properties p = new Properties();
        is = new FileInputStream("config.properties");
        p.load(is);
        setX = Double.valueOf(p.getProperty("setX"));
        setY = Double.valueOf(p.getProperty("setY"));
        countsX = Double.valueOf(p.getProperty("countsX"));
        countsY = Double.valueOf(p.getProperty("countsY"));
        coordinateX = Double.valueOf(p.getProperty("coordinatesX"));
        coordinateY = Double.valueOf(p.getProperty("coordinatesY"));
        bottomLeftCornerX = Double.valueOf(p.getProperty("bottomLeftCornerX"));
        bottomLeftCornerY = Double.valueOf(p.getProperty("bottomLeftCornerY"));
        leftByRightX = Double.valueOf(p.getProperty("leftByRightX"));
        leftByRightY = Double.valueOf(p.getProperty("leftByRightY"));
        interval = Double.valueOf(p.getProperty("interval"));
        fontSize = Integer.valueOf(p.getProperty("fontSize"));
        squaresNum = (int)(leftByRightX/interval/8);
        padding = Integer.valueOf(p.getProperty("padding"));
        dotRadius = interval/2;
        //basic syntax for postscript files
    }

    private void changeFont ( int fontSize) throws Exception {
    fileWriter.write("/Times-Roman findfont\n");
    fileWriter.write(fontSize + " scalefont\n");
    fileWriter.write("setfont\n");
    fileWriter.write("newpath\n");

    }

    void addDot (Dot dot) throws Exception {
        final int dotsPer5Yards = 8;
        final int hashesPer10Yards = 10;
        final double yardsPerHash = 1.6;
        final int holeSize = 27;
        int s1 = dot.side() == 1 ? -1 : 1;
        File file;
        if (pageCount % 2 == 1) {
            setY -= holeSize;
            countsY -= holeSize;
            coordinateY -= holeSize;
            bottomLeftCornerY -= holeSize;
            file = new File(String.format("/home/fuzzy/Downloads/odd/%d.ps", pageCount));
        } else {
            setY += holeSize;
            countsY += holeSize;
            coordinateY += holeSize;
            bottomLeftCornerY += holeSize;
            file = new File(String.format("/home/fuzzy/Downloads/even/%d.ps", pageCount));
        }
        fileWriter = new FileWriter(file);
        pageCount++;
        changeFont(fontSize);
        //adding page marking
        fileWriter.write(String.format("%%%d%n", pageCount));
        //adding set
        fileWriter.write(String.format("%.1f %.1f moveto%n", setX, setY));
        fileWriter.write(String.format("(%s) show%n", dot.getSet()));
        //adding counts
        fileWriter.write(String.format("%.1f %.1f moveto%n", countsX, countsY));
        fileWriter.write(String.format("(%s) show%n", dot.getCounts()));
        //adding coordinate TODO weird thing with coordinate being null
        // formatting for on coordinate
        double coordinateFormat;
        if (dot.ltr() != 0) coordinateFormat = 0;
        else coordinateFormat = dot.ltrRel() == 50 ? .5 : .25;
        fileWriter.write(String.format("%.1f %.1f moveto%n", coordinateX + fontSize * coordinateFormat, coordinateY));
        fileWriter.write(String.format("(%s) show%n", dot.getCoordinateLR()));
        fileWriter.write(String.format("%.1f %.1f moveto%n", coordinateX + fontSize / 2, coordinateY - fontSize));
        fileWriter.write(String.format("(%s) show%n", dot.getCoordinateFB()));
        //adding the grid
        //stroke hash/sideline
        double lineHeight = bottomLeftCornerY + (dot.ftbRel().equals("f") ?
                interval * dotsPer5Yards / 2 : leftByRightY);
        fileWriter.write(String.format("%.1f %.1f moveto%n", bottomLeftCornerX, lineHeight));
        fileWriter.write(String.format("%.1f %.1f lineto%n", bottomLeftCornerX + leftByRightX, lineHeight));
        //adding hashes
        double startingX = bottomLeftCornerX + leftByRightX / 2 - dotsPer5Yards * interval;
        for (int i = 0; i < hashesPer10Yards + 1; i++) {
            double hashX = startingX + i * yardsPerHash * interval;
            double accent = i % 5 == 0 ? 1.5 : 1;
            fileWriter.write(String.format("%.1f %.1f moveto%n", hashX, lineHeight + interval * accent));
            fileWriter.write(String.format("%.1f %.1f lineto%n", hashX, lineHeight - interval * accent));
        }
        startingX += dotsPer5Yards * interval;
        //adding dot
        double centerX = startingX + interval * dot.ltr() * (dot.inOut().equals("in") ? 1 : -1) * s1;
        double centerY = lineHeight + interval * dot.ftb() * (dot.ftbRel().equals("f") ? 1 : -1);
        fileWriter.write(String.format("%.1f %.1f moveto%n", centerX + dotRadius, centerY + dotRadius));
        fileWriter.write(String.format("%.1f %.1f lineto%n", centerX - dotRadius, centerY - dotRadius));
        fileWriter.write(String.format("%.1f %.1f moveto%n", centerX + dotRadius, centerY - dotRadius));
        fileWriter.write(String.format("%.1f %.1f lineto%n", centerX - dotRadius, centerY + dotRadius));
        //adding end syntax
        fileWriter.write("2 setlinewidth\n");
        fileWriter.write("stroke\n");
        //adding number in squares
        changeFont((int) (fontSize / 1.5));
        for (int i = 0; i < squaresNum; i++) {
            fileWriter.write(String.format("%.1f %.1f moveto%n", bottomLeftCornerX - padding + interval * (dotsPer5Yards / 2 + (i * dotsPer5Yards)),
                    bottomLeftCornerY - interval + dotsPer5Yards / 4 * interval));
            int num = dot.ltrRel() - s1 * 5 * (squaresNum / 2) + 5 * s1 * i;
            if (num > 50) num = 50 - (num - 50);
            if (num > 0) fileWriter.write(String.format("(%d) show%n", num));
        }

        //adding closing syntax
        fileWriter.write("showpage\n");
        fileWriter.close();
        }


}
