package com.way.activity;

import com.way.bean.MessageItem;
import com.way.bean.RecentItem;
import com.way.common.util.SendMsgAsyncTask;
import com.way.push.R;
import com.way.push.R.layout;
import com.way.push.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MapActivity extends Activity implements OnClickListener{
	private TextView mTitle, mTitleLeftBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		initView();
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
		//点击返回跳转会主页面
		case R.id.ivTitleBtnLeft:
			startActivity(new Intent(getApplicationContext(), MainActivity.class));
			break;
		case R.id.ivTitleBtnRigh:
			break;
		default:
			break;
		}
	}

}
