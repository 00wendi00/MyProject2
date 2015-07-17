/**
 *文件名:MainActivity.java
 *项目-包:私密图片&文本管理器,cn.hainu.wendi
 *作者:张文迪
 *时间:2015年4月24日
 */
package cn.hainu.wendi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import cn.hainu.Images.Activity_Addfolder;
import cn.hainu.Images.Activity_ImageV;
import cn.hainu.lock.Activity_Lock_interface;
import cn.hainu.lock.ScreenObserver;
import cn.hainu.lock.ScreenObserver.ScreenStateListener;
import cn.hainu.texts.Activity_Text;
import cn.hainu.utils.Shock;

/**
 * @author 张文迪 类名:MainActivity 类说明:
 */
@SuppressLint("ShowToast")
public class Activity_Main extends Activity implements OnScrollListener {
	public static Activity_Main mainActivity;
	private ScreenObserver mScreenObserver;
	protected static boolean voice_checkBox_item = true;
	protected static HashMap<Integer, Boolean>  check2;
	protected static List<Boolean> check1;
	protected static int deleteNumber1 = 0, deleteNumber2 = 0;
	private boolean isExit, isDeleteState1 = false, isDeleteState2 = false;
	private final String STR11 = "####title&&&&", STR12 = "####title####",
			STR21 = "####time&&&&", STR22 = "####time####";
	private RelativeLayout reLayout;
	private ImageView mainBack;
	private TextView mainDelete;
	private TabHost tabHost;
	private TabWidget mTabWidget;
	private GridView grid1, grid2;
	private View v1, v2, vFlag;
	static int width = 0;
	private View guideImage, guideText;
	private String[] l0, s0, s1, s2, s3;
	private int i_flag = 1; // 设置颜色的时候
	Service_Initialization.MyBinder binder;
	private boolean flag_image_change = false;
	private static final String TAG = "MainActivity";
	// private TextView textview_show_prompt = null;
	private List<String> mList = null;
	// 图片缓存用来保存GridView中每个Item的图片，以便释放
	public static Map<String, Bitmap> gridviewBitmapCaches = new HashMap<String, Bitmap>();
	private MyGridViewAdapter adapter = null;

	// 写入tab信息到SharedPreference //切换tab没成功~~~
	/*
	 * private void setTab(boolean v){ if(Shock.vTab != v){
	 * SharedPreferences.Editor editor = getSharedPreferences("MyPrefe",
	 * Application.MODE_PRIVATE).edit(); editor.putBoolean("tabState", v);
	 * editor.commit(); } }
	 */
	// 设置声音
	private void setSound(boolean Flag) {
		tabHost.setSoundEffectsEnabled(Flag);
		v1.setSoundEffectsEnabled(Flag);
		v2.setSoundEffectsEnabled(Flag);
		grid1.setSoundEffectsEnabled(Flag);
		grid2.setSoundEffectsEnabled(Flag);
		mainBack.setSoundEffectsEnabled(Flag);
		mainDelete.setSoundEffectsEnabled(Flag);
		voice_checkBox_item = Flag;
	}

	private void setColor(String string) {
		int i = 0;
		if (string.equals("hui")) {
			i_flag = 1;
			i = getResources().getColor(R.color.grey);
			mTabWidget.setBackgroundColor(i);
			reLayout.setBackgroundColor(i);
		} else if (string.equals("lan")) {
			i_flag = 2;
			i = getResources().getColor(R.color.blue);
			mTabWidget.setBackgroundColor(i);
			reLayout.setBackgroundColor(i);
		} else {
			i_flag = 3;
			i = getResources().getColor(R.color.pink);
			mTabWidget.setBackgroundColor(i);
			reLayout.setBackgroundColor(i);
		}
	}

	// 绑定Service,获取配置信息~~
	private ServiceConnection conn = new ServiceConnection() {
		@Override
		public void onServiceDisconnected(ComponentName name) {

		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (Service_Initialization.MyBinder) service;
			if (binder.getCount() == 0)
				unbindService(conn);
		}
	};

