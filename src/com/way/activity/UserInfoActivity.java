package com.way.activity;

import com.way.push.R;
import com.way.push.R.layout;
import com.way.push.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class UserInfoActivity extends Activity implements OnClickListener{
	private TextView mTitle, mTitleLeftBtn;
	TextView tv = null;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_info);
		initView();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}
	
	private void initView(){
		mTitle = (TextView) findViewById(R.id.ivTitleName);
		mTitle.setText("游戏界面");
		mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		mTitleLeftBtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		mTitle = (TextView) findViewById(R.id.ivTitleName);
		mTitle.setText("个人信息");
		mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		mTitleLeftBtn.setOnClickListener(this);
	}

}
