/**
 *�ļ���:Service_ImageAdd.java
 *��Ŀ-��:˽��ͼƬ&�ı�������,cn.hainu.Images
 *����:���ĵ�
 *ʱ��:2015��6��26��
 */
package cn.hainu.Images;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * @author ���ĵ� ����:Service_ImageAdd ��˵��: ����.����ͼƬ ~~~~~~~~~~~~~��ʵ��
 */
public class Service_ImageAdd extends Service {
	private int count1 = 0;
	private int count2 = 0;
	private MyBinder binder = new MyBinder();

	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}

	public class MyBinder extends Binder {
		public int[] getCount() {
			return new int[]{count1,count2};
		}
	}

	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
		new Thread(){
			public void run(){
				while(count1<count2){
					try {
						
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
			}
		};
	}
}
