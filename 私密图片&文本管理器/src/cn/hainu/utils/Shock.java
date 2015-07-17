/**
 *文件名:Shock.java
 *项目-包:私密图片&文本管理器,cn.hainu.utils
 *作者:张文迪
 *时间:2015年5月1日
 */
package cn.hainu.utils;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;

/**
 * @author 张文迪
 * 类名:Shock
 * 类说明:震动~~数据保存
 */
public class Shock {
	public final static String PATH_TEXT = Environment.getExternalStorageDirectory()+"/ImageTextManager/text";
	public final static String PATH_IMAGE =Environment.getExternalStorageDirectory()+ "/ImageTextManager/image";
	
	public static boolean  if_image_change = false;
	
	public static boolean isScreenOn = true;
	public static boolean isInLockedContent = false;  //  是否显示 黑屏-->锁屏 的判定
	public static boolean hasLockedContent = false;
	
	public static int  width_screen = 0;
	
	public static int number1 = 0,number2 = 0;
	//程序中图片,文本的个数
	
	public static List<String> listImageName = new ArrayList<String>();
	public static boolean vTab = true;
	public static boolean vVoice = true;
	public static boolean vShock = false;
	public static int vTime = 2;
	public static String vColor = "hui";
	public static int vSize = 0;
	public static boolean isLocked = false;  // 是否已经设置密码,是否第一次启动的标志
	public static String lockString = "";
	private static Vibrator vibrator;
	public static void vSimple(Context context, int millisecond) {  
		if(vShock){
			vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);  
			vibrator.vibrate(millisecond);  
		}
    } 
	public static void stop() {  
        if (vibrator != null) {  
            vibrator.cancel();  
        }  
    }
}
