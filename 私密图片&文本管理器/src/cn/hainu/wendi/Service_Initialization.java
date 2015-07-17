/**
 *�ļ���:Initialization.java
 *��Ŀ-��:˽��ͼƬ&�ı�������,cn.hainu.wendi
 *����:���ĵ�
 *ʱ��:2015��5��1��
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
 * @author ���ĵ�
 * ����:Initialization
 * ��˵��:
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
					Toast.makeText(getApplicationContext(), "�޷����г��� : �޷���дsd��", 5000).show();
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
		
		//��һ�ε㿪,д��������Ϣ
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
		
		//��ȡ������Ϣ~~
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