	@Override
	protected void onStart() {
		super.onStart();

	};

	private void findViews() {
		reLayout = (RelativeLayout) findViewById(R.id.main_relay);
		mainBack = (ImageView) findViewById(R.id.mainBack);
		mainDelete = (TextView) findViewById(R.id.mainDelete);
		tabHost = (TabHost) findViewById(R.id.tableHost);
		grid1 = (GridView) findViewById(R.id.content1);
		grid2 = (GridView) findViewById(R.id.content2);
		guideImage = (View) LayoutInflater.from(this).inflate(
				R.layout.guide_image, null);
		guideText = (View) LayoutInflater.from(this).inflate(
				R.layout.guide_text, null);
	}

	@SuppressLint({ "InflateParams", "ShowToast", "ClickableViewAccessibility" })
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setWidth();// 设置屏幕宽度

		mainActivity = this;

		mScreenObserver = new ScreenObserver(this);
		mScreenObserver.requestScreenStateUpdate(new ScreenStateListener() {
			@Override
			public void onScreenOn() {
				Log.e(TAG,
						"-----------------------------------Screen is on----------------------------");
			}

			@Override
			public void onScreenOff() {
				Log.e(TAG,
						"-----------------------------------Screen is off----------------------------");
				Shock.isScreenOn = false;
				Shock.isInLockedContent = true;
				if (Shock.isInLockedContent) {
					if (!Shock.hasLockedContent) {
						Intent intent = new Intent(Activity_Main.this,
								Activity_Lock_interface.class);
						startActivity(intent);
					}
				}
			}
		});

		final Intent intentService = new Intent();
		intentService.setPackage("cn.hainu.wendi");
		intentService.setAction("cn.hainu.wendi.BIND_SERVICE");
		bindService(intentService, conn, Service.BIND_AUTO_CREATE);

		findViews();

		// 初始化TabWidget的过程~~
		tabHost.setup();
		mTabWidget = tabHost.getTabWidget();
		setColor(Shock.vColor); // 这个设置颜色的 写前面了~~
		TabSpec mTabSpec1 = tabHost.newTabSpec("tab1").setIndicator(guideImage)
				.setContent(R.id.content1);
		TabSpec mTabSpec2 = tabHost.newTabSpec("tab2").setIndicator(guideText)
				.setContent(R.id.content2);
		tabHost.addTab(mTabSpec1);
		tabHost.addTab(mTabSpec2);

