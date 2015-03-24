package com.me.brainjumper.screen;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.me.brainjumper.set.Assets;
import com.me.brainjumper.set.SpriteAccessor;
public class Splash implements Screen {
	Game game;
	private SpriteBatch batch;
	private Sprite splash;
	private TweenManager tweenManager;
	OrthographicCamera guiCam;
	Texture Image;
	public Splash (Game game)
	{
		this.game = game;
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 /2, 480/2 , 0);
		
	}
	public void render(float delta) {
		// TODO Auto-generated method stub
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batch.setProjectionMatrix(guiCam.combined);
		batch.begin();
		splash.draw(batch);
		batch.end();
		tweenManager.update(delta);
		
	}

	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	public void show() {
		// TODO Auto-generated method stub
		// apply preferences
		Gdx.graphics.setVSync(true);
		
		batch = new SpriteBatch();
		tweenManager = new TweenManager();
		Tween.registerAccessor(Sprite.class, new SpriteAccessor());
		Image = Assets.loadTexture("data/BrainJumper3.png");
		splash = new Sprite(new TextureRegion(Image, 0, 0, 320, 480));
  
		Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
		Tween.to(splash, SpriteAccessor.ALPHA, 1.5f).target(1).repeatYoyo(1, .5f).setCallback(new TweenCallback() {

			public void onEvent(int type, BaseTween<?> source) {
				game.setScreen(new MainMenuScreen(game));
				
			}
		}).start(tweenManager);
        
		tweenManager.update(Float.MIN_VALUE); // update once avoid short flash of splash before animation
	 
	}

	public void hide() {
		// TODO Auto-generated method stub
		dispose();
	}

	public void pause() {
		// TODO Auto-generated method stub
		
	}

	public void resume() {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		batch.dispose();
		splash.getTexture().dispose();
	}

}
