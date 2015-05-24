package com.way.activity;

import com.way.game.GameView;
import com.way.game.MyDialog;
import com.way.game.OnStateListener;
import com.way.game.OnTimerListener;
import com.way.game.OnToolsChangeListener;
import com.way.push.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

public class GameActivity extends Activity
	implements OnClickListener,OnTimerListener,OnStateListener,OnToolsChangeListener{
	
	private TextView mTitle, mTitleLeftBtn;
	TextView tv = null;
	
	private ImageButton btnPlay;
	private ImageButton btnRefresh;
	private ImageButton btnTip;
	private ImageView imgTitle;
	private GameView gameView;
	private SeekBar progress;
	private MyDialog dialog;
	private ImageView clock;
	private TextView textRefreshNum;
	private TextView textTipNum;
	
	private MediaPlayer player;
	
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
			case 0:
				dialog = new MyDialog(GameActivity.this,gameView,"胜利！",gameView.getTotalTime() - progress.getProgress());
				dialog.show();
				break;
			case 1:
				dialog = new MyDialog(GameActivity.this,gameView,"失败！",gameView.getTotalTime() - progress.getProgress());
				dialog.show();
			}
		}
	};
	
  

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
		initView();
		
        GameView.initSound(this);
        
        Animation scale = AnimationUtils.loadAnimation(this,R.anim.scale_anim);
        imgTitle.startAnimation(scale);
        btnPlay.startAnimation(scale);
        
        player = MediaPlayer.create(this, R.raw.bg);
        player.setLooping(true);//设置循环播放
        player.start();
	}
	
	
	
	private void initView(){
//		mTitle = (TextView) findViewById(R.id.ivTitleName);
//		mTitle.setText("游戏界面");
//		mTitleLeftBtn = (TextView) findViewById(R.id.ivTitleBtnLeft);
//		mTitleLeftBtn.setVisibility(View.VISIBLE);
//		mTitleLeftBtn.setOnClickListener(this);
		
		btnPlay = (ImageButton) findViewById(R.id.play_btn);
        btnRefresh = (ImageButton) findViewById(R.id.refresh_btn);
        btnTip = (ImageButton) findViewById(R.id.tip_btn);
        imgTitle = (ImageView) findViewById(R.id.title_img);
        gameView = (GameView) findViewById(R.id.game_view);
        clock = (ImageView) findViewById(R.id.clock);
        progress = (SeekBar) findViewById(R.id.timer);
        textRefreshNum = (TextView) findViewById(R.id.text_refresh_num);
        textTipNum = (TextView) findViewById(R.id.text_tip_num);
        //XXX
        progress.setMax(gameView.getTotalTime());
        
        btnPlay.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);
        btnTip.setOnClickListener(this);
        gameView.setOnTimerListener(this);
        gameView.setOnStateListener(this);
        gameView.setOnToolsChangedListener(this);
	}

	@Override
    protected void onPause() {
    	super.onPause();
    	gameView.setMode(GameView.PAUSE);
    }
    
    @Override
	protected void onDestroy() {
    	super.onDestroy();
    	gameView.setMode(GameView.QUIT);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.play_btn:
    		Animation scaleOut = AnimationUtils.loadAnimation(this,R.anim.scale_anim_out);
        	Animation transIn = AnimationUtils.loadAnimation(this,R.anim.trans_in);
    		
    		btnPlay.startAnimation(scaleOut);
    		btnPlay.setVisibility(View.GONE);
    		imgTitle.setVisibility(View.GONE);
    		gameView.setVisibility(View.VISIBLE);
    		
    		btnRefresh.setVisibility(View.VISIBLE);
    		btnTip.setVisibility(View.VISIBLE);
    		progress.setVisibility(View.VISIBLE);
    		clock.setVisibility(View.VISIBLE);
    		textRefreshNum.setVisibility(View.VISIBLE);
    		textTipNum.setVisibility(View.VISIBLE);
    		
    		btnRefresh.startAnimation(transIn);
    		btnTip.startAnimation(transIn);
    		gameView.startAnimation(transIn);
    		player.pause();
    		gameView.startPlay();
    		break;
    	case R.id.refresh_btn:
    		Animation shake01 = AnimationUtils.loadAnimation(this,R.anim.shake);
    		btnRefresh.startAnimation(shake01);
    		gameView.refreshChange();
    		break;
    	case R.id.tip_btn:
    		Animation shake02 = AnimationUtils.loadAnimation(this,R.anim.shake);
    		btnTip.startAnimation(shake02);
    		gameView.autoClear();
    		break;
		default:
			break;
		}
	}
	
	@Override
	public void onTimer(int leftTime) {
		Log.i("onTimer", leftTime+"");
		progress.setProgress(leftTime);
	}

	@Override
	public void OnStateChanged(int StateMode) {
		switch(StateMode){
		case GameView.WIN:
			handler.sendEmptyMessage(0);
			break;
		case GameView.LOSE:
			handler.sendEmptyMessage(1);
			break;
		case GameView.PAUSE:
			player.stop();
	    	gameView.player.stop();
	    	gameView.stopTimer();
			break;
		case GameView.QUIT:
			player.release();
	    	gameView.player.release();
	    	gameView.stopTimer();
	    	break;
		}
	}

	@Override
	public void onRefreshChanged(int count) {
		textRefreshNum.setText(""+gameView.getRefreshNum());
	}

	@Override
	public void onTipChanged(int count) {
		textTipNum.setText(""+gameView.getTipNum());
	}
	
	public void quit(){
		this.finish();
	}

}
