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
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.way.push.R;
import android.os.Bundle;
import android.os.Vibrator;
import android.app.Activity;
import android.app.Service;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapActivity extends Activity implements OnClickListener{
//	private MapView mMapView = null;
//	private BaiduMap mBaiduMap;
//    private LatLng ll;
//    
//    private LocationClient mLocationClient = null;
//    
//    
//    private boolean isFirstLoc = true;// 是否首次定位
//    private List<Marker> markers=new ArrayList<Marker>();
//    private List<User> users=new ArrayList<User>();
//    private InfoWindow mInfoWindow;
	
	private MapView mMapView;
	private BaiduMap mBaiduMap;

	private LocationClient locationClient;
	private BDLocationListener locationListener;
	private BDNotifyListener notifyListener;

	private double longitude;// 精度
	private double latitude;// 维度
	private float radius;// 定位精度半径，单位是米
	private String addrStr;// 反地理编码
	private String province;// 省份信息
	private String city;// 城市信息
	private String district;// 区县信息
	private float direction;// 手机方向信息

	private int locType;

	// 定位图标描述
	private BitmapDescriptor currentMarker = null;
	// 记录是否第一次定位
	private boolean isFirstLoc = false;
	
	//振动器设备
	private Vibrator mVibrator;
    
	private TextView mTitle, mTitleLeftBtn;
	TextView tv = null;  
    Button button = null;
    
//    public BDLocationListener myListener = new BDLocationListener() {
//        @Override
//        public void onReceiveLocation(BDLocation location) {
//
//            if (location == null || mMapView == null)
//                return;
//            MyLocationData locData = new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                            // 此处设置开发者获取到的方向信息，顺时针0-360
//                    .direction(100).latitude(location.getLatitude())
//                    .longitude(location.getLongitude()).build();
//            mBaiduMap.setMyLocationData(locData);
//            if (isFirstLoc) {
//                isFirstLoc = false;
//                 ll = new LatLng(location.getLatitude(),
//                        location.getLongitude());
//                Log.w("纬度",location.getLatitude()+"");
//                Log.w("经度",location.getLongitude()+"");
//                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
//                mBaiduMap.animateMapStatus(u);
//            }
//
//            PostLocation postLocation=new PostLocation();
//            new Thread(postLocation).start();
//
//            ShowUsersThread thread=new ShowUsersThread();
//            new Thread(thread).start();
//
//
//        }
//    };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SDKInitializer.initialize(getApplicationContext());
//		
//		mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
//        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        
		setContentView(R.layout.activity_map);
		initView();
		
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
		init();
		
//		mMapView = (MapView) findViewById(R.id.bmapView);
//		
//		mBaiduMap = mMapView.getMap();
//		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
//	    mBaiduMap.setMyLocationEnabled(true);
//	    mBaiduMap.setMaxAndMinZoomLevel(20,15);
//	    UiSettings ui=mBaiduMap.getUiSettings();
//	    ui.setCompassEnabled(false);
//	    ui.setRotateGesturesEnabled(false);
//	    ui.setOverlookingGesturesEnabled(false);
//	    
//	    LocationClientOption option = new LocationClientOption();
//	    option.setOpenGps(true); //打开GPRS  
//        option.setAddrType("all");//返回的定位结果包含地址信息  
//        option.setCoorType("bd09ll");//返回的定位结果是百度经纬度,默认值gcj02  
////        option.setScanSpan(100000);
//        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
//        mLocationClient.setLocOption(option);//设置定位参数
//        mLocationClient.start();
//        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
//                Button button = new Button(getApplicationContext());
//                button.setBackgroundResource(R.drawable.popup);
//                for(int i=0;i<markers.size();i++){
//
//
//                    if(marker==markers.get(i)){
//                        Log.w("length",users.size()+"");
//                        button.setText(users.get(i).getNick());
//                        button.setTextColor(Color.BLACK);
//                        LatLng m_point = marker.getPosition();
//                        mInfoWindow = new InfoWindow(button,m_point,-47);
//                        mBaiduMap.showInfoWindow(mInfoWindow);
//                    }
//
//                }
//                return false;
//            }
//        });
	}
	
	private void initView(){
		mTitle = (TextView) findViewById(R.id.ivTitleName);
		mTitle.setText("添加好友");
		mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		mTitleLeftBtn.setOnClickListener(this);
	}
	
	/**
	 * 
	 */
	private void init() {
		mBaiduMap.setMyLocationEnabled(true);
		mBaiduMap.setMaxAndMinZoomLevel(19, 16);
		// 1. 初始化LocationClient类
		locationClient = new LocationClient(getApplicationContext());
		// 2. 声明LocationListener类
		locationListener = new MyLocationListener();
		// 3. 注册监听函数
		locationClient.registerLocationListener(locationListener);
		// 4. 设置参数
		LocationClientOption locOption = new LocationClientOption();
		locOption.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		locOption.setCoorType("bd09ll");// 设置定位结果类型
//		locOption.setScanSpan(5000);// 设置发起定位请求的间隔时间,ms
		locOption.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		locOption.setNeedDeviceDirect(true);// 设置返回结果包含手机的方向

		locationClient.setLocOption(locOption);
		// 5. 注册位置提醒监听事件
		notifyListener = new MyNotifyListener();
		notifyListener.SetNotifyLocation(longitude, latitude, 3000, "bd09ll");//经度，维度，范围，坐标类型
		locationClient.registerNotify(notifyListener);
		// 6. 开启/关闭 定位SDK
		locationClient.start();
		// locationClient.stop();
		// 发起定位，异步获取当前位置，因为是异步的，所以立即返回，不会引起阻塞
		// 定位的结果在ReceiveListener的方法onReceive方法的参数中返回。
		// 当定位SDK从定位依据判定，位置和上一次没发生变化，而且上一次定位结果可用时，则不会发生网络请求，而是返回上一次的定位结果。
		// 返回值，0：正常发起了定位 1：service没有启动 2：没有监听函数
		// 6：两次请求时间太短（前后两次请求定位时间间隔不能小于1000ms）
		/*
		 * if (locationClient != null && locationClient.isStarted()) {
		 * requestResult = locationClient.requestLocation(); } else {
		 * Log.d("LocSDK5", "locClient is null or not started"); }
		 */

	}
	
	class MyLocationListener implements BDLocationListener {
		// 异步返回的定位结果
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null) {
				return;
			}
			locType = location.getLocType();
			Toast.makeText(MapActivity.this, "当前定位的返回值是："+locType, Toast.LENGTH_SHORT).show();
			longitude = location.getLongitude();
			latitude = location.getLatitude();
			if (location.hasRadius()) {// 判断是否有定位精度半径
				radius = location.getRadius();
			}
			if (locType == BDLocation.TypeGpsLocation) {//
				Toast.makeText(
						MapActivity.this,
						"当前速度是：" + location.getSpeed() + "~~定位使用卫星数量："
								+ location.getSatelliteNumber(),
						Toast.LENGTH_SHORT).show();
			} else if (locType == BDLocation.TypeNetWorkLocation) {
				addrStr = location.getAddrStr();// 获取反地理编码(文字描述的地址)
				Toast.makeText(MapActivity.this, addrStr,
						Toast.LENGTH_SHORT).show();
			}
			direction = location.getDirection();// 获取手机方向，【0~360°】,手机上面正面朝北为0°
			province = location.getProvince();// 省份
			city = location.getCity();// 城市
			district = location.getDistrict();// 区县
			Toast.makeText(MapActivity.this,
					province + "~" + city + "~" + district, Toast.LENGTH_SHORT)
					.show();
			// 构造定位数据
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(radius)//
					.direction(direction)// 方向
					.latitude(latitude)//
					.longitude(longitude)//
					.build();
			// 设置定位数据
			mBaiduMap.setMyLocationData(locData);
			LatLng ll = new LatLng(latitude, longitude);
			MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(msu);

		}
	}
	
	/**
	 * 位置提醒监听器
	 * @author ys
	 *
	 */
	class MyNotifyListener extends BDNotifyListener {
		@Override
		public void onNotify(BDLocation bdLocation, float distance) {
			super.onNotify(bdLocation, distance);
			mVibrator.vibrate(1000);//振动提醒已到设定位置附近
	    	Toast.makeText(MapActivity.this, "震动提醒", Toast.LENGTH_SHORT).show();
		}
	}

	
