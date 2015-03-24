

package com.me.brainjumper.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GLCommon;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.me.brainjumper.set.Assets;
import com.me.brainjumper.set.OverlapTester;

public class HelpScreen implements Screen {
	Game game;

	OrthographicCamera guiCam;
	SpriteBatch batcher;
	Rectangle nextBounds;
	Vector3 touchPoint;
	Texture helpImage;
	TextureRegion helpRegion;

	public HelpScreen (Game game) {
		this.game = game;

		guiCam = new OrthographicCamera(320, 480);
		guiCam.position.set(320 / 2, 480 / 2, 0);
		nextBounds = new Rectangle(320 - 64, 0, 64, 64);
		touchPoint = new Vector3();
		batcher = new SpriteBatch();
		helpImage = Assets.loadTexture("data/help1.png");
		helpRegion = new TextureRegion(helpImage, 0, 0, 320, 480);
	}

	public void update (float deltaTime) {
		if (Gdx.input.justTouched()) {
			guiCam.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

			if (OverlapTester.pointInRectangle(nextBounds, touchPoint.x, touchPoint.y)) {
				Assets.playSound(Assets.clickSound);
				game.setScreen(new HelpScreen2(game));
				return;
			}
		}
	}

	public void draw (float deltaTime) {
		GLCommon gl = Gdx.gl;
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		guiCam.update();

		batcher.setProjectionMatrix(guiCam.combined);
		batcher.disableBlending();
		batcher.begin();
		batcher.draw(helpRegion, 0, 0, 320, 480);
		batcher.end();

		batcher.enableBlending();
		batcher.begin();
		batcher.draw(Assets.arrow, 320, 0, -64, 64);
		batcher.end();

		gl.glDisable(GL10.GL_BLEND);
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
		helpImage.dispose();
	}

	public void resume () {
	}

	public void dispose () {
	}
}