		v1 = mTabWidget.getChildAt(0);
		v2 = mTabWidget.getChildAt(1);
		vFlag = v1;
		v1.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (vFlag == v) {
					Shock.vSimple(v.getContext(), 60);
					Intent intent = new Intent(Activity_Main.this,
							Activity_Addfolder.class);
					startActivityForResult(intent, 1100);
				}
				return false;
			}
		});
		v2.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (vFlag == v) {
					Shock.vSimple(v.getContext(), 60);
					Intent intent = new Intent(Activity_Main.this,
							Activity_Text.class);
					intent.putExtra("Num", -1);
					intent.putExtra("Total", Shock.number2);
					startActivity(intent);
				}
				return false;
			}
		});

		// Toast.makeText(MainActivity.this, "长按<图片>或者<文本>即可添加",
		// 1500).show();---------------------------------------------

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			public void onTabChanged(String tabId) {
				if (tabId.equals("tab1")) {
					vFlag = mTabWidget.getChildAt(0);
					Shock.vSimple(getApplicationContext(), 60);
				} else if (tabId.equals("tab2")) {
					vFlag = mTabWidget.getChildAt(1);
					Shock.vSimple(getApplicationContext(), 60);
				}
			}
		});

		grid1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 删除状态
				if (isDeleteState1) {
					handler.sendEmptyMessage(0x140);
				} else {
					Shock.vSimple(getApplicationContext(), 60);
					Intent intent = new Intent(Activity_Main.this,
							Activity_ImageV.class);
					intent.putExtra("Num1", (int) id);
					startActivityForResult(intent, 1916);
				}
			}
		});
		grid1.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!reLayout.isShown()) {
					Shock.vSimple(getApplicationContext(), 60);
					handler.sendEmptyMessage(0x125);
				}
				return true;
			}
		});
		grid2.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Shock.vSimple(getApplicationContext(), 60);
				Intent intent = new Intent(Activity_Main.this,
						Activity_Text.class);
				intent.putExtra("Num", (int) id);
				intent.putExtra("Total", Shock.number2);
				startActivityForResult(intent, 0x112);
			}
		});
		grid2.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				if (!reLayout.isShown()) {
					Shock.vSimple(getApplicationContext(), 60);
					handler.sendEmptyMessage(0x126);
				}
				return true;
			}
		});
		mainBack.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				if (isDeleteState1) {
					handler.sendEmptyMessage(0x131);
				}
				if (isDeleteState2) {
					handler.sendEmptyMessage(0x132);
				}
			}
		});
		mainDelete.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Resources res = getResources();
				Shock.vSimple(getApplicationContext(), 60);
				// 图片删除的 时候
				if (isDeleteState1) {
					if (deleteNumber1 > 0) { // 有点击...打钩的
						new AlertDialog.Builder(Activity_Main.this)
								.setTitle(res.getString(R.string.delete))
								.setMessage(res.getString(R.string.question))
								.setNegativeButton(res.getString(R.string.no),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												Shock.vSimple(
														getApplicationContext(),
														60);
											}
										})
								.setPositiveButton(res.getString(R.string.yes),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												Shock.vSimple(
														getApplicationContext(),
														60);
												for (int i = check1.size() - 1; i >= 0; i--) { // 这里必须是从大到小~~~~
																								// list.remove()
																								// ~~~~~~~~~~~~~~~~~~
													if (check1.get(i)) {
														File file = new File(
																Shock.PATH_IMAGE
																		+ "/"
																		+ Shock.listImageName
																				.get(i));
														file.delete();
														Shock.listImageName
																.remove(i);
													}
												}
												deleteNumber1 = 0;
												Shock.number1 = Shock.listImageName
														.size();
												handler.sendEmptyMessage(0x124);
											}
										}).show();
					}
				} else if (isDeleteState2) {
					if (deleteNumber2 > 0) {
						new AlertDialog.Builder(Activity_Main.this)
								.setTitle(res.getString(R.string.delete))
								.setMessage(res.getString(R.string.question))
								.setNegativeButton(res.getString(R.string.no),
										new DialogInterface.OnClickListener() {
											@Override
											public void onClick(
													DialogInterface dialog,
													int which) {
												Shock.vSimple(
														getApplicationContext(),
														60);
											}
										})
								.setPositiveButton(res.getString(R.string.yes),
										new DialogInterface.OnClickListener() {
											@SuppressLint("UseSparseArrays")
											public void onClick(
													DialogInterface dialog,
													int which) {
												Shock.vSimple(
														getApplicationContext(),
														60);
												for (int i = check2.size() - 1; i >= 0; i--) {
													if (check2.get(i)) {
														File file = new File(
																Shock.PATH_TEXT
																		+ "/"
																		+ i
																		+ ".bin");
														file.delete();
													}
												}
												// 给剩下的 Texts 改名字排序
												File fileTexts = new File(
														Shock.PATH_TEXT);
												for (int i = 0; i < fileTexts
														.list().length; i++) {
													File fileText = new File(
															Shock.PATH_TEXT
																	+ "/"
																	+ fileTexts
																			.list()[i]);
													fileText.renameTo(new File(
															Shock.PATH_TEXT
																	+ "/" + i
																	+ ".bin"));
												}
												deleteNumber2 = 0;
												Shock.number2 = fileTexts
														.list().length;
												check2 = new HashMap<Integer, Boolean>();
												for (int i = 0; i < Shock.number2; i++) {
													check2.put(i, false);
												}
												handler.sendEmptyMessage(0x123);
												Toast.makeText(
														Activity_Main.this,
														check2.toString()
																+ "//"
																+ Shock.number2,
														2000).show();
											}
										}).show();
					}
				}
			}
		});

		setSound(Shock.vVoice);

		initData();
		setAdapter();
		setStringS0123();
		Shock.listImageName = new ArrayList<String>();
		for (int i = 0; i < l0.length; i++) {
			Shock.listImageName.add(l0[i]);
		}
		grid2.setAdapter(getBaseAdapter_Text()); // 设置~~ 获得BaseAdapter

	}

	// 连续按2次 back退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// Shock.vSimple(getApplicationContext(), 60);
			if (isDeleteState1)
				handler.sendEmptyMessage(0x131);
			else if (isDeleteState2) {
				handler.sendEmptyMessage(0x132);
			} else
				exit();
			return false;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序",
					Toast.LENGTH_SHORT).show();
			handler.sendEmptyMessageDelayed(0x111, 1000);
		} else {
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			startActivity(intent);
			System.exit(0);
		}
	}

	// Menu菜单
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menus, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		// item.getActionView().setSoundEffectsEnabled(voiceFlag);
		// //~~~~~~~~~~~~~~~~~~行不通~~~这里也要设置一个
		if (id == R.id.action_settings1) {
			Shock.vSimple(getApplicationContext(), 60);
			Intent intent = new Intent(Activity_Main.this,
					Activity_Setting.class);
			startActivity(intent);
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			return true;
		} else if (id == R.id.action_settings2) {
			Shock.vSimple(getApplicationContext(), 60);
			System.exit(0);
			;
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// 若有删除 则跟新图片的 grid1 ????????????????????????????为什么这一步 没有走呢?
	// onActivityResult 回调机制呢 ? 111111111111
	// 解释 : 是不能用 singletask 内单例模式启动 .: 会把栈此 activity之上 的 activity全部移除...
	// 肯定找值(resultcode)了,不过没找到.
	@Override
	public void onActivityResult(int requstCode, int resultCode, Intent intent) {
		super.onActivityResult(requstCode, resultCode, intent);
		if (requstCode == 1916 && resultCode == 1916) {
			flag_image_change = intent.getExtras()
					.getBoolean("FlagChang_image");
			handler.sendEmptyMessageDelayed(0x124, 500);
		}
		if (requstCode == 1100 && resultCode == 1100) {
			handler.sendEmptyMessage(0x124);
		}
	}

	@Override
	public void onRestart() {
		super.onRestart();
		if (Shock.if_image_change) {
			handler.sendEmptyMessage(0x124);
			Shock.if_image_change = false;
		}
	};

	@SuppressLint({ "HandlerLeak", "UseSparseArrays" })
	final Handler handler = new Handler() {
		@SuppressLint("ShowToast")
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x123:// 文本grid2跟新
				setStringS0123();
				// getBaseAdapter().notifyDataSetChanged(); 观察者模式 ~~ 但是
				// 不靠谱!!!!!!!!!!!!!!!!!!!!!!!
				grid2.setAdapter(getBaseAdapter_Text()); // 设置~~ 获得BaseAdapter
				grid2.requestLayout();
				setSound(Shock.vVoice);
				break;

			case 0x124:// 图片grid1跟新
				initData();
				setAdapter();
				setStringS0123();
				Shock.listImageName = new ArrayList<String>();
				for (int i = 0; i < l0.length; i++) {
					Shock.listImageName.add(l0[i]);
				}
				flag_image_change = false;
				grid1.requestLayout();
				setSound(Shock.vVoice);
				break;

			case 0x125:// 把checkBox搞出来 grid1 更新
				check1 = new ArrayList<Boolean>(); // 在number1 赋值之后给
															// check赋值,
				for (int i = 0; i < Shock.number1; i++) { // 如果是第二次 整出来,那么是根据现在的
															// number1
					check1.add(false);
				}
				MyGridViewAdapter.isMulChoice = true;
				MyGridViewAdapter.checkBox_flag = true;
				isDeleteState1 = true;
				tabHost.setClickable(false); // 这个一定要写 让他不能点击 然后让后面的组件点击
				mTabWidget.setClickable(false); // 这个一定要写 让他不能点击 然后让后面的组件点击
				setAdapter();
				// 简单 的 移动 动画
				reLayout.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.right_in));
				mTabWidget.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.left_out));
				reLayout.setVisibility(View.VISIBLE);
				mTabWidget.setVisibility(View.INVISIBLE);
				break;

			case 0x126:// 把checkBox搞出来 grid2 更新
				check2 = new HashMap<Integer, Boolean>();
				for (int i = 0; i < Shock.number2; i++) {
					check2.put(i, false);
				}
				isDeleteState2 = true;
				tabHost.setClickable(false);
				mTabWidget.setClickable(false);
				reLayout.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.right_in));
				mTabWidget.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.left_out));
				reLayout.setVisibility(View.VISIBLE);
				mTabWidget.setVisibility(View.INVISIBLE);
				handler.sendEmptyMessage(0x123);
				break;

			case 0x111:// 两次back退出
				isExit = false;
				break;

			case 0x131:// 图片删除中:按箭头 或者 按back按键
				check1 = null;
				deleteNumber1 = 0;
				isDeleteState1 = false;
				MyGridViewAdapter.isMulChoice = false;
				MyGridViewAdapter.checkBox_flag = false;
				setAdapter();
				tabHost.setClickable(true);
				mTabWidget.setClickable(true);

				reLayout.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.right_out));
				mTabWidget.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.left_in));
				reLayout.setVisibility(View.GONE);
				mTabWidget.setVisibility(View.VISIBLE);
				break;

			case 0x132:// 文本删除中:按箭头 或者 按back按键
				check1 = null;
				deleteNumber2 = 0;
				isDeleteState2 = false;

				tabHost.setClickable(true);
				mTabWidget.setClickable(true);

				reLayout.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.right_out));
				mTabWidget.setAnimation(AnimationUtils.loadAnimation(
						Activity_Main.this, R.anim.left_in));
				reLayout.setVisibility(View.GONE);
				mTabWidget.setVisibility(View.VISIBLE);
				handler.sendEmptyMessage(0x123);
				break;

			case 0x140:
				reLayout.requestLayout();
				break;

			default:
				break;
			}
		}
	};

	public void onResume() {
		super.onResume();
		/*
		 * //!!!!!!!!!!!!!!!　 还得给个~~ 判定进去 ~~ 又 : 直接在 ischecked中判定最好~ //当在删除的时候
		 * 弹出去 或者不在 最上层了, 清空check1,check2,deleteNumber1,deleteNumber2......
		 * deleteNumber1 = 0; deleteNumber2 = 0; if(isDeleteState1){ for(int i =
		 * 0 ;i<check1.size();i++){ check1.put(i, false); } }
		 * if(isDeleteState2){ for(int i = 0 ;i<check2.size();i++){
		 * check2.put(i, false); } }
		 */
		// ????????????????????????
		// adapter.registerDataSetObserver(null);
		// 一条守护 进程, 刷新gridView
		grid2.setAdapter(getBaseAdapter_Text()); // 设置~~ 获得BaseAdapter
		grid2.requestLayout();
		setColor(Shock.vColor);
		// 刷新 文本的 grid2
		Timer timer1 = new Timer();
		timer1.schedule(new TimerTask() {
			int count = 0;

			@Override
			public void run() {
				while (count < 5) {
					handler.sendEmptyMessage(0x123);
					count++;
				}
			}
		}, 500, 200);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mScreenObserver.stopScreenStateUpdate();
	}

	// 设置~~获取屏幕宽度
	private void setWidth() {
		WindowManager manager = this.getWindowManager();
		DisplayMetrics outMetrics = new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(outMetrics);
		width = outMetrics.widthPixels;
		Shock.width_screen = width;
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private void initData() {
		/*
		 * mList = new ArrayList<String>(); String url = null; url =
		 * Environment.getExternalStorageDirectory() +"/ImageTextGridView";
		 * //为了方便测试，我们这里只存入一张图片，将其路径后面添加数字进行区分，到后面要获取图片时候再处理该路径。 File file = new
		 * File(url); String[] names = file.list(); for(int
		 * i=0;i<names.length;i++){ //mList.add(url+"/"+i);//区分路径
		 * mList.add(names[i]+"/"+i); } //for(int i=0;i<1000;i++){ //
		 * mList.add(url+"/"+i);//区分路径
		 */
		mList = new ArrayList<String>();
		// String urlllll =Environment.getExternalStorageDirectory()+
		// "/ImageTextManager/image";//为sd卡下面创建testGridView文件夹，将图片放入其中
		// 为了方便测试，我们这里只存入一张图片，将其路径后面添加数字进行区分，到后面要获取图片时候再处理该路径。

		try {
			File file = new File(Shock.PATH_IMAGE); // String[] names =
													// file.list();
			if (file.listFiles() == null) {
				Shock.number1 = 0;
				l0 = new String[0];
			} else {
				Shock.number1 = file.listFiles().length;
				l0 = file.list();
			}
			
			mList = new ArrayList<String>();
			for (int i = 0; i < Shock.number1; i++) {
				mList.add(file.getCanonicalPath() + "/" + l0[i]); //
			}
			Log.e(TAG, " ~~~~~~~~~~~~~:" + mList.size());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void setAdapter() {
		adapter = new MyGridViewAdapter(this, mList);
		grid1.setAdapter(adapter);
		grid1.setOnScrollListener(this);
	}

	// 释放图片的函数
	private void recycleBitmapCaches(int fromPosition, int toPosition) {
		Bitmap delBitmap = null;
		for (int del = fromPosition; del < toPosition; del++) {
			delBitmap = gridviewBitmapCaches.get(mList.get(del));
			if (delBitmap != null) {
				// 如果非空则表示有缓存的bitmap，需要清理
				//Log.e(TAG, "release position:" + del);
				// 从缓存中移除该del->bitmap的映射
				gridviewBitmapCaches.remove(mList.get(del));
				delBitmap.recycle();
				delBitmap = null;
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 注释：firstVisibleItem为第一个可见的Item的position，从0开始，随着拖动会改变
		// visibleItemCount为当前页面总共可见的Item的项数
		// totalItemCount为当前总共已经出现的Item的项数
		recycleBitmapCaches(0, firstVisibleItem);
		recycleBitmapCaches(firstVisibleItem + visibleItemCount, totalItemCount);

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	// 设置s1,s2,s3,s0; s0 目录表,s1是标题,s2是内容(一部分),s3时间
	private void setStringS0123() {
		try {
			File targetFile2 = new File(Shock.PATH_TEXT);
			Shock.number2 = targetFile2.listFiles().length;
			s0 = targetFile2.list();
			s1 = new String[Shock.number2];
			s2 = new String[Shock.number2];
			s3 = new String[Shock.number2];
			for (int i = 0; i < Shock.number2; i++) {
				File file = new File(Shock.PATH_TEXT + "/" + s0[i]);
				InputStream stream = new FileInputStream(file);
				BufferedReader brReader = new BufferedReader(
						new InputStreamReader(stream));
				StringBuilder builder = new StringBuilder("");
				for (int k = 0; k < 1; k++) {
					builder.append(brReader.readLine());
					if (brReader.readLine() == null) {
						break;
					}
				}
				String string = builder.toString();
				s1[i] = string.substring(string.indexOf(STR11) + 13,
						string.indexOf(STR12));
				s3[i] = string.substring(string.indexOf(STR21) + 12,
						string.indexOf(STR22));
				s2[i] = string.substring(string.indexOf(STR22) + 12,
						string.length());
				stream.close();
			}

			File targetFile1 = new File(Shock.PATH_IMAGE);
			Shock.number1 = targetFile1.listFiles().length;
			l0 = targetFile1.list();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 获得BaseAdapter
	@SuppressLint("ResourceAsColor")
	private BaseAdapter getBaseAdapter_Text() {
		BaseAdapter adapter = new BaseAdapter() {
			@Override
			public int getCount() {
				return Shock.number2;
			}

			@Override
			public Object getItem(int position) {
				return position;
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@SuppressLint({ "NewApi", "ViewHolder" })
			@Override
			public View getView(final int position, View convertView,ViewGroup parent) {
				LinearLayout ll = new LinearLayout(Activity_Main.this);
				ll.setDrawingCacheEnabled(false);
				ll.setLayoutParams(new AbsListView.LayoutParams(
						AbsListView.LayoutParams.MATCH_PARENT, width / 3));
				RelativeLayout rl = new RelativeLayout(Activity_Main.this);
				rl.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.MATCH_PARENT));
				ll.setBackgroundResource(R.drawable.textview_br1);
				ll.setPadding(6, 6, 6, 6);
				if (i_flag == 1)
					// rl.setBackground(res.getDrawable(R.drawable.textview_br_grey));
					rl.setBackgroundResource(R.drawable.textview_br_grey);
				else if (i_flag == 2)
					// rl.setBackground(res.getDrawable(R.drawable.textview_br_blue));
					rl.setBackgroundResource(R.drawable.textview_br_blue);
				else
					// rl.setBackground(res.getDrawable(R.drawable.textview_br_pink));
					rl.setBackgroundResource(R.drawable.textview_br_pink);
				rl.setPadding(10, 10, 10, 10);
				final CheckBox checkBox2 = new CheckBox(Activity_Main.this);
				TextView tv1 = new TextView(Activity_Main.this);
				TextView tv2 = new TextView(Activity_Main.this);
				TextView tv3 = new TextView(Activity_Main.this);
				tv1.setId(1);
				tv2.setId(2);
				tv3.setId(3);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT,
						RelativeLayout.LayoutParams.MATCH_PARENT);
				lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				checkBox2.setSoundEffectsEnabled(false);
				checkBox2.setLayoutParams(lp);
				checkBox2.setGravity(Gravity.TOP | Gravity.START);
				checkBox2.setVisibility(View.INVISIBLE);
				checkBox2.setClickable(false);
				if (isDeleteState2 == true) {
					checkBox2.setVisibility(View.VISIBLE);
					checkBox2.setClickable(true);
					for (int i = 0; i < check2.size(); i++) {
						if (check2.get(position))
							checkBox2.setChecked(true);
					}
					checkBox2.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Shock.vSimple(getApplicationContext(), 60);
							// 这个判定很重要~
							if (!checkBox2.isChecked()) {
								deleteNumber2--;
								check2.put(position, false);
							} else if (checkBox2.isChecked()) {
								deleteNumber2++;
								check2.put(position, true);
							}
						}
					});
				}
				lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
				tv1.getPaint().setFakeBoldText(true); // ???????????????????????????????????????
				Resources res = Activity_Main.this.getResources();
				if (s1[position].equals("")) {
					tv1.setText(res.getString(R.string.untitled));
					tv1.setTextSize(13);
				} else {
					tv1.setTextSize(17);
					tv1.setText(s1[position]);
				}
				tv1.setLayoutParams(lp);
				tv1.setSingleLine();
				tv1.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

				lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.BELOW, tv1.getId());
				tv2.setTextSize(14);
				tv2.setText(s2[position]);
				tv2.setLayoutParams(lp);
				tv2.setMaxLines(4);
				tv2.setEllipsize(TextUtils.TruncateAt.valueOf("END"));

				lp = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
						RelativeLayout.TRUE);
				lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
						RelativeLayout.TRUE);
				tv3.setTextSize(12);
				tv3.setLayoutParams(lp);
				tv3.setText(s3[position]);
				rl.addView(checkBox2);
				rl.addView(tv1);
				rl.addView(tv2);
				rl.addView(tv3);

				ll.addView(rl);
				return ll;
			}
		};
		return adapter;
	}

}