//	class PostLocation implements Runnable{
//        @Override
//        public void run() {
//            Map<String,String> map=new HashMap<String,String>();
//            map.put("latitude",ll.latitude+"");
//            map.put("longitude",ll.longitude+"");
//            map.put("id",123+"");
//            Log.w("","发送自己位置信息");
////            mHttpClient.post(API.PostLocation,map,0,new RequestListener() {
////                @Override
////                public void onPreRequest() {
////                    Log.w("","请求重发");
////                }
////
////                @Override
////                public void onRequestSuccess(BaseResponse response) {
////
////                    Log.w("","发送信息成功");
////                }
////
////                @Override
////                public void onRequestError(int code, String msg) {
////                    Log.e("","发送请求错误");
////                }
////
////                @Override
////                public void onRequestFail(int code, String msg) {
////                    Log.e("","发送请求错误");
////                }
////            });
//        }
//    }
	
//	class ShowUsersThread implements Runnable {
//
//        public void run() {
//
////            Log.w("","获取用户信息");
////            mHttpClient.get(API.GetUser,0, new RequestListener(){
////
////                @Override
////                public void onPreRequest() {
////
////                    Log.w("","请求重发");
////                }
////
////                @Override
////                public void onRequestSuccess(BaseResponse response) {
////
////                    Log.w("","获取用户信息成功");
////                    List<User> allusers=response.getList(User.class);
////
////                    users=searchUser(allusers);
////                    Message msg = Message.obtain();
////                    msg.obj=users;
////                    updateMarkerHandler.sendMessage(msg);
////
////
////                }
////
////                @Override
////                public void onRequestError(int code, String msg) {
////
////                    Log.e("","获取请求错误");
////                }
////
////                @Override
////                public void onRequestFail(int code, String msg) {
////
////                    Log.e("","获取请求失败");
////                }
////            });
//
//            List<User> allusers=getUser();
////
//                    users=searchUser(allusers);
//                    Message msg = Message.obtain();
//                    msg.obj=users;
//                    updateMarkerHandler.sendMessage(msg);
//
//        }
//    }
	
