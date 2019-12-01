package com.Band.app;

import javafx.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Optional;

class Dot {
    private String[] coordinateSplit;
    private Pair<String,String> coordinate;
    private int set,counts;

    Dot(String coordinate,int set,int counts) {
        if (Pattern.matches("^(s[1,2] [1-4] ((in|out)|on) ([1-4]?5|([1-5]0))|on 50) ((([1-9]|10) (fbh|bfh))|(([1-9]|1[0-6]) (((bf|fb)s)|((b{2}|f{2})h))))$", coordinate)) {
            coordinate = coordinate.toLowerCase();
            boolean onCoordinate = coordinate.contains("on");
            int splittingPoint = onCoordinate ? 1 : 2;
            Pattern pattern = Pattern.compile("50 |5 |0 ");
            Matcher m = pattern.matcher(coordinate);
            for (int i = 0; i < splittingPoint; i++) m.find();
            this.coordinate = new Pair<String, String>(coordinate.substring(0, m.end() - 1), coordinate.substring(m.end()));
            if (onCoordinate) {
                coordinate = String.format("%s 0 in %s ", coordinate.substring(0, 2), getCoordinateX().substring(getCoordinateX().length() - 2));
                //special case for on 50
                if (!coordinate.contains("s")) coordinate = "s1 0 in 50 ";
                coordinate += this.coordinate.getValue();
            }
            this.coordinateSplit = coordinate.split(" ");
        }
        if(this.coordinate == null) System.out.println("coordinate entered does not match regex");
        this.set = set;
        this.counts = counts;
    }

    String getCoordinateX(){
        return coordinate.getKey();
    }

    String getCoordinateY(){
        return coordinate.getValue();
    }

    int side() {
        return Integer.valueOf(coordinateSplit[0].substring(1));
    }

    int ltr() {
        return Integer.valueOf(coordinateSplit[1]);
    }

    String inOut() {
        return  coordinateSplit[2];
    }

    int ltrRel(){
        return Integer.valueOf(coordinateSplit[3]);
    }

    int ftb(){
        return Integer.valueOf(coordinateSplit[4]);
    }

    String ftbRel(){
        return coordinateSplit[5].substring(0,1);
    }

    int getSet() {
        return set;
    }

    int getCounts() {
        return counts;
    }
}
