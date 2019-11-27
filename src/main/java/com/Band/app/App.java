package com.Band.app;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        Config config = new Config(54,274.5,54,225,10,180,102,130.5,280,166.5,7,20,20);
        PostScriptGenerator postScriptGenerator = new PostScriptGenerator(config);
        Dot dot = new Dot("s1 3 out 50 2 fbh",1,3);
        postScriptGenerator.addDot(dot);
        System.out.println(postScriptGenerator.showPage());
    }
}