//	public List<User> getUser(){
//        User user1=new User();
//        user1.setLatitude(31.9131313);
//        user1.setLongitude(118.79111);
//        user1.setNick("张三");
//        User user2=new User();
//        user2.setLatitude(31.91233);
//        user2.setLongitude(118.795345);
//        user2.setNick("李四");
//        User user3=new User();
//        user3.setLatitude(31.914323);
//        user3.setLongitude(118.795345);
//        user3.setNick("王五");
//        User user4=new User();
//        user4.setLatitude(31.9124234);
//        user4.setLongitude(118.7923422);
//        user4.setNick("小明");
//        List<User> users=new ArrayList<User>();
//        users.add(user1);
//        users.add(user2);
//        users.add(user3);
//        users.add(user4);
//        return users;
//    }
	
//	public List<User> searchUser(List<User> allusers){
//
//        List<User> newUser=new ArrayList<User>();
//        for(User user:allusers){
//            LatLng point=new LatLng(user.getLatitude(),user.getLongitude());
//            double distance=DistanceUtil.getDistance(ll,point);
//            Log.w("距离",distance+"");
//            if(distance<1000){
//                newUser.add(user);
//            }
//        }
//        return newUser;
//    }
	
//	//创建handler
//    Handler updateMarkerHandler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
////            List<User> musers= (List<User>) msg.obj;
//
//
//            int i=0;
//            for (User user:users) {
//
//                LatLng point=new LatLng(user.getLatitude(),user.getLongitude());
//                //构建Marker图标
//                BitmapDescriptor bitmap = BitmapDescriptorFactory
//                        .fromResource(R.drawable.icon_marki);
////构建MarkerOption，用于在地图上添加Marker
//                OverlayOptions option = new MarkerOptions()
//                        .position(point)
//                        .icon(bitmap);
////             mBaiduMap = nMapView.getMap();
////在地图上添加Marker，并显示
//
//                markers.add((Marker) mBaiduMap.addOverlay(option));
//
//            }
//       }
//
//
//    };

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
        locationClient.unRegisterLocationListener(locationListener);
		//取消位置提醒
		locationClient.removeNotifyEvent(notifyListener);
		locationClient.stop();
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
