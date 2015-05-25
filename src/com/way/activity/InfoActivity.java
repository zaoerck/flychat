package com.way.activity;

import com.way.app.PushApplication;
import com.way.common.util.SharePreferenceUtil;
import com.way.db.UserDB;
import com.way.push.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class InfoActivity extends Activity implements OnClickListener{
	private TextView mTitle, mTitleLeftBtn;
	private Button addFriend;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		initView();
	}
	

	private void initView() {
		// TODO Auto-generated method stub
		mTitle = (TextView) findViewById(R.id.ivTitleName);
		mTitle.setText("个人信息");
		mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
		mTitleLeftBtn.setVisibility(View.VISIBLE);
		mTitleLeftBtn.setOnClickListener(this);
		addFriend = (Button) findViewById(R.id.addFirend);
	}

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


}
