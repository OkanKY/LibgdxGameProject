

package com.me.brainjumper.screen;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.brainjumper.World;
import com.me.brainjumper.World.WorldListener;
import com.me.brainjumper.WorldRenderer;
import com.me.brainjumper.set.Assets;
import com.me.brainjumper.set.Calculator;
import com.me.brainjumper.set.LevelGenerator;
import com.me.brainjumper.set.OverlapTester;
import com.me.brainjumper.set.Settings;

public class GameScreen implements Screen {
	static final int GAME_READY = 0;
	static final int GAME_RUNNING = 1;
	static final int GAME_PAUSED = 2;
	static final int GAME_LEVEL_END = 3;
	static final int GAME_OVER = 4;
	 LevelGenerator levelGenerator;
	Game game;
	private int max,min;
	int state;
	OrthographicCamera guiCam;
	Vector3 touchPoint;
	SpriteBatch batcher;
	World world;
	WorldListener worldListener;
	WorldRenderer renderer;
	Rectangle pauseBounds;
	Rectangle resumeBounds;
	Rectangle quitBounds;
	int lastScore;
	String scoreString;
	 Calculator calculator;

	public GameScreen (Game game) {
		this.game = game;
	    this.levelGenerator=new LevelGenerator();
		setMaxMin(5,3);
		state = GAME_READY;
		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
	
		worldListener = new WorldListener() {
			public void jump () {
				Assets.playSound(Assets.jumpSound);
			}

			public void highJump() {
				Assets.playSound(Assets.highJumpSound);
			}

			public void hit() {
				Assets.playSound(Assets.hitSound);
			}

			public void coin () {
				Assets.playSound(Assets.coinSound);
			}
		};
		world = new World(worldListener);
		renderer = new WorldRenderer(batcher, world);
		pauseBounds = new Rectangle(320 - 64, 480 - 64, 64, 64);
		resumeBounds = new Rectangle(160 - 96, 240, 192, 36);
		quitBounds = new Rectangle(160 - 96, 240 - 36, 192, 36);
		calculator= new Calculator(getMax(),getMin(),batcher,world,guiCam);
		lastScore = 0;
		scoreString = "SCORE: 0";
	}

	public void update (float deltaTime) {
		if (deltaTime > 0.1f) deltaTime = 0.1f;

		switch (state) {
		case GAME_READY:
			updateReady();
			break;
		case GAME_RUNNING:
			updateRunning(deltaTime);
			break;
		case GAME_PAUSED:
			updatePaused();
			break;
		case GAME_LEVEL_END:
			updateLevelEnd();
			break;
		case GAME_OVER:
			updateGameOver();
			break;
		}
	}

	private void updateReady () {
		if (Gdx.input.justTouched()) {
			state = GAME_RUNNING;
		}
	}

	private void updateRunning (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(pauseBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				state = GAME_PAUSED;
				return;
			}
		}
		
		ApplicationType appType = Gdx.app.getType();
		
		// should work also with Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)
		if (appType == ApplicationType.Android || appType == ApplicationType.iOS) {
			world.update(deltaTime, Gdx.input.getAccelerometerX());
		} else {
			float accel = 0;
			if (Gdx.input.isKeyPressed(Keys.DPAD_LEFT)) accel = 5f;
			if (Gdx.input.isKeyPressed(Keys.DPAD_RIGHT)) accel = -5f;
			world.update(deltaTime, accel);
		}
		if (world.score != lastScore) {
			lastScore = world.score;
			scoreString = "SCORE: " + lastScore;
		}
		if (world.state == World.WORLD_STATE_NEXT_LEVEL) {
			state = GAME_LEVEL_END;
		}
		if (world.state == World.WORLD_STATE_GAME_OVER) {
			state = GAME_OVER;

			if (lastScore >= Settings.highscores[4])
				scoreString = "NEW HIGHSCORE: " + lastScore;
			else
				scoreString = "SCORE: " + lastScore;
			Settings.addScore(lastScore);
			Settings.save();
		}
	}

	private void updatePaused () {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(resumeBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				state = GAME_RUNNING;
				return;
			}

			if (OverlapTester.pointInRectangle(quitBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new MainMenuScreen(game));
				return;
			}
		}
	}

	private void updateLevelEnd () {
		if (Gdx.input.justTouched()) {
			if(LevelGenerator.levelCount<4)
			{
			levelGenerator.nextLevelUpdate();
			calculator.setMaxMin(getMax()*LevelGenerator.levelCount, getMin()*LevelGenerator.levelCount);
			calculator.generate();
			world = new World(worldListener);
			renderer = new WorldRenderer(batcher, world);
			world.score = lastScore+LevelGenerator.GameScore;
			state = GAME_READY;
			}
			else
				game.setScreen(new MainMenuScreen(game));	
		}
	}

	private void updateGameOver () {
		if (Gdx.input.justTouched()) {
			game.setScreen(new MainMenuScreen(game));
		}
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		renderer.render();

		guiCam.update();
		batcher.setProjectionMatrix(guiCam.combined);
		batcher.enableBlending();
		batcher.begin();
		switch (state) {
		case GAME_READY:
			presentReady();
			break;
		case GAME_RUNNING:
			presentRunning(deltaTime);
			break;
		case GAME_PAUSED:
			presentPaused();
			break;
		case GAME_LEVEL_END:
			presentLevelEnd();
			break;
		case GAME_OVER:
			presentGameOver();
			break;
		}
		batcher.end();
	}

	private void presentReady () {
		batcher.draw(Assets.ready, 160 - 192 / 2, 240 - 32 / 2, 192, 32);
	}

	private void presentRunning (float deltaTime) {
		batcher.draw(Assets.pause, 320 - 64, 480 - 64, 64, 64);
		Assets.font.draw(batcher, scoreString, 65, 480 - 5);
		calculator.update(deltaTime);
	}

	private void presentPaused () {
		batcher.draw(Assets.pauseMenu, 160 - 192 / 2, 240 - 96 / 2, 192, 96);
		Assets.font.draw(batcher, scoreString, 16, 480 - 20);
	}

	private void presentLevelEnd () {
		if(LevelGenerator.levelCount<4)
		{
		String topText = "the level finish ...";
		String bottomText = "in another level!";
		float topWidth = Assets.font.getBounds(topText).width;
		float bottomWidth = Assets.font.getBounds(bottomText).width;
		Assets.font.draw(batcher, topText, 160 - topWidth / 2, 480 - 40);
		Assets.font.draw(batcher, bottomText, 160 - bottomWidth / 2, 40);
		}
		else
		{
			String Text = "Successful game ";
			float Width = Assets.font.getBounds(Text).width;
			Assets.font.draw(batcher, Text, 160 - Width / 2, 480 - 40);
		}
	}

	private void presentGameOver () {
		batcher.draw(Assets.gameOver, 160 - 160 / 2, 240 - 96 / 2, 160, 96);
		float scoreWidth = Assets.font.getBounds(scoreString).width;
		Assets.font.draw(batcher, scoreString, 160 - scoreWidth / 2, 480 - 20);
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
		if (state == GAME_RUNNING) state = GAME_PAUSED;
	}

	public void resume () {
	}

	public void dispose () {
	}

	public int getMax() {
		return max;
	}

	public void setMaxMin(int max, int min) {
		this.max = max;
		this.min = min;
	}

	public int getMin() {
		return min;
	}


}
