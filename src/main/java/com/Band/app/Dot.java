package com.Band.app;

import javafx.util.Pair;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Dot {
    private String[] coordinateSplit;
    private Pair<String,String> coordinate;
    private int set,counts;

    Dot(String coordinate,int set,int counts){
        if(Pattern.matches("^((s[1,2] ([1-4] (in|out))|on)|on 50) ([1-4]?5|([1-5]0)) ([1-9]|1[0-4]) ([bf](bh|fh)|(bfs|fbs))$",coordinate)){
            final int shortCoordinateMaxLength = 12;
            boolean shortCoordinate = coordinate.length() < 12;
            int splittingPoint = shortCoordinate?  2: 4;
            Pattern pattern = Pattern.compile(" ");
            coordinate.toLowerCase();
            Matcher m =  pattern.matcher(coordinate);
            for(int i = 0;i < splittingPoint; i++) m.find();
            this.coordinate = new Pair<String,String>(coordinate.substring(0,m.start()),coordinate.substring(m.start() + 1));
            if(shortCoordinate){
                coordinate = "s1 0 in 50 " + this.coordinate.getValue();
            }
            this.coordinateSplit = coordinate.split(" ");
        }
        this.set = set;
        this.counts = counts;
    }

    Pair<String,String> getCoordinate(){
        return coordinate;
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
