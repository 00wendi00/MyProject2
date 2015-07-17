/**
 *文件名:Image.java
 *项目-包:私密图片&文本管理器,cn.hainu.wendi
 *作者:张文迪
 *时间:2015年5月3日
 */
package cn.hainu.Images;

import java.io.File;

import cn.hainu.utils.Shock;
import cn.hainu.wendi.Activity_Main;
import cn.hainu.wendi.R;
import cn.hainu.wendi.Activity_Setting;
import cn.hainu.wendi.R.anim;
import cn.hainu.wendi.R.color;
import cn.hainu.wendi.R.id;
import cn.hainu.wendi.R.layout;
import cn.hainu.wendi.R.string;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author 张文迪
 * 类名:Image
 * 类说明:
 */
@SuppressLint("HandlerLeak")
public class Activity_ImageV extends Activity implements OnTouchListener,OnGestureListener
{
	private GestureDetector myDetector;
	private String color_string;
	private boolean sound_flag = true;
	private Intent intent;
	private RelativeLayout re_layout;
	private FrameLayout fr_layout;
	private TextView autoPlay,image_setting,textDelete;
	private ImageView image_back;
	private AdapterViewFlipper viewFlipper;
	private int  position = 0, number = Shock.listImageName.size();
	
	private void findViews(){
		fr_layout = (FrameLayout)findViewById(R.id.image_linearLayout);
		re_layout = (RelativeLayout)findViewById(R.id.text_relay);
		autoPlay = (TextView)findViewById(R.id.autoPlay);
		image_setting = (TextView)findViewById(R.id.image_setting);
		textDelete = (TextView)findViewById(R.id.shanchu);
		image_back = (ImageView)findViewById(R.id.back_image);
		viewFlipper = (AdapterViewFlipper)findViewById(R.id.image_view);
	}
	@SuppressLint("HandlerLeak")
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_view);
		
		findViews();
		
		position = this.getIntent().getExtras().getInt("Num1");
		myDetector = new GestureDetector(Activity_ImageV.this, this);
		//name = Shock.listImageName.get(position);
		//bitmap = BitmapFactory.decodeFile(Shock.PATH_IMAGE+"/"+ name);
		re_layout.setVisibility(View.VISIBLE);
		viewFlipper.setAdapter(getAdapter());
		
		image_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				Intent intent = new Intent(Activity_ImageV.this,Activity_Main.class);
				startActivity(intent);
				Activity_ImageV.this.finish();
				overridePendingTransition(R.anim.left_in, R.anim.right_out);
			}
		});
		autoPlay.setOnClickListener(new TextView.OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				if(viewFlipper.isFlipping()){
					viewFlipper.stopFlipping();
				}else {
					viewFlipper.startFlipping();
				}
				
				re_layout.setVisibility(View.GONE);
			}
		});
		image_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				viewFlipper.stopFlipping();
				Intent intent = new Intent(Activity_ImageV.this,Activity_Setting.class);
				startActivity(intent);
				overridePendingTransition(R.anim.right_in,R.anim.left_out);
			}
		});
		textDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				viewFlipper.stopFlipping();
				final File imageFile = new File(Shock.PATH_IMAGE+"/"+Shock.listImageName.get((position-1)%number));  // 65 行++ 了 
				Resources res = getResources();
				new AlertDialog.Builder(Activity_ImageV.this)
					.setTitle(res.getString(R.string.delete))
					.setMessage(res.getString(R.string.question))
					.setNegativeButton(res.getString(R.string.no), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Shock.vSimple(getApplicationContext(), 60);
						}
					})
					.setPositiveButton(res.getString(R.string.yes), new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Shock.vSimple(getApplicationContext(), 60);
							imageFile.delete();
							Shock.listImageName.remove((position-1)%number); // 65 行++ 了 
							handler.sendEmptyMessage(0x222);
						}
					})
					.show();
			}
		});
	}
	@Override
	public void onResume(){
		super.onResume();
		sound_flag = Shock.vVoice;
		setSound(sound_flag);	
		color_string = Shock.vColor;
		setColor(color_string);
		re_layout.getBackground().setAlpha(80);
		re_layout.setVisibility(View.VISIBLE);
		
		viewFlipper.setFlipInterval(Shock.vTime*1000);
	}

	private void setSound(boolean flag){
		re_layout.setSoundEffectsEnabled(flag);
		fr_layout.setSoundEffectsEnabled(flag);
		image_back.setSoundEffectsEnabled(flag);
		autoPlay.setSoundEffectsEnabled(flag);
		textDelete.setSoundEffectsEnabled(flag);
		image_setting.setSoundEffectsEnabled(flag);
	}
	private void setColor(String string){
		if(string.equals("hui")){
			re_layout.setBackgroundColor(getResources().getColor(R.color.grey));
		}else if (string.equals("lan")) {
			re_layout.setBackgroundColor(getResources().getColor(R.color.blue));
		}else{
			re_layout.setBackgroundColor(getResources().getColor(R.color.pink));
		}
	}
	private Adapter getAdapter(){
		BaseAdapter adapter = new BaseAdapter(){
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ImageView imageView = new ImageView(Activity_ImageV.this);
				imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
				if(Shock.listImageName.size() == 0){
					Activity_ImageV.this.finish();
				}
				
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inJustDecodeBounds = true;
				options.inPreferredConfig = Bitmap.Config.RGB_565;
				options.inSampleSize = 2;
				options.inJustDecodeBounds = false;
				Bitmap bitmap = BitmapFactory.decodeFile(Shock.PATH_IMAGE+"/"+Shock.listImageName.get((
						Activity_ImageV.this.position)%number)  , options);
				imageView.setImageBitmap(bitmap);
				Activity_ImageV.this.position ++;
				imageView.setScaleType(ScaleType.FIT_CENTER);
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						return;
					}
				});
				return imageView;
			}
			@Override
			public long getItemId(int position) {
				return position;
			}
			@Override
			public Object getItem(int position) {
				return position;
			}
			@Override
			public int getCount() {
				return number;
			}
		};
		return adapter;
	}
	
	final Handler handler = new Handler(){
		@SuppressLint("HandlerLeak")
		public void handleMessage(Message msg){
			if(msg.what == 0x222){
				if(intent == null){
					intent = getIntent();
					intent.putExtra("FlagChang_image", true);
					Activity_ImageV.this.setResult(1916, intent);
					//以上没问题
					Shock.if_image_change = true;
				}
				Activity_ImageV.this.position --;
				number = Shock.listImageName.size();
				if(Shock.listImageName.size()==0){  
					Activity_ImageV.this.finish();
					overridePendingTransition(R.anim.left_in, R.anim.right_out);
				}else {
					viewFlipper.showNext();
				}
			}
		}
	};
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {  
	    if (e2.getX()-e1.getX() > 30) {  
	        // fling right  
	    	Activity_ImageV.this.position ++;
	        viewFlipper.showPrevious();
	    } else if (e1.getX() - e2.getX() > 30) {  
	        // fling left  
	        viewFlipper.showNext();  
	    }  
	    return true;  
	} 
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}
	@Override
	public void onShowPress(MotionEvent e) {
		
	}
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		setShowTitleBar();
		return false;
	}
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
		return false;
	}
	@Override
	public void onLongPress(MotionEvent e) {
		
	}
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return myDetector.onTouchEvent(event);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
            finish();  
            return true;  
        }  
        return super.onKeyDown(keyCode, event);  
    }
	//手势中的~~消除冲突~~~~~
	public boolean dispatchTouchEvent(MotionEvent event)
	{
		if(myDetector.onTouchEvent(event))
		{
			//在这里面 实现方法也不靠谱
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}
	//点击触发  onSingleTapUp()  这个方法在onSingleTapUp() 中使用
	public void setShowTitleBar(){
		if(viewFlipper.isFlipping()){
			viewFlipper.stopFlipping();	
			re_layout.setVisibility(View.VISIBLE);
		}else {
			if(re_layout.isShown())
				re_layout.setVisibility(View.GONE);
			else 
				re_layout.setVisibility(View.VISIBLE);
		}
	}
}









