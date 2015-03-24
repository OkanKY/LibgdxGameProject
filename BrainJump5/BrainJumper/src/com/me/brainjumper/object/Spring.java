

package com.me.brainjumper.object;


public class Spring extends GameObject {
	public static float SPRING_WIDTH = 0.5f;
	public static float SPRING_HEIGHT = 0.5f;

	public Spring (float x, float y) {
		super(x, y, SPRING_WIDTH, SPRING_HEIGHT);
	}
}
