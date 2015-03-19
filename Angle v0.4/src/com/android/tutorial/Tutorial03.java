package com.android.tutorial;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.android.angle.AngleActivity;
import com.android.angle.AngleRotatingSprite;
import com.android.angle.AngleSpriteLayout;
import com.android.angle.FPSCounter;

/**
 * Use FPSCounter class to see performance, put the activity in fullscreen (look in AndroidManifest.xml) and play with alpha
 * 
 * 
 * @author Ivan Pajuelo
 * 
 */
public class Tutorial03 extends AngleActivity
{
	private class MyAnimatedSprite extends AngleRotatingSprite
	{
		private static final float sRotationSpeed = 20;
		private static final float sAlphaSpeed = 0.5f;
		private float mAplhaDir;

		public MyAnimatedSprite(AngleSpriteLayout layout)
		{
			super(layout);
			mAplhaDir=sAlphaSpeed;
		}

		@Override
		public void step(float secondsElapsed)
		{
			mRotation+=secondsElapsed*sRotationSpeed;
			mAlpha+=secondsElapsed*mAplhaDir;
			if (mAlpha>1)
			{
				mAlpha=1;
				mAplhaDir=-sAlphaSpeed;
			}
			if (mAlpha<0)
			{
				mAlpha=0;
				mAplhaDir=sAlphaSpeed;
			}
			super.step(secondsElapsed);
		}
		
	};
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		AngleSpriteLayout mLogoLayout = new AngleSpriteLayout(mGLSurfaceView, 128, 128, R.drawable.anglelogo);
		MyAnimatedSprite mLogo = new MyAnimatedSprite (mLogoLayout);
		mLogo.mPosition.set(160, 200); 
		mGLSurfaceView.addObject(mLogo);
		
		//Add FPS counter. See logcat
		mGLSurfaceView.addObject(new FPSCounter());

		FrameLayout mMainLayout=new FrameLayout(this);
		mMainLayout.addView(mGLSurfaceView);
		setContentView(mMainLayout);
	}
}
