package com.me.brainjumper.set;

import com.me.brainjumper.screen.GameScreen;

public class LevelGenerator  {
	public static float platformFrequency; 
    public static float platformValues;
    public static float squirrelFrequency;
    public static int levelCount;
    public static int GameScore;
    public static float PLATFORM_WIDTH;
    public static float PLATFORM_HEIGHT;
    public static float PLATFORM_VELOCITY;
    public static float WORLD_HEIGHT;
    GameScreen gamescreen;
    int score;
	public LevelGenerator() {
		// TODO Auto-generated constructor stub
		levelCount=1;
	    platformFrequency=0.5f;
	    platformValues=0.8f;
	    squirrelFrequency=1.2f;
	   PLATFORM_WIDTH=0;
	    PLATFORM_HEIGHT=0;
	    PLATFORM_VELOCITY=0;
	    WORLD_HEIGHT=0;
	}
	public void nextLevelUpdate()
	{
		levelCount+=1;
		platformFrequency+=2f;
		squirrelFrequency-=0.3f;
		platformValues-=0.2f;
		
	 
	   switch (levelCount)
	   {
	   case 1 :
		   
		   Assets.setBackGround(1);
		   //GameScore=100;
		   break;
	   case 2:
		   WORLD_HEIGHT+=40;
		   PLATFORM_WIDTH-=0.5f;
		    PLATFORM_HEIGHT-=0.1f;
		    PLATFORM_VELOCITY+=1.0f;
		   Assets.setBackGround(2);
		   GameScore=500;
		   break;
	   case 3:
		   WORLD_HEIGHT+=40;
		   PLATFORM_WIDTH-=0.5f;
		    PLATFORM_HEIGHT-=0.1f;
		    PLATFORM_VELOCITY+=1.0f;
		   Assets.setBackGround(3);
		   GameScore=1000;
		   break;
	   }   
	   
		
	}
	
	
}
