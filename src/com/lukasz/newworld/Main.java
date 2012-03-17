package com.lukasz.newworld;

import android.app.Activity;
import android.graphics.Point;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.OnScaleGestureListener;


public class Main extends Activity implements OnScaleGestureListener
{
	private GLSurfaceView mGLView;
	private MyRender render;
	private ScaleGestureDetector gestureDec = null;
	
	private float scale = 0.05f;
	
	private Point touch1;
	private Point touch2;
	private	boolean reset = false;
	private double touch = 0; 
	private float xpos = 0;
	private float ypos = 0;
	
	protected void onCreate(Bundle savedInstanceState) 
	{		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);			
		
		touch1 = new Point();
		touch2 = new Point();
		
		mGLView = (GLSurfaceView)findViewById(R.id.glsurfaceview);

		render = new MyRender(getResources());
		mGLView.setRenderer(render);
		
		gestureDec = new ScaleGestureDetector(mGLView.getContext(), this);
	}

	@Override
	protected void onPause() 
	{
		super.onPause();
		mGLView.onPause();	
	}

	@Override
	protected void onResume() 
	{
		super.onResume();
		mGLView.onResume();	       
	}

	protected void onStop() 
	{
		super.onStop();  
	}
	
	public boolean onTouchEvent(MotionEvent me) 
	{
			/*
			double rot = 0;			
			
			touch1.x = (int)me.getX(0);
			touch1.y = (int)me.getY(0);
			
			touch2.x = (int)me.getX(1);
			touch2.y = (int)me.getY(1);
			
			if(reset == false)
			{
				touch = Math.atan2(touch1.y-touch2.y, touch1.x-touch2.x)* 180/Math.PI;
				reset = true;
			}
						
			
			
			rot = Math.atan2(touch1.y-touch2.y, touch1.x-touch2.x)* 180/Math.PI;
			rot = touch - rot;
			render.rotate = rot;
			Log.e("DEGREE",String.valueOf(rot) + " touch: " + String.valueOf(touch));
			*/
		
		if(me.getPointerCount() == 1)
		{		

			if (me.getAction() == MotionEvent.ACTION_DOWN) 
			{
				if(reset == false)
				{
					xpos = me.getX();				
					ypos = me.getY();
					reset = true;
				}
				return true;
			}

			if (me.getAction() == MotionEvent.ACTION_UP) 
			{
				reset = false;
				render.moveX = 0;
				render.moveY = 0;
				xpos = 0;
				ypos = 0;
				return true;
			}

			if (me.getAction() == MotionEvent.ACTION_MOVE) 
			{
				render.moveX = (int) (me.getX() - xpos)/50;
				render.moveY = (int) (ypos - me.getY())/50;
				
				return true;
			}
		}
		else
		{
			gestureDec.onTouchEvent(me);
		}
		
		try 
		{
			Thread.sleep(15);
		} 
		catch (Exception e) 
		{
			// Doesn't matter here...
		}

		return super.onTouchEvent(me);
	}


	protected boolean isFullscreenOpaque()
	{
		return true;
	}

	@Override
	public boolean onScale(ScaleGestureDetector detector) 
	{
		float div = detector.getCurrentSpan() - detector.getPreviousSpan();
		div /= 5000;
	
		scale += div;
	
		if (scale > 0.063f) 
		{
			scale = 0.063f;
		}
		
		if (scale < 0) 
		{
			scale = 0;
		}
	
		render.scale = -10000*scale;
		return true;
	}

	@Override
	public boolean onScaleBegin(ScaleGestureDetector detector) 
	{
		render.move = true;
		return true;
	}

	@Override
	public void onScaleEnd(ScaleGestureDetector detector) 
	{
		render.move = false;
		touch = 0;
		render.moveX = 0;
		render.moveY = 0;	
		reset = false;
	}
}
