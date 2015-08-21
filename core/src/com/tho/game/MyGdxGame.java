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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class MyGdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	private Texture bgTexture, imgFrog, imgCoins, imgPig, imgCloud, imgWater, imgPause;
	private Music musicBackground;
	private Sound frogSound, successSound, falseSound;
	private Rectangle frogRectangle, coinsRectangle, waterRectangle, pauseRectangle;
	private OrthographicCamera objOrthographicCamera;
	private Vector3 objVector3;
	private BitmapFont nameBitmapFont, scoreBitmapFont, playBitmapFont, endBitmapFont;
	private int xcloudAnInt, ycloudAnInt = 570, driection = 1, scoreAnInt, endAnInt = 10,point = 0;


	private Array<Rectangle> objCoinsDrop, objWaterDrop, objPause;
	private long lastDropCoins, lastDropWater;
	private Iterator<Rectangle> coinsIterator, watersIterator, pauseIterator;

	private String[] nameStrings = {"pause.png","pig.png"};



	
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
		imgWater = new Texture("droplet.png");
		imgPause = new Texture(nameStrings[0]);



		//Create Camera
		objOrthographicCamera = new OrthographicCamera();
		objOrthographicCamera.setToOrtho(false, 1280, 768);



		//Setup BitMapFont
		nameBitmapFont = new BitmapFont();
		nameBitmapFont.setColor(Color.BLACK);
		nameBitmapFont.setScale(4);

		scoreBitmapFont = new BitmapFont();
		scoreBitmapFont.setColor(Color.YELLOW);
		scoreBitmapFont.setScale(3);

		endBitmapFont = new BitmapFont();
		endBitmapFont.setColor(Color.RED);
		endBitmapFont.setScale(3);



		//Inherit
		frogRectangle = new Rectangle();
		frogRectangle.x = 590;
		frogRectangle.y = 100;
		frogRectangle.width = 100;
		frogRectangle.height = 75;


		//Pause
		pauseRectangle = new Rectangle();
		pauseRectangle.x = 1150;
		pauseRectangle.y = 650;
		pauseRectangle.width = 100;
		pauseRectangle.height = 100;


		//Create CoinsDorp
		objCoinsDrop = new Array<Rectangle>();
		gameCoinsDrop();

		//Create WaterDrop
		objWaterDrop = new Array<Rectangle>();
		gameWaterDrop();


		//set Music
		musicBackground = Gdx.audio.newMusic(Gdx.files.internal("bggame.mp3"));
		musicBackground.setLooping(true);
		musicBackground.play();

		//set frog sound
		frogSound = Gdx.audio.newSound(Gdx.files.internal("frog.wav"));
		successSound = Gdx.audio.newSound(Gdx.files.internal("coins_drop.wav"));
		falseSound = Gdx.audio.newSound(Gdx.files.internal("water_drop.wav"));

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

		//Frog
		batch.draw(imgFrog, frogRectangle.x, frogRectangle.y);

		//Coins
		for(Rectangle forRectangle : objCoinsDrop){
			batch.draw(imgCoins,forRectangle.x,forRectangle.y);
		}// for

		//water
		for(Rectangle forRectangle : objWaterDrop){
			batch.draw(imgWater,forRectangle.x,forRectangle.y);
		}// for


		//drawable cloud
		batch.draw(imgCloud, xcloudAnInt, ycloudAnInt);



		//Drawable Font
		nameBitmapFont.draw(batch, "Coin's Frog By Tho", 50, 720);

		//Score
		scoreBitmapFont.draw(batch, "Your Score = " + scoreAnInt, 50, 70);

		//Life Times
		endBitmapFont.draw(batch, "Life Times = " + endAnInt, 940, 70);

		// Pause Button

		batch.draw(imgPause, pauseRectangle.x, pauseRectangle.y);


		// End Draw
		batch.end();



		//Active Frog
		activeTouch();

		//move cloud
		moveCloud();


		//coins move
		coinsMove();

		//water move
		watersMove();




	}//render





	private void activeTouch() {
		if (Gdx.input.isTouched()) {




			frogSound.play();
			objVector3 = new Vector3();
			objVector3.set(Gdx.input.getX(), Gdx.input.getY(), 0);


			//Pause touch
			objOrthographicCamera.unproject(objVector3);
			Rectangle myPause = new Rectangle();
			myPause.x = objVector3.x;
			myPause.y = objVector3.y;
			if(myPause.overlaps(pauseRectangle)){
				imgPause = new Texture(nameStrings[(++point)%nameStrings.length]);
			}
			//End Pause Touch

			//Control Screen
			if (objVector3.x < Gdx.graphics.getWidth()/2) { // <<<<<<<<half Display
				frogRectangle.x -= (frogRectangle.x<0)?0:10;
			} else {
				frogRectangle.x += (frogRectangle.x>1180)?0:10;
			}
			// move touch
			//objOrthographicCamera.unproject(objVector3);
			//frogRectangle.x = objVector3.x - 50;
		}

	}//Touch

	private void watersMove() {

		//Check Time End of Drop Water
		if (TimeUtils.nanoTime() - lastDropWater > 1E9)
		{
			gameWaterDrop();
		}
		watersIterator = objWaterDrop.iterator();
		while (watersIterator.hasNext()){
			Rectangle objMyWater = watersIterator.next();
			objMyWater.y -= 80*Gdx.graphics.getDeltaTime();
			if(objMyWater.y + 64 < 0)
			{

				watersIterator.remove();
			}//if
			if(objMyWater.overlaps(frogRectangle))
			{
				falseSound.play();
				scoreAnInt--;
				watersIterator.remove();
			}
		}//while
	}

	private void coinsMove() {

		//Check Time End of Drop Coins
		if (TimeUtils.nanoTime() - lastDropCoins > 1E9)
		{
			gameCoinsDrop();
		}
		coinsIterator = objCoinsDrop.iterator();
		while (coinsIterator.hasNext()){
			Rectangle objMyCoins = coinsIterator.next();
			objMyCoins.y -= 200*Gdx.graphics.getDeltaTime();
			if(objMyCoins.y + 64 < 0)
			{
				falseSound.play();
				endAnInt--;
				coinsIterator.remove();
			}//if
			if(objMyCoins.overlaps(frogRectangle))
			{
				successSound.play();
				scoreAnInt++;
				coinsIterator.remove();
			}
		}//while
	}

	private void moveCloud() {
		if ((xcloudAnInt < 0 )||(xcloudAnInt > 957)) {
			driection *= -1;
		}
		xcloudAnInt += 200 * Gdx.graphics.getDeltaTime() * driection;
	}

	private void gameCoinsDrop() {

		coinsRectangle = new Rectangle();
		coinsRectangle.x = MathUtils.random(0, 1226);
		coinsRectangle.y = 570;
		coinsRectangle.width = 64;
		coinsRectangle.height = 64;
		objCoinsDrop.add(coinsRectangle);
		lastDropCoins = TimeUtils.nanoTime();
	}//game coins drop


	private void gameWaterDrop() {

		waterRectangle = new Rectangle();
		waterRectangle.x = MathUtils.random(0, 1226);
		waterRectangle.y = 570;
		waterRectangle.width = 64;
		waterRectangle.height = 64;
		objWaterDrop.add(waterRectangle);
		lastDropWater = TimeUtils.nanoTime();
	}//game water drop

}
