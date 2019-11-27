package com.Band.app;

import org.junit.Test;
import org.junit.Before;

public class AppTest 
{
    private Config config;
    @Before
    public void setDotPageConfig(){
        config = new Config(54,274.5,54,225,54,180,96,130.5,282,166.5,7,20,20);
    }
    @Test
    public void testConfig(){
        PostScriptGenerator postScriptGenerator = new PostScriptGenerator(config);
        System.out.println(postScriptGenerator.showPage());
    }
}
