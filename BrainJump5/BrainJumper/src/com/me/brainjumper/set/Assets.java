
package com.me.brainjumper.set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {
	public static Texture background;
	public static TextureRegion backgroundRegion;
	public static TextureRegion buttonRegion,longButtonRegion;
	public static Texture items,bobItem;
	public static TextureRegion mainMenu;
	public static TextureRegion pauseMenu;
	public static TextureRegion ready;
	public static TextureRegion gameOver;
	public static TextureRegion highScoresRegion;
	public static TextureRegion logo;
	public static TextureRegion soundOn;
	public static TextureRegion soundOff;
	public static TextureRegion arrow;
	public static TextureRegion pause;
	public static TextureRegion spring;
	public static TextureRegion castle,bobJump;
	public static Animation coinAnim;
//	public static Animation bobJump;
	public static Animation bobFall;
	public static TextureRegion bobHit,squirrelFly;
	//public static Animation squirrelFly;
	public static TextureRegion platform;
	public static Animation brakingPlatform;
	public static BitmapFont font;

	public static Music music;
	public static Sound jumpSound;
	public static Sound highJumpSound;
	public static Sound hitSound;
	public static Sound coinSound;
	public static Sound clickSound;

	public static Texture loadTexture (String file) {
		return new Texture(Gdx.files.internal(file));
	}

	public static void load () {
	//	background = loadTexture("data/background.png");
		//  background = loadTexture("data/BackGround2.png");
		
		items = loadTexture("data/items.png");
		bobItem = loadTexture("data/ac.png");
		buttonRegion = new TextureRegion(items,411, 463, 101, 49);
		longButtonRegion = new TextureRegion(items,283, 414, 229, 39);
		mainMenu = new TextureRegion(items, 0, 224, 300, 150);
		pauseMenu = new TextureRegion(items, 224, 128, 192, 96);
		ready = new TextureRegion(items, 320, 224, 192, 32);
		gameOver = new TextureRegion(items, 352, 256, 160, 96);
		highScoresRegion = new TextureRegion(Assets.items, 0, 257, 300, 110 / 3);
		logo = new TextureRegion(items, 0, 370, 274, 142);
		soundOff = new TextureRegion(items, 0, 0, 64, 64);
		soundOn = new TextureRegion(items, 64, 0, 64, 64);
		arrow = new TextureRegion(items, 0, 64, 64, 64);
		pause = new TextureRegion(items, 64, 64, 64, 64);

		spring = new TextureRegion(items, 300, 440, 101, 77);
		castle = new TextureRegion(items, 128, 64, 64, 64);
		coinAnim = new Animation(0.2f, new TextureRegion(items, 128, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32),
			new TextureRegion(items, 192, 32, 32, 32), new TextureRegion(items, 160, 32, 32, 32));
		//bobJump = new Animation(0.2f, new TextureRegion(bobItem, 0, 0, 250, 250), new TextureRegion(bobItem,250, 250, 250, 250));
		bobJump = new TextureRegion(bobItem,250, 250, 250, 250);

		bobFall = new Animation(0.2f, new TextureRegion(bobItem, 250, 250, 250, 250), new TextureRegion(bobItem, 250, 0, 250, 250));
		bobHit = new TextureRegion(bobItem, 250, 0, 250, 250);
		/*bobJump = new Animation(0.2f, new TextureRegion(items, 0, 128, 32, 32), new TextureRegion(items, 32, 128, 32, 32));
		bobFall = new Animation(0.2f, new TextureRegion(items, 64, 128, 32, 32), new TextureRegion(items, 96, 128, 32, 32));
		bobHit = new TextureRegion(items, 128, 128, 32, 32);
		*/
		//squirrelFly = new Animation(0.2f, new TextureRegion(items, 0, 160, 32, 32), new TextureRegion(items, 32, 160, 32, 32));
		squirrelFly =  new TextureRegion(items, 0, 160, 64, 45);

		platform = new TextureRegion(items, 64, 160, 64, 16);
		brakingPlatform = new Animation(0.2f, new TextureRegion(items, 64, 160, 64, 16), new TextureRegion(items, 64, 176, 64, 16),
			new TextureRegion(items, 64, 192, 64, 16), new TextureRegion(items, 64, 208, 64, 16));

		font = new BitmapFont(Gdx.files.internal("data/font.fnt"), Gdx.files.internal("data/font.png"), false);

		music = Gdx.audio.newMusic(Gdx.files.internal("data/music.mp3"));
		music.setLooping(true);
		music.setVolume(0.5f);
		if (Settings.soundEnabled) music.play();
		jumpSound = Gdx.audio.newSound(Gdx.files.internal("data/jump.wav"));
		highJumpSound = Gdx.audio.newSound(Gdx.files.internal("data/highjump.wav"));
		hitSound = Gdx.audio.newSound(Gdx.files.internal("data/hit.wav"));
		coinSound = Gdx.audio.newSound(Gdx.files.internal("data/coin.wav"));
		clickSound = Gdx.audio.newSound(Gdx.files.internal("data/click.wav"));
	}

	public static void playSound (Sound sound) {
		if (Settings.soundEnabled) sound.play(1);
	}

	public static void setBackGround(int i) {
		// TODO Auto-generated method stub
		  switch (i)
		   {
		   case 1 :
			   background = loadTexture("data/background.png");
			   break;
		   case 2:
			   background = loadTexture("data/BackGround2.png");
			   break;
		   case 3:
			   background = loadTexture("data/BackGround3.png");
			   break;
		   }
		  backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
	}
	
}
