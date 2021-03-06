package com.example.sargonocrapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.sargonocrapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class ImageCaptureActv extends Activity 
{
	
	private Camera mCamera;
    private CameraPreview mPreview;
    Bitmap currentImage;
    String OCRtxt = "";
    final int OCR_RESULT = 0;
    String ResultsTEXT = "";
	File filePathtoImage;
    
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_capture);

        mCamera = Camera.open(0);
        //Get camera parameters
        Camera.Parameters camParameters = mCamera.getParameters();
        
        //Set auto focus and start it
        camParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); 
        mCamera.setParameters(camParameters);
                
        // Create our Preview view and set it as the content of our activity.  
        mPreview = new CameraPreview(this, mCamera);    
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        
        //retrieve a reference to the UI button
        Button captureBtn = (Button)findViewById(R.id.button_capture);
        captureBtn.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View v)
        	{
            	// get an image from the camera
            	mCamera.takePicture(shutterCallback, null, mPicture);
        	}
        });
    }
    @Override
    public void onBackPressed() 
    {
    }     
    private final ShutterCallback shutterCallback = new ShutterCallback() 
    {
    	public void onShutter()
    	{
            //make a tone for in focus
//            ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
//    		tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD,  200);
    	}	
    };
    
	private PictureCallback mPicture = new PictureCallback() 
	{

        @Override
        public void onPictureTaken(byte[] data, Camera camera) 
        {

            File pictureFile = getOutputMediaFile();
            filePathtoImage = pictureFile;
            currentImage = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (pictureFile == null)
            {
            	//edit message
                return;
            }
            try 
            {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
            } 
            catch (FileNotFoundException e) 
            {
            	//edit message
            } 
            catch (IOException e) 
            {
            	//edit message
            }
            Intent intent = new Intent(ImageCaptureActv.this, DisplayImageActv.class);
            intent.putExtra("imagePath", filePathtoImage.getPath());
            startActivity(intent);      
            finish();   
        };
	
	};
    
	/** Check if this device has a camera */
	public boolean checkCameraHardware(Context context) 
	{
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA))
	    {
	        // this device has a camera
	        return true;
	    } 
	    else 
	    {
	        // no camera on this device
	        return false;
	    }
	}
	
	/** A safe way to get an instance of the Camera object. */
	public  Camera getCameraInstance()
	{
		if(!checkCameraHardware(this))
		{
			return null;
		}
	    Camera c = null;
	    try 
	    {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e)
	    {
	        // Camera is not available (in use or does not exist)
//	    	releaseCamera();
	    }
	    return c; // returns null if camera is unavailable
	}
	
	/** Create a file to store the image */
    private static File getOutputMediaFile()
    {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "sargonOCRApp");
        if (!mediaStorageDir.exists())
        {
            if (!mediaStorageDir.mkdirs())
            {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
 
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        return mediaFile;
    }
}
	    	
	    	
	    
	    
	  

	