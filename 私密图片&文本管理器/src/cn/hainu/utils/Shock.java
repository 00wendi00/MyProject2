/**
 *�ļ���:Shock.java
 *��Ŀ-��:˽��ͼƬ&�ı�������,cn.hainu.utils
 *����:���ĵ�
 *ʱ��:2015��5��1��
 */
package cn.hainu.utils;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.os.Environment;
import android.os.Vibrator;

/**
 * @author ���ĵ�
 * ����:Shock
 * ��˵��:��~~���ݱ���
 */
public class Shock {
	public final static String PATH_TEXT = Environment.getExternalStorageDirectory()+"/ImageTextManager/text";
	public final static String PATH_IMAGE =Environment.getExternalStorageDirectory()+ "/ImageTextManager/image";
	
	public static boolean  if_image_change = false;
	
	public static boolean isScreenOn = true;
	public static boolean isInLockedContent = false;  //  �Ƿ���ʾ ����-->���� ���ж�
	public static boolean hasLockedContent = false;
	
	public static int  width_screen = 0;
	
	public static int number1 = 0,number2 = 0;
	//������ͼƬ,�ı��ĸ���
	
	public static List<String> listImageName = new ArrayList<String>();
	public static boolean vTab = true;
	public static boolean vVoice = true;
	public static boolean vShock = false;
	public static int vTime = 2;
	public static String vColor = "hui";
	public static int vSize = 0;
	public static boolean isLocked = false;  // �Ƿ��Ѿ���������,�Ƿ��һ�������ı�־
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
