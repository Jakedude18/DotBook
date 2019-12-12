package com.Band.app;

import javafx.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Dot {
    private String[] coordinateSplit;
    private Pair<String, String> coordinate;
    private String set;
    private String counts;

    Dot(String set, String counts, String coordinate) {
        if (Pattern.matches("^(s[1,2] (([1-4](\\.[0-9]+)? (in|out))|on) ([1-4]?5|([1-5]0))|on 50) ((([1-9](\\.[0-9]+)?|10) (fbh|bfh))|(([1-9](\\.[0-9]+)?|1[0-5](\\.[0-9]+)?)|on|16(\\.0)?) (((bf|fb)s)|((b{1,2}|f{1,2})h)))$", coordinate)) {
            Pattern pattern = Pattern.compile("(?<!\\.)(50|5|0)");
            Matcher m = pattern.matcher(coordinate);
            m.find();
            this.coordinate = new Pair<>(coordinate.substring(0, m.end()), coordinate.substring(m.end()));
            if (this.coordinate.getKey().contains("on")) {
                coordinate = String.format("%s 0 in %s ", coordinate.substring(0, 1) + coordinate.substring(1,2), getCoordinateLR().substring(getCoordinateLR().length() - 2));
                //special case for on 50
                if (!coordinate.contains("s")) coordinate = "s1 0 in 50 ";
            }
            else coordinate = getCoordinateLR();
            if(this.coordinate.getValue().contains("on")){
                coordinate += " 0 b"+getCoordinateFB().substring(getCoordinateFB().length()-2);
            }
            else coordinate += this.coordinate.getValue();
            this.coordinateSplit = coordinate.split(" ");
        }
        if (this.coordinate == null) System.out.println(coordinate + " does not match regex");
        this.set = set;
        this.counts = counts;
    }

    String getCoordinateLR() {
        return coordinate.getKey();
    }

    String getCoordinateFB() {
        return coordinate.getValue();
    }

    int side() {
        return Integer.valueOf(coordinateSplit[0].substring(1));
    }

    double ltr() {
        return Double.valueOf(coordinateSplit[1]);
    }

    String inOut() {
        return coordinateSplit[2];
    }

    int ltrRel() {
        return Integer.valueOf(coordinateSplit[3]);
    }

    double ftb() {
        return Double.valueOf(coordinateSplit[4]);
    }

    String ftbRel() {
        return coordinateSplit[5].substring(0, 1);
    }

    String getSet() {
        return set;
    }

    String getCounts() {
        return counts;
    }
}
