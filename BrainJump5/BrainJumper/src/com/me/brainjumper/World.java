

package com.me.brainjumper;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.badlogic.gdx.math.Vector2;
import com.me.brainjumper.object.Bob;
import com.me.brainjumper.object.Coin;
import com.me.brainjumper.object.Meteor;
import com.me.brainjumper.object.Platform;
import com.me.brainjumper.object.Spring;
import com.me.brainjumper.object.Star;
import com.me.brainjumper.set.Calculator;
import com.me.brainjumper.set.LevelGenerator;
import com.me.brainjumper.set.OverlapTester;

public class World {
	public interface WorldListener {
		public void jump ();

		public void highJump ();

		public void hit ();

		public void coin ();
	}

	public static final float WORLD_WIDTH = 10;
	public static final float WORLD_HEIGHT = 15 * 20;
	public static final int WORLD_STATE_RUNNING = 0;
	public static final int WORLD_STATE_NEXT_LEVEL = 1;
	public static final int WORLD_STATE_GAME_OVER = 2;
	public static final Vector2 gravity = new Vector2(0, -12);
	public final Bob bob;
	public final List<Platform> platforms;
	public final List<Spring> springs;
	public final List<Meteor> meteors;
	public final List<Coin> coins;
	public Star star;
	public final WorldListener listener;
	public final Random rand;
	
	public float heightSoFar;
	
   // public float collisionX;
    //public float collisionY;
	public int score;
	public int state;
   
	public World (WorldListener listener) {
		this.bob = new Bob(5, 1);
		this.platforms = new ArrayList<Platform>();
		this.springs = new ArrayList<Spring>();
		this.meteors = new ArrayList<Meteor>();
		this.coins = new ArrayList<Coin>();
		this.listener = listener;
	
		rand = new Random();
		generateLevel();
		this.heightSoFar = 0;
		this.score = 0;
		this.state = WORLD_STATE_RUNNING;
		//collisionX=0;
		//collisionY=0;
	}

	private void generateLevel () {
		float y = (Platform.PLATFORM_HEIGHT +LevelGenerator.PLATFORM_HEIGHT)/ 2;
		float maxJumpHeight = Bob.BOB_JUMP_VELOCITY * Bob.BOB_JUMP_VELOCITY / (2 * -gravity.y);
		while (y < (WORLD_HEIGHT+LevelGenerator.WORLD_HEIGHT) - WORLD_WIDTH / 2) {
			int type = rand.nextFloat() > LevelGenerator.platformValues ? Platform.PLATFORM_TYPE_MOVING : Platform.PLATFORM_TYPE_STATIC;
			float x = rand.nextFloat() * (WORLD_WIDTH - (Platform.PLATFORM_WIDTH+LevelGenerator.PLATFORM_WIDTH)) + (Platform.PLATFORM_WIDTH+LevelGenerator.PLATFORM_WIDTH) / 2;

			Platform platform = new Platform(type, x, y);
			platforms.add(platform);
 
			if (rand.nextFloat() > 0.9f && type != Platform.PLATFORM_TYPE_MOVING) {
				Spring spring = new Spring(platform.position.x, platform.position.y + (Platform.PLATFORM_HEIGHT+LevelGenerator.PLATFORM_HEIGHT) / 2
					+ Spring.SPRING_HEIGHT / 2);
				springs.add(spring);
			}

			if (y > (WORLD_HEIGHT+LevelGenerator.WORLD_HEIGHT) / (1+2*LevelGenerator.levelCount) && rand.nextFloat() > LevelGenerator.squirrelFrequency) {
				Meteor squirrel = new Meteor(platform.position.x + rand.nextFloat(), platform.position.y
					+ Meteor.SQUIRREL_HEIGHT + rand.nextFloat() * 2);
				meteors.add(squirrel);
			}

			if (rand.nextFloat() > 0.6f) {
				Coin coin = new Coin(platform.position.x + rand.nextFloat(), platform.position.y + Coin.COIN_HEIGHT
					+ rand.nextFloat() * 3);
				coins.add(coin);
			}

			
			y += (maxJumpHeight + LevelGenerator.platformFrequency);
			y -= rand.nextFloat() * (maxJumpHeight / 3);
		}

		star = new Star(WORLD_WIDTH / 2, y);
	}

