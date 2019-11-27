package com.Band.app;

class PostScriptGenerator {

    private StringBuilder postScript;
    private Config config;

    PostScriptGenerator (Config config){
        this.config = config;
        this.postScript = new StringBuilder("%!\n");
        changeFont(config.fontSize);
    }

    private void changeFont(int fontSize){
        postScript.append("/Times-Roman findfont\n");
        postScript.append(fontSize);
        postScript.append(" scalefont\nsetfont\nnewpath\n");
    }

    void addDot(Dot dot){

        final int dotsPer5Yards = 8;
        final int hashesPer10Yards = 5;
        final double yardsPerHash = 1.6;
        //adding set
        postScript.append(String.format("%.1f %.1f moveto%n", config.getSet().getKey(), config.getSet().getValue()));
        postScript.append(String.format("(%d) show%n",dot.getSet()));
        //adding counts
        postScript.append(String.format("%.1f %.1f moveto%n", config.getCounts().getKey(), config.getCounts().getValue()));
        postScript.append(String.format("(%d) show%n",dot.getCounts()));
        //adding coordinate TODO weird thing with coordinate being null
        postScript.append(String.format("%.1f %.1f moveto%n", config.getCoordinate().getKey(), config.getCoordinate().getValue()));
        postScript.append(String.format("(%s) show%n",dot.getCoordinate().getKey()));
        postScript.append(String.format("%.1f %.1f moveto%n", config.getCoordinate().getKey(), config.getCoordinate().getValue()- config.padding));
        postScript.append(String.format("(%s) show%n",dot.getCoordinate().getValue()));
        //adding the grid
        //stroke line
        double lineHeight = config.getBottomLeftCorner().getValue() + (dot.ftbRel().equals("f")?
                config.interval*dotsPer5Yards/2: config.getLeftByRight().getValue());
        postScript.append(String.format("%.1f %.1f moveto%n", config.getBottomLeftCorner().getKey(), lineHeight));
        postScript.append(String.format("%.1f %.1f lineto%n", config.getBottomLeftCorner().getKey() + config.getLeftByRight().getKey(), lineHeight));
        //adding hashes
        double startingX = config.getBottomLeftCorner().getKey() + config.getLeftByRight().getKey()/2 - dotsPer5Yards/2 * config.interval;
        for(int i = 0; i < hashesPer10Yards; i++){
            double hashX = startingX + i*yardsPerHash* config.interval;
            postScript.append(String.format("%.1f %.1f moveto%n",hashX, lineHeight + config.interval));
            postScript.append(String.format("%.1f %.1f lineto%n",hashX, lineHeight - config.interval));
        }
        startingX += dotsPer5Yards/2 * config.interval;
        //adding dot
        double centerX = startingX  + config.interval * dot.ltr() * (dot.inOut().equals("in")? 1:-1);
        double centerY = lineHeight + config.interval * dot.ftb() * (dot.ftbRel().equals("f")? 1: -1);
        postScript.append(String.format("%.1f %.1f moveto%n",centerX, centerY + config.dotRadius));
        postScript.append(String.format("%.1f %.1f lineto%n",centerX, centerY - config.dotRadius));
        postScript.append(String.format("%.1f %.1f moveto%n",centerX + config.dotRadius, centerY));
        postScript.append(String.format("%.1f %.1f lineto%n",centerX -  config.dotRadius, centerY));
        //adding end syntax
        postScript.append("2 setlinewidth\n");
        postScript.append("stroke\n");
        //adding numbers
        changeFont((int)(config.fontSize/1.5));
        for(int i = 0; i < config.squaresNum; i++){
            postScript.append(String.format("%.1f %.1f moveto%n", config.getBottomLeftCorner().getKey()  + config.interval * (dotsPer5Yards/2+(i * dotsPer5Yards)),
                    config.getBottomLeftCorner().getValue() - config.interval + dotsPer5Yards/4 * config.interval));
            int num = dot.ltrRel() - 5 * (config.squaresNum/2) + 5 * i;
            if (num > 50) num = 50 - (num - 50);
            postScript.append(String.format("(%d) show%n",num));
        }

        //adding showpage
        postScript.append("showpage");
    }

    String showPage(){
        return postScript.toString();
    }
}
