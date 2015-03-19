package com.android.tutorial;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleFont;
import com.android.angle.AngleObject;
import com.android.angle.AngleSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.AngleString;
import com.android.angle.AngleTileBank;
import com.android.angle.AngleTileMap;
import com.android.angle.AngleUI;
import com.android.angle.AngleVector;
import com.android.angle.FPSCounter;

/**
 * Cretate little game simulation
 * 
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Game extends AngleActivity
{
	private MyDemo mDemo;
	
	public class MyDemo extends AngleUI
	{
		private AngleTileMap tmGround;
		private static final long mShotColdDownTime = 70;
		private AngleSpriteLayout slShip;
		private AngleSpriteLayout slShot;
		private MyShip mShip;
		private long mShotColdDown = 0;
		private AngleObject ogField;
		private AngleObject ogDashboard;

		
		class MyShip extends AngleSprite
		{
			AngleVector mDestination;
			float Speed; 
			public MyShip(AngleSpriteLayout sprite)
			{
				super(sprite);
				mPosition.set(160,240);
				mDestination=new AngleVector(mPosition); 
				Speed=200;
			}
			@Override
			public void step(float secondsElapsed)
			{
				if ((int)mPosition.mX<(int)mDestination.mX)
					mPosition.mX+=Speed*secondsElapsed;
				if ((int)mPosition.mX>(int)mDestination.mX)
					mPosition.mX-=Speed*secondsElapsed;
				if ((int)mPosition.mY<(int)mDestination.mY)
					mPosition.mY+=Speed*secondsElapsed;
				if ((int)mPosition.mY>(int)mDestination.mY)
					mPosition.mY-=Speed*secondsElapsed;
				super.step(secondsElapsed);
			}
		}
		
		class MyShot extends AngleSprite
		{

			public MyShot(MyShip ship, AngleSpriteLayout layout)
			{
				super(layout);
				mPosition.set(ship.mPosition.mX, ship.mPosition.mY - 20);
			}

			@Override
			public void step(float secondsElapsed)
			{
				mPosition.mY -= 300 * secondsElapsed;
				if (mPosition.mY < -10)
					mDie=true;
				super.step(secondsElapsed);
			}
			
		};

		public MyDemo(AngleActivity activity)
		{
			super(activity);
			
			//Create two main object groups. One for the game field and other for the dashboard
			ogField=new AngleObject();
			addObject(ogField);
			ogDashboard=new AngleObject();
			addObject(ogDashboard);

			AngleTileBank tbGround = new AngleTileBank(mActivity.mGLSurfaceView,R.drawable.tilemap,4,4,32,32);
			tmGround = new AngleTileMap(tbGround, 320, 480, 15, 180, false,false);
			for (int t=0;t<tmGround.mColumnsCount*tmGround.mRowsCount;t++)
				tmGround.mMap[t]=(int) (Math.random()*tbGround.mTilesCount);
			// Put the top of the camera at the lowest part of the map
			tmGround.mTop = tmGround.mRowsCount* tbGround.mTileHeight - tmGround.mHeight;
			ogField.addObject(tmGround);
			slShip = new AngleSpriteLayout(mActivity.mGLSurfaceView,64, 64, R.drawable.anglelogo, 0, 0, 128, 128);
			slShot = new AngleSpriteLayout(mActivity.mGLSurfaceView,16, 16, R.drawable.anglelogo, 0, 0, 128, 128);
			mShip = new MyShip(slShip);
			ogField.addObject(mShip);
			
			//The dashboard background
			AngleSpriteLayout slDash = new AngleSpriteLayout(mActivity.mGLSurfaceView, 320, 64, R.drawable.tilemap, 0, 32, 320, 64);
			AngleSprite mDash=new AngleSprite (slDash);
			mDash.mPosition.set(160, 480-slDash.roHeight/2);
			mDash.mAlpha=0.5f;
			ogDashboard.addObject(mDash);

			//Font and text
			AngleFont fntCafe25 = new AngleFont(mActivity.mGLSurfaceView, 25, Typeface.createFromAsset(getAssets(),"cafe.ttf"), 222, 0, 0, 30, 200, 255, 255);
			AngleString mText = new AngleString(fntCafe25);
			mText.set("Hola");
			mText.mAlignment = AngleString.aCenter;
			mText.mPosition.set(160, 440); 
			ogDashboard.addObject(mText);
		}

		@Override
		public boolean onTouchEvent(MotionEvent event)
		{
			// Prevent event flooding
			// Max 33 events per second
			try
			{
				Thread.sleep(30);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
			// -------------------------
			mShip.mDestination.set(event.getX(), event.getY() - 32 - slShip.roHeight / 2); // Position the ship
			long CTM = System.currentTimeMillis();
			if (CTM > mShotColdDown) // Prevent shoot in less than mShotColdDownTime milliseconds
			{
				mShotColdDown = CTM + mShotColdDownTime;
				ogField.addObject(new MyShot(mShip, slShot));
			}
			return true;
		}

		@Override
		public void step(float secondsElapsed)
		{
			tmGround.mTop -= 200 * secondsElapsed; // Move the camera
			super.step(secondsElapsed);
		}

	}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		//Add FPS counter. See logcat
		mGLSurfaceView.addObject(new FPSCounter());

		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
		
		mDemo=new MyDemo(this);
		setUI(mDemo);
	}
}
