package com.me.brainjumper.set;

import java.util.Random;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.brainjumper.World;
import com.me.brainjumper.object.Bob;

public class Calculator {
	Random rand = new Random();
	int x = rand.nextInt(3);
	private int max,min,numberA,numberB;
	private float result,chosen;
	private float timer=0;
	private int operator,current;
	private int clickState;
	private float draw_y;
	public static int trueCount;
	private boolean click;
	String op;
	Rectangle A_Bounds,B_Bounds,C_Bounds;
	Vector3 touchPoint;
	World world;
	Bob bob;
	OrthographicCamera guiCam;
	SpriteBatch batcher;
	float[] numberArray = new float[]{11.11f,11.11f,11.11f};
	int [] ChosenArray=new int[]{111,111,111};
	public Calculator (int max, int min,SpriteBatch batcher, World world,OrthographicCamera guiCam) 
	{
		this.max=max;
		this.min=min;
		A_Bounds = new Rectangle( 6, 376, 101, 49);
		B_Bounds = new Rectangle( 110 , 376, 101, 49);
		C_Bounds = new Rectangle( 214, 376, 101, 49);
		click=false;
		touchPoint = new Vector3();
		this.world = world;
		this.batcher = batcher;
		this.guiCam=guiCam;
		setClickState(0);
		trueCount=0;
		//draw_y=376;
		draw_y=480;
		generate ();
	}
   
	public int getMax() {
		return max;
	}

	public void setMaxMin(int max,int min) {
		this.max = max;
		this.min = min;
	}
	public int getMin() {
		return min;
	}
	public int getOperator() {
		return operator;
	}


	public void update(float deltaTime)
   {      
        timer+=deltaTime;
        System.out.println(timer);
		     if (getClickState()==0&&draw_y>376) 
		     {
		    	 draw_y-=0.5f;
		    	 draw();
		    	 click=true;
		     }
		     else if (getClickState()==0&&draw_y==376) 
		     {
		    	 draw();
		    	 click=true;
		     }
		
		     else if(getClickState()==1&&draw_y<=480)
		     {
		    	 draw_y+=0.5;
		    	 draw();
		    	 click=false;
		     }	 
		     else if(getClickState()==1&&draw_y>=480)
		     { 
		    	
		    	 generate ();
		     }
	}
    public void draw()
    {
   
    	   A_Bounds.set(6,  draw_y-20, 101, 49);
    	   B_Bounds.set(110,  draw_y-20, 101, 49);
    	   C_Bounds.set(214,  draw_y-20, 101, 49);
    	
    		if (Gdx.input.justTouched() && click) {
    			
    			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
    			if (OverlapTester.pointInRectangle(A_Bounds, touchPoint.x, touchPoint.y)) {
    				Assets.playSound(Assets.clickSound);
    				chosen=numberArray[ChosenArray[0]];
    				controller();
    				setClickState(1);
    				return;
    			}
    			if (OverlapTester.pointInRectangle(B_Bounds, touchPoint.x, touchPoint.y)) {
    				Assets.playSound(Assets.clickSound);
    				
    				chosen=numberArray[ChosenArray[1]];
    				controller();
    				setClickState(1);
    				return;
    			}
    			if (OverlapTester.pointInRectangle(C_Bounds, touchPoint.x, touchPoint.y)) {
    				Assets.playSound(Assets.clickSound);
    				chosen=numberArray[ChosenArray[2]];
    				
    				controller();
    				setClickState(1);
    				return;
    			}
    		
    		}
    		
    	batcher.draw(Assets.buttonRegion, 214, draw_y-20, 101, 49);
		batcher.draw(Assets.buttonRegion, 110 , draw_y-20, 101, 49);
		batcher.draw(Assets.buttonRegion,  6, draw_y-20, 101, 49);
		float a=numberArray[ChosenArray[2]];
		float b=numberArray[ChosenArray[1]];
		float c=numberArray[ChosenArray[0]];
		Assets.font.draw(batcher,""+trueCount,10,475);
		Assets.font.draw(batcher,""+c,20, draw_y+15);
		Assets.font.draw(batcher,""+b,130,draw_y+15);
		Assets.font.draw(batcher,""+a,235,draw_y+15);
		Assets.font.draw(batcher,"   "+numberA+"   "+op+"   "+numberB+" =  ?",20, draw_y+60);

	
    }
    public void generate ()
    {
    	  numberA = MathUtils.random(min, max);
		  numberB = MathUtils.random(min, max);
		     operator = rand.nextInt(4);
		     setClickState(0);
		     timer=0;
		    switch (operator) {
		    case 0:
		      result=numberA+numberB; 
		      op="+";
		      break;
		    case 1:
		    	result=numberA-numberB;
		    	op="-";
		      break;
		    case 2:
		    	result=(float)(numberA/numberB);
		    	op="/";
		      break;
		    case 3:
		    	result=numberA*numberB;
		    	op="*";
		      break;
		   
		    }
		    numberArray[0]=result;
		    for (int i = 1; i < numberArray.length; i++) 
		    { 
				int error = MathUtils.random(-4, 4) ;
				numberArray[i]=result+error;
				for (int a = 0; a < i; a++)
				{ 
					if(numberArray[a]==result+error)
					{
						i--;
						break;
					}
					else
					{
						numberArray[i]=result+error;
					}
					
				}
				
		    }
		    
		    current = rand.nextInt(3);
			 ChosenArray[0]= current;
		    for (int i = 1; i < ChosenArray.length; i++) 
		    {    
		    	current = rand.nextInt(3);
				 ChosenArray[i]= current;
				for (int a = 0; a < i; a++)
				{ 
					if(ChosenArray[a]== current)
					{
						i--;
						break;
					
					}
					else
					{
						ChosenArray[i]= current;
					}
				}
				
		    }
		  
    }
    public void controller()
    {
    	if(result==chosen)
        trueCount++;
    	
    	else
        trueCount--;
    }

	public int getClickState() {
		return clickState;
	}

	public void setClickState(int clickState) {
		this.clickState = clickState;
	}
}
