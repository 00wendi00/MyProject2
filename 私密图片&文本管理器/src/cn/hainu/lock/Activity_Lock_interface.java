/**
 *文件名:MainActivity.java
 *项目-包:私密图片&文本管理器,cn.hainu.wendi
 *作者:张文迪
 *时间:2015年6月11日
 */
package cn.hainu.lock;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.hainu.utils.Shock;
import cn.hainu.wendi.Service_Initialization;
import cn.hainu.wendi.Activity_Main;
import cn.hainu.wendi.R;

/**
 * @author 张文迪
 * 类名:Lock_interface
 * 类说明: 应用锁(第一次进入程序 + 重置密码 + 锁屏就会出现锁界面)
 */
public class Activity_Lock_interface extends Activity {
	private Button into;
	private TextView textPasswdTips;
	private EditText editPasswdText;
	private Service_Initialization.MyBinder binder;
	private int flag_Reset_Passwd = 0;
	private boolean isReset = false;
	private String passwdTemp = "";
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (Service_Initialization.MyBinder)service;
		}
	};
	private void findViews(){
		into = (Button)findViewById(R.id.button_lock_into);
		textPasswdTips = (TextView)findViewById(R.id.textPassword);
		editPasswdText = (EditText)findViewById(R.id.editTextPassword);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password);
		
		final Intent intentService = new Intent();
		intentService.setPackage("cn.hainu.wendi");
		intentService.setAction("cn.hainu.wendi.BIND_SERVICE");
		bindService(intentService, conn, Service.BIND_AUTO_CREATE);
		
		findViews();
		
		final Resources res = getResources();
		editPasswdText.setFocusable(true);
		editPasswdText.setFocusableInTouchMode(true);
		editPasswdText.requestFocus();
		editPasswdText.requestFocusFromTouch();
		/*InputMethodManager inputMethodManager =(InputMethodManager)this.getApplicationContext().
				getSystemService(Context.INPUT_METHOD_SERVICE); 
		inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

		
		//是否是 重置密码
		Intent intent = getIntent();
		if(intent.getStringExtra("ResetPasswd") != null){
			if(intent.getStringExtra("ResetPasswd").equals("reset")){
				textPasswdTips.setText(res.getString(R.string.passwdOld));
				isReset = true;
			}
		}
		
		into.setOnClickListener(new OnClickListener() {
			@SuppressLint("ShowToast")
			@Override
			public void onClick(View arg0) {
				Shock.vSimple(getApplicationContext(), 60);
				if(isReset){			//	通过该setting重置
					switch (flag_Reset_Passwd) {
					case 0:
						if(Shock.lockString.equals(editPasswdText.getText().toString())){
							textPasswdTips.setText(res.getString(R.string.passwdNew));
							editPasswdText.setText("");
							flag_Reset_Passwd = 1;
						}
						else {
							textPasswdTips.setText(res.getString(R.string.passwdTrueOld));
							editPasswdText.setText("");
						}
						break;
						
					case 1:
						if(editPasswdText.getText().toString().length() < 4){		
							textPasswdTips.setText(res.getString(R.string.passwdLeast));
						}else {
							passwdTemp = editPasswdText.getText().toString();
							editPasswdText.setText("");
							textPasswdTips.setText(res.getString(R.string.passwdNewDetermine));
							flag_Reset_Passwd = 2;
						}
						break;
					case 2:
						if(!editPasswdText.getText().toString().equals(passwdTemp)){
							Toast.makeText(Activity_Lock_interface.this,res.getString(R.string.passwdCause), 1000).show();
							textPasswdTips.setText(res.getString(R.string.passwdNew));
							editPasswdText.setText("");
							passwdTemp = "";
							flag_Reset_Passwd = 1;
						}else {  // 3 次输入都符合要求 , 写入文件,这个activity finish
							Shock.lockString = passwdTemp ;
							SharedPreferences preferences = getSharedPreferences("MyPrefe", Application.MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences.edit();
							editor.putBoolean("isLocked", true);
							editor.putString("lockString", Shock.lockString);
							editor.commit();
							Toast.makeText(Activity_Lock_interface.this, res.getString(R.string.passwdResetSuccess), 1000).show();
							isReset = false;
							passwdTemp = "";
							Activity_Lock_interface.this.finish();
						}
						break;
						
					default:
						break;
					}
				}else if(!Shock.isLocked){  				//应用第一次启动的时候,进入这个
					if(Shock.lockString.equals("")){
						if(editPasswdText.getText().toString().length() < 4){		
							textPasswdTips.setText(res.getString(R.string.passwdLeast));
							Toast.makeText(Activity_Lock_interface.this, "11111111", 1000).show();
						}else {
							Shock.lockString = editPasswdText.getText().toString();
							textPasswdTips.setText(res.getString(R.string.passwdDetermine));
							editPasswdText.setText("");
							Toast.makeText(Activity_Lock_interface.this, "22222222", 1000).show();
						}
					}else {
						if(Shock.lockString.equals(editPasswdText.getText().toString())){
							startActivity(new Intent(Activity_Lock_interface.this, cn.hainu.wendi.Activity_Main.class));
							Activity_Lock_interface.this.finish();
							Shock.isLocked = true;
							//Shock.lockString = passwd;
							SharedPreferences preferences = getSharedPreferences("MyPrefe", Application.MODE_PRIVATE);
							SharedPreferences.Editor editor = preferences.edit();
							editor.putBoolean("isLocked", true);
							editor.putString("lockString", Shock.lockString);
							editor.commit();
							Activity_Lock_interface.this.finish();
						}else {
							Toast.makeText(Activity_Lock_interface.this, res.getString(R.string.passwdCause)+ "   ~~~    " , 1000).show();
							textPasswdTips.setText(res.getString(R.string.passwdAgainSet));
							editPasswdText.setText("");
							Shock.lockString = "";
						}
					}
				}else if(Shock.isLocked){		//应用不是第一次启动的时候,进入这个
					if(editPasswdText.getText().toString().length()<4){
						textPasswdTips.setText(res.getString(R.string.passwdLeast));
					}else if(!Shock.lockString.equals(editPasswdText.getText().toString())){
						Toast.makeText(Activity_Lock_interface.this, res.getString(R.string.passwdError),400).show();
						textPasswdTips.setText(res.getString(R.string.passwdAgain));
						editPasswdText.setText("");
					}else if(Shock.lockString.equals(editPasswdText.getText().toString())){
						//   这个判定很重要　：　不在入口锁屏界面下,弹出锁屏
						if(!Shock.isInLockedContent){
							startActivity(new Intent(Activity_Lock_interface.this, cn.hainu.wendi.Activity_Main.class));
						}
						Shock.isInLockedContent = false;
						Shock.isScreenOn = true;
						Shock.hasLockedContent = false;
						Activity_Lock_interface.this.finish();
					}
				}
			}
		});
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if(Shock.isInLockedContent){
				Activity_Main.mainActivity.finish();
				Shock.hasLockedContent = false;
				Activity_Lock_interface.this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		/*if(binder.getCount() == 0)
			unbindService(conn);*/
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(Shock.isInLockedContent){
			System.exit(0);
		}else {
			unbindService(conn);
		}
	}
}