	public void update (float deltaTime, float accelX) {
		updateBob(deltaTime, accelX);
		updatePlatforms(deltaTime);
		updateSquirrels(deltaTime);
		updateCoins(deltaTime);
		if (bob.state != Bob.BOB_STATE_HIT) checkCollisions();
		checkGameOver();
	}

	private void updateBob (float deltaTime, float accelX) {
		if (bob.state != Bob.BOB_STATE_HIT && bob.position.y <= 0.5f) bob.hitPlatform();
		if (bob.state != Bob.BOB_STATE_HIT) bob.velocity.x = -accelX / 10 * Bob.BOB_MOVE_VELOCITY;
		bob.update(deltaTime);
		heightSoFar = Math.max(bob.position.y, heightSoFar);
	}

	private void updatePlatforms (float deltaTime) {
		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			platform.update(deltaTime);
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
				platforms.remove(platform);
				len = platforms.size();
			}
		}
	}

	private void updateSquirrels (float deltaTime) {
		int len = meteors.size();
		for (int i = 0; i < len; i++) {
			Meteor squirrel = meteors.get(i);
			squirrel.update(deltaTime);
		}
	}

	private void updateCoins (float deltaTime) {
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			coin.update(deltaTime);
		}
	}

	private void checkCollisions () {
		checkPlatformCollisions();
		checkSquirrelCollisions();
		checkItemCollisions();
		checkCastleCollisions();
	}

	private void checkPlatformCollisions () {
		if (bob.velocity.y > 0) return;

		int len = platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = platforms.get(i);
			if (bob.position.y > platform.position.y) {
				if (OverlapTester.overlapRectangles(bob.bounds, platform.bounds)) {
					bob.hitPlatform();
					listener.jump();
					if (rand.nextFloat() > 0.5f) {
						platform.pulverize();
					}
					break;
				}
			}
		}
	}

	private void checkSquirrelCollisions () {
		int len = meteors.size();
		for (int i = 0; i < len; i++) {
			Meteor squirrel = meteors.get(i);
		if (OverlapTester.overlapRectangles(squirrel.bounds, bob.bounds)) {
			meteors.remove(squirrel);
			len=meteors.size();
			//other users
			//if(Bob.BOB_HEIGHT+Squirrel.SQUIRREL_HEIGHT+collisionY<bob.bounds.y/*&&Bob.BOB_WIDTH+Squirrel.SQUIRREL_WIDTH+collisionX<bob.bounds.x*/)
			//{
				 if(Calculator.trueCount>=3)
			   {
				Calculator.trueCount-=3;
		       }
			    else
			   { 
				bob.hitSquirrel();
				listener.hit();
			   }
				// collisionX=bob.bounds.x;
				  // collisionY=bob.bounds.y;
			//}
			
		}
		
		}
	}

	private void checkItemCollisions () {
		int len = coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = coins.get(i);
			if (OverlapTester.overlapRectangles(bob.bounds, coin.bounds)) {
				coins.remove(coin);
				len = coins.size();
				listener.coin();
				score += Coin.COIN_SCORE;
			}

		}

		if (bob.velocity.y > 0) return;

		len = springs.size();
		for (int i = 0; i < len; i++) {
			Spring spring = springs.get(i);
			if (bob.position.y > spring.position.y) {
				if (OverlapTester.overlapRectangles(bob.bounds, spring.bounds)) {
					bob.hitSpring();
					listener.highJump();
				}
			}
		}
	}

	private void checkCastleCollisions () {
		if (OverlapTester.overlapRectangles(star.bounds, bob.bounds)) {
			state = WORLD_STATE_NEXT_LEVEL;
			
		}
	}

	private void checkGameOver () {
		if (heightSoFar - 7.5f > bob.position.y) {
			state = WORLD_STATE_GAME_OVER;
		}
	}



}
