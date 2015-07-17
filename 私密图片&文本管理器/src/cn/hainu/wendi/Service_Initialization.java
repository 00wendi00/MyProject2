/**
 *文件名:Initialization.java
 *项目-包:私密图片&文本管理器,cn.hainu.wendi
 *作者:张文迪
 *时间:2015年5月1日
 */
package cn.hainu.wendi;

import java.io.File;
import java.io.IOException;

import cn.hainu.utils.Shock;
import android.annotation.SuppressLint;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

/**
 * @author 张文迪
 * 类名:Initialization
 * 类说明:
 */
public class Service_Initialization extends Service
{
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	private int count;
	private MyBinder binder = new MyBinder();
	public class MyBinder extends Binder{
		@SuppressLint("ShowToast")
		public MyBinder(){
			try {
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
					File sdCardDirFile = Environment.getExternalStorageDirectory();
					File textFile1 = new File(sdCardDirFile.getCanonicalPath()+"/ImageTextManager/text");
					File textFile2 = new File(sdCardDirFile.getCanonicalPath()+"/ImageTextManager/image");
					if (!textFile1.exists()) {
						textFile1.mkdir();
					}else if (!textFile2.exists()) {
						textFile2.mkdir();
					}
				}else {
					Toast.makeText(getApplicationContext(), "无法运行程序 : 无法读写sd卡", 5000).show();
					System.exit(0);
				}
			}catch(IOException e)
			{
				e.printStackTrace();
			}
		count = 0;
		}
		public int getCount(){
			return count;
		}
	}
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		
		//第一次点开,写入配置信息
		preferences = getSharedPreferences("MyPrefe", Application.MODE_PRIVATE);
		editor = preferences.edit();
		
		if (!preferences.contains("tabState")) {
			editor.putBoolean("tabState", true);
			editor.commit();
		}else if(!preferences.contains("voiceState")){
			editor.putBoolean("voiceState", true);
			editor.commit();
		}else if (!preferences.contains("shockState")) {
			editor.putBoolean("shockState", false);
			editor.commit();
		}else if (!preferences.contains("colorState")) {
			editor.putString("colorState", "hui");
			editor.commit();
		}else if (!preferences.contains("sizeState")) {
			editor.putInt("sizeState", 16);
			editor.commit();
		}else if (!preferences.contains("timeState")) {
			editor.putInt("timeState", 2);
			editor.commit();
		}else if (!preferences.contains("isLocked")) {
			editor.putBoolean("isLocked", false);
			editor.commit();
		}else if (!preferences.contains("lockString")) {
			editor.putString("lockString", "");
			editor.commit();
		}
		
		//读取配置信息~~
		Shock.vTab = preferences.getBoolean("tabState", true);
		Shock.vVoice = preferences.getBoolean("voiceState", true);
		Shock.vShock = preferences.getBoolean("shockState", false);
		Shock.vColor = preferences.getString("colorState", "hui");
		Shock.vSize = preferences.getInt("sizeState", 17);
		Shock.vTime = preferences.getInt("timeState", 2);
		Shock.isLocked = preferences.getBoolean("isLocked", false);
		Shock.lockString = preferences.getString("lockString", "");
		
		return binder;
	}
	public boolean onUnbind(Intent intent){
		return false;
	}
}
