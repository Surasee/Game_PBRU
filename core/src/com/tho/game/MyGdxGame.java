package com.tho.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;
import java.util.Vector;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture bgTexture, imgFrog, imgCoins, imgPig, imgCloud;
	private Music musicBackground;
	private Sound frogSound;
	private Rectangle frogRectangle;
	private OrthographicCamera objOrthographicCamera;
	private Vector3 objVector3;
	private BitmapFont nameBitmapFont;
	private int xcloudAnInt, ycloudAnInt = 570, driection = 1;
	private boolean cloudABoolean = true;
	private Array<Rectangle> objCoinsDrop;
	private long lastDorpTime;
	private Iterator<Rectangle> objIterator;



	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//set background
		bgTexture = new Texture("bg01.png");

		//set img
		imgPig = new Texture("pig.png");
		imgFrog = new Texture("frog.png");
		imgCoins = new Texture("coins.png");
		imgCloud = new Texture("cloud.png");


		//Create Camera
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1280, 768);


		//Setup BitMapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.BLACK);
		nameBitmapFont.setScale(4);



		//Inherit
		frogRectangle = new Rectangle();
		frogRectangle.x = 590;
		frogRectangle.y = 100;
		frogRectangle.width = 100;
		frogRectangle.height = 75;



		//set Music
		musicBackground = Gdx.audio.newMusic(Gdx.files.internal("bggame.mp3"));
		musicBackground.setLooping(true);
		musicBackground.play();

		//set frog sound
		frogSound = Gdx.audio.newSound(Gdx.files.internal("frog.wav"));

	}

	@Override
	public void render () {
		//set BG
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



		//About cam
		objOrthographicCamera.update();

		//update render
		batch.setProjectionMatrix(objOrthographicCamera.combined);


		//Draw Object
		batch.begin();

		//draw BG
		batch.draw(bgTexture, 0, 0);
		//drawable cloud
		batch.draw(imgCloud, xcloudAnInt, ycloudAnInt);



		//Drawable Font

		nameBitmapFont.draw(batch, "Coin's Frog", 50, 720);


		//Frog
		batch.draw(imgFrog, frogRectangle.x, frogRectangle.y);



		batch.end();

		//Active Frog
		activeTouch();

		//move cloud
		//moveCloud();
		moveCloud2();



		//control screen frog




	}

	private void activeTouch() {



		if (Gdx.input.isTouched()) {


			frogSound.play();
			objVector3 = new Vector3();
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);

			//Control Screen
			if (objVector3.x < 590) {
				frogRectangle.x -= (frogRectangle.x<0)?0:10;
			} else {
				frogRectangle.x += (frogRectangle.x>1180)?0:10;
			}




			//objOrthographicCamera.unproject(objVector3);
			//frogRectangle.x = objVector3.x - 50;
		}
	}

	private void moveCloud2() {
		if ((xcloudAnInt < 0 )||(xcloudAnInt > 957)) {
			driection *= -1;
		}
		xcloudAnInt += 200 * Gdx.graphics.getDeltaTime() * driection;
	}


	private void moveCloud() {
		if (cloudABoolean) {
			if (xcloudAnInt < 937) {
				xcloudAnInt += 200 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = !cloudABoolean;
			}

		} else {
			if (xcloudAnInt>0) {
				xcloudAnInt -= 200 * Gdx.graphics.getDeltaTime();
			} else {
				cloudABoolean = true;
			}
		}



	}//movecloud

}
