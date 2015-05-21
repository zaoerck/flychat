package com.way.activity;

import java.util.Timer;
import java.util.TimerTask;

import com.way.bean.Lanterns;
import com.way.push.R;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GameActivity extends Activity implements OnClickListener{
	ProgressBar progressBar;
    Lanterns lanterns=new Lanterns();
    Button button;
	
	private TextView mTitle, mTitleLeftBtn;
	TextView tv = null;  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		initView();
		
		progressBar= (ProgressBar) findViewById(R.id.lanternsprogressBar);
        progressBar.setVisibility(View.VISIBLE);
        RequestLanterns requestLanterns=new RequestLanterns();
        new Thread(requestLanterns).start();
	}
	
	
	Handler updateBarHandler = new Handler(){
        @SuppressLint("NewApi")
		public void handleMessage(Message msg){
            progressBar.setVisibility(View.INVISIBLE);
            LayoutInflater inflater = (LayoutInflater)GameActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View view = inflater.inflate(R.layout.lanternsgame_text, null);


            AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this,  AlertDialog.THEME_HOLO_LIGHT)
                    .setTitle("猜灯谜")
                    .setMessage("三人行（打一字）")
                    .setView(view);

            AlertDialog dialog=builder.create();
            dialog.show();

            button= (Button) view.findViewById(R.id.gameDialogButton);
            Log.w("button",button.toString());

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText lanternsanswer = (EditText) view.findViewById(R.id.lanternsanswer);
                    String lanternsanswerString = lanternsanswer.getText().toString();

                    judgeAnswer(lanternsanswerString);
                }
            });

        }
    };

    @SuppressLint({ "LongLogTag", "NewApi" })
    public void judgeAnswer(String lanternsanswerString){
        Log.w("输入内容",lanternsanswerString);
        boolean b1=lanternsanswerString.equals(lanterns.getAnswer());
        boolean b2=lanternsanswerString==lanterns.getAnswer();

        if(lanternsanswerString.equals("众")){
            Log.w("","right");
            AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                    .setMessage("回答正确");
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
            Timer timer = new Timer();
            TimerTask task = new TimerTask(){
            	public void run(){
            		Intent intent = new Intent(GameActivity.this, InfoActivity.class);
            		startActivity(intent);
            	}
            };
            timer.schedule(task, 1000);
        }
        else{
            Log.w("","wrong");
            AlertDialog.Builder builder=new AlertDialog.Builder(GameActivity.this, AlertDialog.THEME_HOLO_LIGHT)
                    .setMessage("回答错误,请重试");
            AlertDialog alertDialog=builder.create();
            alertDialog.show();
        }
    }
    class RequestLanterns implements Runnable{
        @Override
        public void run() {

//
//            mHttpClient.get(API.GetLanterns,0,new RequestListener() {
//                @Override
//                public void onPreRequest() {
//                    Log.w("","请求重发");
//                }
//
//                @Override
//                public void onRequestSuccess(BaseResponse response) {
//                    Log.w("","请求成功");
//                    lanterns=response.getObject(Lanterns.class);
//                    Log.w("lanterns",lanterns.toString());
//                    GameActivity.this.updateBarHandler.sendMessage(new Message());
//                }
//
//                @Override
//                public void onRequestError(int code, String msg) {
//                    Log.w("","请求错误");
//                }
//
//                @Override
//                public void onRequestFail(int code, String msg) {
//                    Log.w("","请求失败");
//                }
//            });

            GameActivity.this.updateBarHandler.sendMessage(new Message());
        }
    }

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.game, menu);
//		return true;
//	}
//	
//	 @Override
//	    public boolean onOptionsItemSelected(MenuItem item) {
//	        // Handle action bar item clicks here. The action bar will
//	        // automatically handle clicks on the Home/Up button, so long
//	        // as you specify a parent activity in AndroidManifest.xml.
//	        int id = item.getItemId();
//
//	        //noinspection SimplifiableIfStatement
//	        if (id == R.id.action_settings) {
//	            return true;
//	        }
//
//	        return super.onOptionsItemSelected(item);
//	    }
	
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
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		default:
			break;
		}
	}

}
