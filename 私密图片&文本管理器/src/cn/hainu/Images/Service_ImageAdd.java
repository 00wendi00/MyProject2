/**
 *文件名:Service_ImageAdd.java
 *项目-包:私密图片&文本管理器,cn.hainu.Images
 *作者:张文迪
 *时间:2015年6月26日
 */
package cn.hainu.Images;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * @author 张文迪 类名:Service_ImageAdd 类说明: 服务.复制图片 ~~~~~~~~~~~~~待实现
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
