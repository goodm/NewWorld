package com.lukasz.newworld;

import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.threed.jpct.Loader;
import com.threed.jpct.Matrix;
import com.threed.jpct.Object3D;
import com.threed.jpct.SimpleVector;

public class LoadModel 
{
	private Resources res;
	
	public LoadModel(Resources resources)
	{
		this.res = resources;
	}
	
	public Object3D loadModel(String filename, float scale) 
	{
		Log.d("LoadModel", "Loading");
		AssetManager assMan = res.getAssets();
		InputStream is = new InputStream() 
		{
			@Override
			public int read() throws IOException 
			{
				return 0;
			}
		};
		
		try 
		{
			is =  assMan.open(filename, AssetManager.ACCESS_UNKNOWN);
		} 
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        Object3D[] model = Loader.load3DS(is,scale);
        Object3D o3d = new Object3D(0);
        Object3D temp = null;
        for (int i = 0; i < model.length; i++) 
        {
            temp = model[i];
            Log.d("LoadModel", String.valueOf(i));
            temp.setCenter(SimpleVector.ORIGIN);
            temp.rotateX((float)( -.5*Math.PI));
            temp.rotateMesh();
            temp.setRotationMatrix(new Matrix());
            o3d = Object3D.mergeObjects(o3d, temp);
            o3d.build();
        }
        return o3d;
    }
}
