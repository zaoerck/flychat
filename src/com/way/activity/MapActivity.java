package com.way.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baidu.android.common.logging.Log;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.way.bean.User;
import com.way.push.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MapActivity extends Activity implements OnClickListener{
	private MapView mMapView = null;
    private BaiduMap mBaiduMap;
    private LatLng ll;
    private LocationClient mLocationClient = null;
    private boolean isFirstLoc = true;// 是否首次定位
    private List<Marker> markers=new ArrayList<Marker>();
    private List<User> users=new ArrayList<User>();
    private InfoWindow mInfoWindow;
	

	private TextView mTitle, mTitleLeftBtn;
	TextView tv = null;  
    
    public BDLocationListener myListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {

            if (location == null || mMapView == null)
                return;
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                 ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                Log.w("纬度",location.getLatitude()+"");
                Log.w("经度",location.getLongitude()+"");
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.animateMapStatus(u);
            }

            PostLocation postLocation=new PostLocation();
            new Thread(postLocation).start();

            ShowUsersThread thread=new ShowUsersThread();
            new Thread(thread).start();


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数

        setContentView(R.layout.activity_map);
        initView();
        
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMaxAndMinZoomLevel(19,16);
        UiSettings ui=mBaiduMap.getUiSettings();
        ui.setCompassEnabled(false);
        ui.setRotateGesturesEnabled(false);
        ui.setOverlookingGesturesEnabled(false);


        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
//        option.setScanSpan(100000);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Button button = new Button(getApplicationContext());
                button.setBackgroundResource(R.drawable.popup);
                button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(MapActivity.this, GameActivity.class);
						startActivity(intent);
					}
				});
                for(int i=0;i<markers.size();i++){


                    if(marker==markers.get(i)){
                        Log.w("length",users.size()+"");
                        button.setText(users.get(i).getNick());
                        button.setTextColor(Color.parseColor("#000000"));
                        LatLng m_point = marker.getPosition();
                        mInfoWindow = new InfoWindow(button,m_point,-47);
                        mBaiduMap.showInfoWindow(mInfoWindow);
                    }

                }
                return false;
            }
        });


    }
	
	private void initView(){
		mTitle = (TextView) findViewById(R.id.ivTitleName);
		mTitle.setText("添加好友");
		mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		mTitleLeftBtn.setOnClickListener(this);
	}
	
	
	

	
	class PostLocation implements Runnable{
        @Override
        public void run() {
            Map<String,String> map=new HashMap<String,String>();
            map.put("latitude",ll.latitude+"");
            map.put("longitude",ll.longitude+"");
            map.put("id",123+"");
            Log.w("","发送自己位置信息");
//            mHttpClient.post(API.PostLocation,map,0,new RequestListener() {
//                @Override
//                public void onPreRequest() {
//                    Log.w("","请求重发");
//                }
//
//                @Override
//                public void onRequestSuccess(BaseResponse response) {
//
//                    Log.w("","发送信息成功");
//                }
//
//                @Override
//                public void onRequestError(int code, String msg) {
//                    Log.e("","发送请求错误");
//                }
//
//                @Override
//                public void onRequestFail(int code, String msg) {
//                    Log.e("","发送请求错误");
//                }
//            });
        }
    }
	
	class ShowUsersThread implements Runnable {

        public void run() {

//            Log.w("","获取用户信息");
//            mHttpClient.get(API.GetUser,0, new RequestListener(){
//
//                @Override
//                public void onPreRequest() {
//
//                    Log.w("","请求重发");
//                }
//
//                @Override
//                public void onRequestSuccess(BaseResponse response) {
//
//                    Log.w("","获取用户信息成功");
//                    List<User> allusers=response.getList(User.class);
//
//                    users=searchUser(allusers);
//                    Message msg = Message.obtain();
//                    msg.obj=users;
//                    updateMarkerHandler.sendMessage(msg);
//
//
//                }
//
//                @Override
//                public void onRequestError(int code, String msg) {
//
//                    Log.e("","获取请求错误");
//                }
//
//                @Override
//                public void onRequestFail(int code, String msg) {
//
//                    Log.e("","获取请求失败");
//                }
//            });

            List<User> allusers=getUser();
//
//                    users=searchUser(allusers);
            		users = allusers;
                    Message msg = Message.obtain();
                    msg.obj=users;
                    updateMarkerHandler.sendMessage(msg);

        }
    }

    public List<User> getUser(){
        User user1=new User();
        user1.setLatitude(31.9131313);
        user1.setLongitude(118.79111);
        user1.setNick("张三");
        User user2=new User();
        user2.setLatitude(31.91233);
        user2.setLongitude(118.795345);
        user2.setNick("李四");
        User user3=new User();
        user3.setLatitude(31.914323);
        user3.setLongitude(118.795345);
        user3.setNick("王五");
        User user4=new User();
        user4.setLatitude(31.9124234);
        user4.setLongitude(118.7923422);
        user4.setNick("小明");
        List<User> users=new ArrayList<User>();
        users.add(user1);
        users.add(user2);
        users.add(user3);
        users.add(user4);
        return users;
    }
    public List<User> searchUser(List<User> allusers){

        List<User> newUser=new ArrayList<User>();
        Log.w("allusers", allusers.toString());
        for(User user:allusers){
            LatLng point=new LatLng(user.getLatitude(),user.getLongitude());
            double distance=DistanceUtil.getDistance(ll,point);
            Log.w("距离",distance+"");
            if(distance<1000){
                newUser.add(user);
            }
        }
        return newUser;
    }

    //创建handler
    Handler updateMarkerHandler = new Handler(){
    	
        @Override
        public void handleMessage(Message msg) {
//            List<User> musers= (List<User>) msg.obj;
        	Log.w("有没有显示", "1");
        	if(users == null){
        		Log.w("users", "is null");
        	}
        	else Log.w("users", users.toString());
            for (User user:users) {

            	Log.w("有没有显示", "2");
                LatLng point=new LatLng(user.getLatitude(),user.getLongitude());
                //构建Marker图标
                BitmapDescriptor bitmap = BitmapDescriptorFactory
                        .fromResource(R.drawable.icon_marki);
//构建MarkerOption，用于在地图上添加Marker
                OverlayOptions option = new MarkerOptions()
                        .position(point)
                        .icon(bitmap);
//             mBaiduMap = nMapView.getMap();
//在地图上添加Marker，并显示

            	Log.w("有没有显示", "3");
                markers.add((Marker) mBaiduMap.addOverlay(option));

            }
       }


    };


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
    protected void onDestroy() {  
        super.onDestroy();  
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
        mMapView.onDestroy();
        mLocationClient.unRegisterLocationListener(myListener);
        mLocationClient.stop();
    }  
    @Override  
    protected void onResume() {  
        super.onResume();  
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
        mMapView.onResume();  
        }  
    @Override  
    protected void onPause() {  
        super.onPause();  
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
        mMapView.onPause();  
        }  
}
