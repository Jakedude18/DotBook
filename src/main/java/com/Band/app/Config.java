package com.Band.app;


import javafx.util.Pair;

 class Config {

    private Pair<Double,Double> set, counts, coordinate, bottomLeftCorner, leftByRight;
    final int squaresNum,fontSize;
    final double interval,padding,dotRadius;

     Config(double setX, double setY, double countsX, double countsY, double coordinatesX, double coordinatesY, double bottomLeftCornerX,
            double bottomLeftCornerY, double leftByRightX, double leftByRightY, double interval, double padding, int fontSize){
        set = new Pair<Double, Double>(setX,setY);
        counts = new Pair<Double, Double>(countsX, countsY);
        coordinate = new Pair<Double, Double>(coordinatesX, coordinatesY);
        bottomLeftCorner = new Pair<Double, Double>(bottomLeftCornerX, bottomLeftCornerY);
        leftByRight = new Pair<Double, Double>(leftByRightX,leftByRightY);
        this.interval = interval;
        this.padding = padding;
        squaresNum = (int)(leftByRightX/interval/8);
        this.dotRadius = interval / 2;
        this.fontSize = fontSize;
    }
     Pair<Double, Double> getSet(){
        return set;
    }

     Pair<Double, Double> getCoordinate() {
        return coordinate;
    }

     Pair<Double, Double> getBottomLeftCorner() {
        return bottomLeftCorner;
    }

     Pair<Double, Double> getLeftByRight() {
        return leftByRight;
    }

     Pair<Double, Double> getCounts(){
        return counts;
    }

 }
