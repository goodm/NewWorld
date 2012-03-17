package com.lukasz.newworld;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.res.Resources;
import android.opengl.GLSurfaceView;
import android.util.Log;
import android.view.ScaleGestureDetector;

import com.threed.jpct.Camera;
import com.threed.jpct.Config;
import com.threed.jpct.FrameBuffer;
import com.threed.jpct.Light;
import com.threed.jpct.Loader;
import com.threed.jpct.Logger;
import com.threed.jpct.Object3D;
import com.threed.jpct.RGBColor;
import com.threed.jpct.SimpleVector;
import com.threed.jpct.Texture;
import com.threed.jpct.TextureManager;
import com.threed.jpct.World;
import com.threed.jpct.util.MemoryHelper;

class MyRender implements GLSurfaceView.Renderer 
{

	private boolean stop = false;
	private FrameBuffer fb = null;
	private World world = null;
	public boolean move = false;
	private RGBColor back = new RGBColor(50, 50, 100);
	private Object3D plane = null;

	private Texture font = null;
	
	private Object3D box = null;
	private Object3D rock = null;
	private Light sun = null;
	
	private Camera cam;
	private Object3D thing = null;
	private boolean start = false;
	
	private float bz;
	private float a = 0;
	private LoadModel models;
	private Resources res;

	public float scale = -210;
	public double rotate = 0;
	
	public int moveX = 0;
	public int moveY = 0;
	
	private float xpos = 0;
	private float ypos = 0;
	
	public MyRender(Resources resources) 
	{		
		world = new World();
		cam = world.getCamera();
		
		this.res = resources;
		models = new LoadModel(res);
		Config.maxPolysVisible = 500;
		Config.farPlane = 1500;
		Config.glTransparencyMul = 0.1f;
		Config.glTransparencyOffset = 0.1f;
		Config.useVBO=true;
		
		Texture.defaultToMipmapping(true);
		Texture.defaultTo4bpp(true);
		
		TextureManager tm = TextureManager.getInstance();
		Texture rocky = new Texture(res.openRawResource(R.raw.cate));
		Texture planetex = new Texture(res.openRawResource(R.raw.planetex));
		
		tm.flush();
		
		font = new Texture(res.openRawResource(R.raw.numbers));
		font.setMipmap(false);

		tm.addTexture("rock", rocky);
		tm.addTexture("grassy", planetex);

		plane = Loader.loadSerializedObject(res.openRawResource(R.raw.serplane));
		rock = Loader.loadSerializedObject(res.openRawResource(R.raw.serrock));
		rock.translate(0, 0, -90);
		rock.rotateX(-(float) Math.PI / 2);

		plane.rotateX((float) Math.PI / 2f);
		plane.setName("plane");
		rock.setName("rock");

		thing = models.loadModel("cat" + ".3ds", 10);			
   		thing.build();
   		thing.setTexture("rock");
		thing.translate(0,0,-150);
   		world.addObject(thing);
		world.addObject(plane);
		world.addObject(rock);

		plane.strip();

		rock.strip();
		thing.strip();

		world.buildAllObjects();								
		world.getCamera().setPosition(xpos, -1000-scale, ypos);
		sun = new Light(world);
		sun.setIntensity(250, 250, 250);

		SimpleVector sv = new SimpleVector();
		sv.set(plane.getTransformedCenter());
		sv.z = 100;
		sun.setPosition(sv);
	}

	public void stop() {
		stop = true;
	}

	public void onSurfaceChanged(GL10 gl, int w, int h) 
	{
		if (fb != null) 
		{
			fb.dispose();
		}

		fb = new FrameBuffer(gl, w, h);
		MemoryHelper.compact();				
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) 
	{			

	}
	
	public void onDrawFrame(GL10 gl) 
	{
		try 
		{
			if (!stop) 
			{					
				cam = world.getCamera();
                world.getCamera().getBack().setIdentity();
                
                world.getCamera().rotateZ((float)Math.toRadians(0));
                world.getCamera().rotateX((float)Math.toRadians(-90));
                world.getCamera().rotateY((float)Math.toRadians(0));

            	if(Math.floor(xpos) != Math.floor(xpos-moveX))
            	{
            		xpos -= moveX/50;
            	}
            	
            	if(Math.floor(ypos) != Math.floor(ypos-moveY))
            	{
            		ypos -= moveY/50;
            	}
                
                if(move == true)                	
                {
                	world.getCamera().setPosition(xpos, -1000-scale, ypos);
                }

                
				fb.clear(back);
				world.renderScene(fb);
				world.draw(fb);

				fb.display();
				
				if (box != null) 
				{
					box.rotateX(0.01f);
				}

			} 
			else 
			{
				if (fb != null) 
				{
					fb.dispose();
					fb = null;
				}
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			Logger.log("Drawing thread terminated!", Logger.MESSAGE);
		}
	}
}
