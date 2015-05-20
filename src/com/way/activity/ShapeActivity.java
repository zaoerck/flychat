package com.way.activity;

import com.way.push.R;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ShapeActivity extends Activity implements OnClickListener, SensorEventListener{
	private TextView mTitle, mTitleLeftBtn;
	TextView tv = null;  
    Button button = null;  
    SensorManager sensorManager = null;  
    Vibrator vibrator = null; 
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shape);
		initView();
		
		 
        tv = (TextView) findViewById(R.id.mapTextView);  
          
  
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);  
        vibrator = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}
	
	private void initView(){
		mTitle = (TextView) findViewById(R.id.ivTitleName);
		mTitle.setText("添加好友");
		mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		mTitleLeftBtn.setOnClickListener(this);
	}

	/*
	 * 按钮点击
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		int sensorType = event.sensor.getType();  
        //values[0]:X轴，values[1]：Y轴，values[2]：Z轴  
        float[] values = event.values;  
        if (sensorType == Sensor.TYPE_ACCELEROMETER)  
        {  
            if ((Math.abs(values[0]) > 17 || Math.abs(values[1]) > 17 || Math  
                    .abs(values[2]) > 17))  
            {  
                Log.d("sensor x ", "============ values[0] = " + values[0]);  
                Log.d("sensor y ", "============ values[1] = " + values[1]);  
                Log.d("sensor z ", "============ values[2] = " + values[2]);  
                Intent intent = new Intent(this, MapActivity.class);
                startActivity(intent);
                //摇动手机后，再伴随震动提示~~  
                vibrator.vibrate(500);  
            }  
  
        }  
	}
	
	@Override  
    protected void onPause()  
    {  
        super.onPause();  
        sensorManager.unregisterListener(this);  
    } 
	
	@Override  
    protected void onResume()  
    {  
        super.onResume();  
        sensorManager.registerListener(this,  
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),  
                SensorManager.SENSOR_DELAY_NORMAL);  
    } 

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		//当传感器精度改变时回调该方法，Do nothing. 
	}

}
