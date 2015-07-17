/**
 *文件名:Setting.java
 *项目-包:私密图片&文本管理器,cn.hainu.wendi
 *作者:张文迪
 *时间:2015年4月27日
 */
package cn.hainu.wendi;

import cn.hainu.utils.Shock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;

/**
 * @author 张文迪
 * 类名:Setting
 * 类说明:
 */
@SuppressLint("CommitPrefEdits")
public class Activity_Setting extends Activity
{
	private RelativeLayout relativeLayout;
	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private ImageView back_setting;
	private RadioGroup radioVoice,radioShock,radioColor,radioSize,radioTime;
	private RadioButton voiceOpButton,voiceClButton,shockOpButton,shockClButton,
		colorHuiButton,colorLanButton,colorLvButton,sizeXiaoButton,sizeZhongButton,
		sizeDaButton,time1Button,time2Button,time3Button;
	private Button button_reset_passwd;
	//设置声音~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~这个好像作用不大啊
	private void setSound(RadioButton[] views,boolean flag){
		for(int i = 0;i<views.length;i++){
			if(flag){
				views[i].setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
					}
				});	
			}
			else {
				views[i].setOnClickListener(null);
			}
		}
	}
	private void setColor(String string){
		int i = 0;
		if(string.equals("hui")){
			i = getResources().getColor(R.color.grey);
		}else if (string.equals("lan")) {
			i = getResources().getColor(R.color.blue);
		}else{
			i = getResources().getColor(R.color.pink);
		}
		relativeLayout.setBackgroundColor(i);
	}
	private void findViews(){
		relativeLayout = (RelativeLayout)findViewById(R.id.setting_relay);
		back_setting = (ImageView)findViewById(R.id.backSetting);
		radioVoice = (RadioGroup)findViewById(R.id.radioGroup1);
		radioShock = (RadioGroup)findViewById(R.id.radioGroup2);
		radioColor = (RadioGroup)findViewById(R.id.radioGroup3);
		radioSize = (RadioGroup)findViewById(R.id.radioGroup4);
		radioTime = (RadioGroup)findViewById(R.id.radioGroup5);
		
		voiceOpButton = (RadioButton)findViewById(R.id.openVoice);
		voiceClButton = (RadioButton)findViewById(R.id.closeVoice);
		shockOpButton = (RadioButton)findViewById(R.id.openShock);
		shockClButton = (RadioButton)findViewById(R.id.closeShock);
		colorHuiButton = (RadioButton)findViewById(R.id.buttonHui);
		colorLanButton = (RadioButton)findViewById(R.id.buttonLan);
		colorLvButton = (RadioButton)findViewById(R.id.buttonLv);
		sizeXiaoButton = (RadioButton)findViewById(R.id.buttonXiao);
		sizeZhongButton = (RadioButton)findViewById(R.id.buttonZhong);
		sizeDaButton = (RadioButton)findViewById(R.id.buttonDa);
		time1Button = (RadioButton)findViewById(R.id.buttonTime1);
		time2Button = (RadioButton)findViewById(R.id.buttonTime2);
		time3Button = (RadioButton)findViewById(R.id.buttonTime3);
		
		button_reset_passwd = (Button)findViewById(R.id.passwdReset);
	}
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);
		preferences = getSharedPreferences("MyPrefe", Context.MODE_PRIVATE);
		editor = preferences.edit();
		
		findViews();
		
		radioVoice.check(preferences.getBoolean("voiceState", true) == true ? R.id.openVoice : R.id.closeVoice);
		radioShock.check(preferences.getBoolean("shockState", false) == true ? R.id.openShock : R.id.closeShock );
		if(preferences.getString("colorState", "hui").equals("hui"))
			radioColor.check(R.id.buttonHui);
		else if(preferences.getString("colorState", "hui").equals("lan"))
			radioColor.check(R.id.buttonLan);
		else if(preferences.getString("colorState", "hui").equals("lv"))
				radioColor.check(R.id.buttonLv);
		
		if(preferences.getInt("sizeState", 17) == 14)
			radioSize.check(R.id.buttonXiao);
		else if (preferences.getInt("sizeState", 17) == 17) 
			radioSize.check(R.id.buttonZhong);
		else if (preferences.getInt("sizeState", 17) == 20) 
			radioSize.check(R.id.buttonDa);
		
		if(preferences.getInt("timeState", 2) ==2 ){
			radioTime.check(R.id.buttonTime1);
		}else if(preferences.getInt("timeState", 2) == 3){
			radioTime.check(R.id.buttonTime2);
		}else if(preferences.getInt("timeState", 2) == 4){
			radioTime.check(R.id.buttonTime3);
		}
	
		//设置声音  ,这个数组的初始化~ 一定要在 这些引用指向具体实例之后再 初始化 ,
		//不然, 就会出现引用指向为空  的错误
		final RadioButton[] views = {voiceOpButton,voiceClButton,shockOpButton,shockClButton,
				colorHuiButton,colorLanButton,colorLvButton,sizeXiaoButton,sizeZhongButton,
				sizeDaButton,time1Button,time2Button,time3Button}; 
		back_setting.setSoundEffectsEnabled(Shock.vVoice);
		button_reset_passwd.setSoundEffectsEnabled(Shock.vVoice);
		setSound(views, Shock.vVoice);
		
		back_setting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				Activity_Setting.this.finish();
				overridePendingTransition(R.anim.left_in, R.anim.right_out);
			}
		});
		button_reset_passwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				Intent intent = new Intent(Activity_Setting.this,cn.hainu.lock.Activity_Lock_interface.class);
				intent.putExtra("ResetPasswd", "reset");
				startActivity(intent);
			}
		});
		
		radioVoice.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				Shock.vSimple(getApplicationContext(), 60);
				if(checkedId == R.id.openVoice){
					Shock.vVoice = true;
					editor.putBoolean("voiceState", true);
				}else {
					Shock.vVoice = false;
					editor.putBoolean("voiceState", false);
				}
				editor.commit();
				//这一步 :刷新声音 设置  :  imageView +button_reset_passwd + radioButtons
				back_setting.setSoundEffectsEnabled(Shock.vVoice);
				button_reset_passwd.setSoundEffectsEnabled(Shock.vVoice);
				setSound(views, Shock.vVoice);
			}
		});
		radioShock.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.openShock) {
					Shock.vShock = true;
					editor.putBoolean("shockState", true);
					Shock.vSimple(getApplicationContext(), 60);
				}else {
					Shock.vShock = false;
					editor.putBoolean("shockState", false);		
					Shock.vSimple(getApplicationContext(), 60);
				}
				editor.commit();
			}
		});
		radioColor.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.buttonHui){
					Shock.vColor = "hui";
					editor.putString("colorState", "hui");
					Shock.vSimple(getApplicationContext(), 60);
				}else if (checkedId ==R.id.buttonLan) {
					Shock.vColor = "lan";
					editor.putString("colorState", "lan");
					Shock.vSimple(getApplicationContext(), 60);
				}else if (checkedId == R.id.buttonLv) {
					Shock.vColor = "lv";
					editor.putString("colorState", "lv");
					Shock.vSimple(getApplicationContext(), 60);
				}
				editor.commit();
				setColor(Shock.vColor);
			}
		});
		radioSize.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.buttonXiao){
					Shock.vSize = 14;
					editor.putInt("sizeState", 14);
					Shock.vSimple(getApplicationContext(), 60);
				}else if(checkedId == R.id.buttonZhong){
					Shock.vSize = 17;
					editor.putInt("sizeState", 17);
					Shock.vSimple(getApplicationContext(), 60);
				}else if(checkedId == R.id.buttonDa){
					Shock.vSize = 20;
					editor.putInt("sizeState", 20);
					Shock.vSimple(getApplicationContext(), 60);
				}
				editor.commit();
			}
		});
		radioTime.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.buttonTime1){
					Shock.vTime = 2;
					editor.putInt("timeState", 2);
					Shock.vSimple(getApplicationContext(), 60);
				}else if (checkedId == R.id.buttonTime2){
					Shock.vTime = 3;
					editor.putInt("timeState", 3);
					Shock.vSimple(getApplicationContext(), 60);
				}else if (checkedId == R.id.buttonTime3){
					Shock.vTime = 4;
					editor.putInt("timeState", 4);
					Shock.vSimple(getApplicationContext(), 60);
				}
				editor.commit();
			}
		});
	}
	//这个 menu 和 back 键的声音和震动 不好调
	/*public boolean onKeyDown(int keyCode,KeyEvent event){
		if (keyCode == KeyEvent.KEYCODE_BACK) {  
			Shock.vSimple(getApplicationContext(), 60);
			super.onKeyDown(keyCode, event);
			Setting.this.finish();
            return false;  
        } else {  
            return super.onKeyDown(keyCode, event);  
        }
	}*/
	public void onResume(){
		super.onResume();
		setColor(Shock.vColor);
		radioShock.requestLayout();
	}
	
	public void onDestroy()
	{
		super.onDestroy();
	}
}






