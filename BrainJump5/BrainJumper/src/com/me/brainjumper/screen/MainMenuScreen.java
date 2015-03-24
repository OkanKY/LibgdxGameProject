

package com.me.brainjumper.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.brainjumper.set.Assets;
import com.me.brainjumper.set.OverlapTester;
import com.me.brainjumper.set.Settings;

public class MainMenuScreen implements Screen {
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle soundBounds;
	Rectangle playBounds;
	Rectangle highscoresBounds;
	Rectangle helpBounds;
	Rectangle ExitBounds;
	Vector3 touchPoint;

	public MainMenuScreen (Game game) {
		this.game = game;
		Assets.setBackGround(1);
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		batcher = new SpriteBatch();
		soundBounds = new Rectangle(0, 0, 64, 64);
		playBounds = new Rectangle(160 - 150, 200 + 36, 300, 30);
		highscoresBounds = new Rectangle(160 - 150, 200+3, 300, 30);
		helpBounds = new Rectangle(160 - 150, 200  - 36, 300, 30);
		ExitBounds = new Rectangle(160 - 150, 200  - 72, 300, 30);
		touchPoint = new Vector3();
	}

	public MainMenuScreen() {
		// TODO Auto-generated constructor stub
	}

	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(playBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new GameScreen(game));
				return;
			}
			if (OverlapTester.pointInRectangle(highscoresBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new HighscoresScreen(game));		
				
				return;
			}
			if (OverlapTester.pointInRectangle(helpBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new HelpScreen(game));
				return;
			}
			if (OverlapTester.pointInRectangle(ExitBounds, touchPoint.x, touchPoint.y)) {
				Gdx.app.exit();
				return;
			}
			if (OverlapTester.pointInRectangle(soundBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				Settings.soundEnabled = !Settings.soundEnabled;
				if (Settings.soundEnabled)
					Assets.music.play();
				else
					Assets.music.pause();
			}
		}
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClearColor(1, 0, 0, 1);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);

		batcher.disableBlending();
		batcher.begin();
		batcher.draw(Assets.backgroundRegion, 0, 0, 320, 480);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();
		batcher.draw(Assets.logo, 160 - 274 / 2, 480 - 10 - 142, 274, 142);
		batcher.draw(Assets.mainMenu, 10, 200 - 150 / 2, 300, 150);
		batcher.draw(Settings.soundEnabled ? Assets.soundOn : Assets.soundOff, 0, 0, 64, 64);
		batcher.end();
	}

	
	public void render (float delta) {
		update(delta);
		draw(delta);
	}


	public void resize (int width, int height) {
	}

	
	public void show () {
	}

	public void hide () {
	}

	
	public void pause () {
		Settings.save();
	}

	public void resume () {
	}


	public void dispose () {
	}
}
