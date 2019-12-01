package com.Band.app;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws Exception
    {
        PostScriptGenerator postScriptGenerator = new PostScriptGenerator();
        Dot dot = new Dot("on 50 8 bfs",1,3);
        postScriptGenerator.addDot(dot);
        System.out.println(postScriptGenerator.showPage());
    }
}
