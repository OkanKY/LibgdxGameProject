

package com.me.brainjumper;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.brainjumper.object.Bob;
import com.me.brainjumper.object.Coin;
import com.me.brainjumper.object.Meteor;
import com.me.brainjumper.object.Platform;
import com.me.brainjumper.object.Spring;
import com.me.brainjumper.object.Star;
import com.me.brainjumper.set.Animation;
import com.me.brainjumper.set.Assets;
import com.me.brainjumper.set.LevelGenerator;

public class WorldRenderer {
	static final float FRUSTUM_WIDTH = 10;
	static final float FRUSTUM_HEIGHT = 15;
	World world;
	OrthographicCamera cam;
	SpriteBatch batch;
	TextureRegion background;
	
	public WorldRenderer (SpriteBatch batch, World world) {
		this.world = world;
		this.cam = new OrthographicCamera(FRUSTUM_WIDTH, FRUSTUM_HEIGHT);
		this.cam.position.set(FRUSTUM_WIDTH / 2, FRUSTUM_HEIGHT / 2, 0);
		this.batch = batch;
		
		
	}

	public void render () {
		if (world.bob.position.y > cam.position.y) cam.position.y = world.bob.position.y;
		cam.update();
		batch.setProjectionMatrix(cam.combined);
		renderBackground();
		renderObjects();
	}

	public void renderBackground () {
		batch.disableBlending();
		batch.begin();
		batch.draw(Assets.backgroundRegion, cam.position.x - FRUSTUM_WIDTH / 2, cam.position.y - FRUSTUM_HEIGHT / 2, FRUSTUM_WIDTH,
			FRUSTUM_HEIGHT);
		
		batch.end();
	}

	
	public void renderObjects () {
		batch.enableBlending();
		batch.begin();
		renderBob();
		renderPlatforms();
		renderItems();
		renderSquirrels();
		renderCastle();
		batch.end();
	}

	private void renderBob () {
		TextureRegion keyFrame;
		switch (world.bob.state) {
		case Bob.BOB_STATE_FALL:
			keyFrame = Assets.bobFall.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			break;
		case Bob.BOB_STATE_JUMP:
			//keyFrame = Assets.bobJump.getKeyFrame(world.bob.stateTime, Animation.ANIMATION_LOOPING);
			keyFrame = Assets.bobJump;
			break;
		case Bob.BOB_STATE_HIT:
		default:
			keyFrame = Assets.bobHit;
		}

	/*	float side = world.bob.velocity.x < 0 ? -1 : 1;
		if (side < 0)
			batch.draw(keyFrame, world.bob.position.x + 0.5f, world.bob.position.y - 0.5f, side * 3, 3);
		else
			batch.draw(keyFrame, world.bob.position.x - 0.5f, world.bob.position.y - 0.5f, side * 3, 3);
	*/
		batch.draw(keyFrame, world.bob.position.x -Bob.BOB_WIDTH/2, world.bob.position.y - Bob.BOB_HEIGHT/2,Bob.BOB_WIDTH,Bob.BOB_HEIGHT);
	}

	private void renderPlatforms () {
		int len = world.platforms.size();
		for (int i = 0; i < len; i++) {
			Platform platform = world.platforms.get(i);
			TextureRegion keyFrame = Assets.platform;
			if (platform.state == Platform.PLATFORM_STATE_PULVERIZING) {
				keyFrame = Assets.brakingPlatform.getKeyFrame(platform.stateTime, Animation.ANIMATION_NONLOOPING);
			}

			batch.draw(keyFrame, platform.position.x - (Platform.PLATFORM_WIDTH+LevelGenerator.PLATFORM_WIDTH)/2, platform.position.y - (Platform.PLATFORM_HEIGHT+LevelGenerator.PLATFORM_HEIGHT)/2, (Platform.PLATFORM_WIDTH+LevelGenerator.PLATFORM_WIDTH), (Platform.PLATFORM_HEIGHT+LevelGenerator.PLATFORM_HEIGHT));
		}
	}

	private void renderItems () {
		int len = world.springs.size();
		for (int i = 0; i < len; i++) {
			Spring spring = world.springs.get(i);
			batch.draw(Assets.spring, spring.position.x - Spring.SPRING_WIDTH/2, spring.position.y - Spring.SPRING_HEIGHT/2, Spring.SPRING_WIDTH, Spring.SPRING_HEIGHT);
		
		}

		len = world.coins.size();
		for (int i = 0; i < len; i++) {
			Coin coin = world.coins.get(i);
			TextureRegion keyFrame = Assets.coinAnim.getKeyFrame(coin.stateTime, Animation.ANIMATION_LOOPING);
			batch.draw(keyFrame, coin.position.x - Coin.COIN_WIDTH/2, coin.position.y -Coin.COIN_HEIGHT/2, Coin.COIN_WIDTH, Coin.COIN_HEIGHT);
			
		}
	}

	private void renderSquirrels () {
		
		int len = world.meteors.size();
		for (int i = 0; i < len; i++) {
			Meteor squirrel = world.meteors.get(i);
			//TextureRegion keyFrame = Assets.squirrelFly.getKeyFrame(squirrel.stateTime, Animation.ANIMATION_LOOPING);
			TextureRegion keyFrame = Assets.squirrelFly;

			float side = squirrel.velocity.x < 0 ? -1 : 1;
			if (side < 0)
				batch.draw(keyFrame, squirrel.position.x + Meteor.SQUIRREL_WIDTH/2, squirrel.position.y - Meteor.SQUIRREL_HEIGHT/2, side * Meteor.SQUIRREL_WIDTH, Meteor.SQUIRREL_HEIGHT);
			else
				batch.draw(keyFrame, squirrel.position.x - Meteor.SQUIRREL_WIDTH, squirrel.position.y - Meteor.SQUIRREL_HEIGHT, side * Meteor.SQUIRREL_WIDTH, Meteor.SQUIRREL_HEIGHT);
		}
	}

	private void renderCastle () {
		Star castle = world.star;
		batch.draw(Assets.castle, castle.position.x - Star.CASTLE_WIDTH/2, castle.position.y - Star.CASTLE_HEIGHT/2, Star.CASTLE_WIDTH, Star.CASTLE_HEIGHT);
	}
}
