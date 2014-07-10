package com.example.flashbright;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.provider.Settings.System;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private int bright;
	private int tmp;
	
	private ContentResolver cResolver;
	private Camera camera;
	Parameters params;
	private Window window;
	private boolean hasFlash;
	private boolean isOn=false;
	private boolean isLite=false;
	private boolean isBack=true;
	
	ImageButton btnSwitch;
	ImageButton btnLite;
	ImageButton btnBack;
	ImageView bck;
	View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		view = this.getWindow().getDecorView();
		btnSwitch = (ImageButton) findViewById(R.id.onBtn);
		btnLite = (ImageButton) findViewById(R.id.onLiteBtn);
		btnBack = (ImageButton) findViewById(R.id.onBackBtn);	
		bck = (ImageView) findViewById(R.id.imageView1);
		cResolver = getContentResolver();
		
		window = getWindow();
		
		hasFlash = getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
		
		if (!hasFlash){
			Toast.makeText(getApplicationContext(), "Unfortunately, your device doesn't have flash light!", Toast.LENGTH_SHORT).show();
		}else{
			getCamera();
		}
		
		try{
			bright = System.getInt(cResolver, System.SCREEN_BRIGHTNESS);
			tmp = bright;
		}
		catch(SettingNotFoundException e){
			Log.e("ERROR", "System Brightness cannot be accessed!");
		}
		
		btnLite.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(hasFlash){
					if(!isBack){
						isLite = true;
						btnLite.setImageResource(R.drawable.btn_lite_on);
					}else{
						if (isOn){
							turnBackOff();
						}
						isLite = true;
						isBack = false;
						btnLite.setImageResource(R.drawable.btn_lite_on);
						btnBack.setImageResource(R.drawable.back_switch_off);
					}
				}else{
					btnLite.setImageResource(R.drawable.btn_lite_off);
				}
			}
		});
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(hasFlash){
					if(!isLite){
						isBack = true;
						btnBack.setImageResource(R.drawable.back_switch_on);
					}else{
						if (isOn){
							turnFlashOff();
						}
						isBack = true;
						isLite = false;
						btnLite.setImageResource(R.drawable.btn_lite_off);
						btnBack.setImageResource(R.drawable.back_switch_on);
					}
				}else{
					isBack = true;
					btnLite.setImageResource(R.drawable.btn_lite_off);
					btnBack.setImageResource(R.drawable.back_switch_on);
				}
			}
		});
		
		
		
		btnSwitch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!isOn&&isBack){
					turnBackOn();
				}else if(isOn&&isBack){
					turnBackOff();
				}else if(!isOn&&isLite){
					turnFlashOn();
				}else if(isOn&&isLite){
					turnFlashOff();
				}
			}
		});

		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Failed to Open. Error: ", e.getMessage());
            }
        }
    }
	
	private void turnBackOn(){
		bright = 255;
		System.putInt(cResolver, System.SCREEN_BRIGHTNESS, bright);
		LayoutParams lp = window.getAttributes();
		lp.screenBrightness = bright / (float) 255;
		window.setAttributes(lp);
		isOn=true;
		btnSwitch.setImageResource(R.drawable.btn_switch_on);
		bck.setVisibility(View.VISIBLE);
	}

	private void turnBackOff(){
		System.putInt(cResolver, System.SCREEN_BRIGHTNESS, tmp);
		LayoutParams lp = window.getAttributes();
		lp.screenBrightness = tmp / (float) 255;
		window.setAttributes(lp);
		isOn=false;
		btnSwitch.setImageResource(R.drawable.btn_switch_off);
		bck.setVisibility(View.GONE);
	}
	
	private void turnFlashOn(){
        if (camera == null || params == null) {
            return;
        }        
        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
        isOn=true;
        btnSwitch.setImageResource(R.drawable.btn_switch_on);
	}
	
	private void turnFlashOff(){
		if (camera == null || params == null) {
            return;
        }
        params = camera.getParameters();
        params.setFlashMode(Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        isOn=false;
        btnSwitch.setImageResource(R.drawable.btn_switch_off);
	}
	
	@Override
    protected void onDestroy() {
        super.onDestroy();
    }
 
    @Override
    protected void onRestart() {
        super.onRestart();
    }
 
    @Override
    protected void onStart() {
        super.onStart();
        // on starting the app get the camera params
        if(hasFlash){
        	getCamera();
        }
    }
 
    @Override
    protected void onStop() {
        super.onStop();
         
        // on stop release the camera
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
