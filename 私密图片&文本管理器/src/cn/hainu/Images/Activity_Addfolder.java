/**
 *文件名:Addfolder.java
 *项目-包:私密图片&文本管理器,cn.hainu.addImage
 *作者:张文迪
 *时间:2015年6月14日
 */
package cn.hainu.Images;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.hainu.utils.Asy_loading;
import cn.hainu.utils.ImageAdd;
import cn.hainu.utils.Shock;
import cn.hainu.wendi.R;

/**
 * @author 张文迪
 * 类名:Addfolder
 * 类说明:
 */
@SuppressLint("ViewHolder") public class Activity_Addfolder extends Activity
{
	protected static int hasChoosed = 0;
	private RelativeLayout relativeLayout;
	private LinearLayout ll1,ll2;
	private ImageView back_addFolder;
	private TextView choosed_addFolser;
	private TextView add_addFolder;
	private TextView textProgress_addFolder;
	private ListView listView;
	
	private List<String>  names = new ArrayList<String>();
	private List<String> paths = new ArrayList<String>();
	private ArrayList<Map<String, String>> lists = new ArrayList<Map<String,String>>();
	private List<ArrayList<String>> list_lists = new ArrayList<ArrayList<String>>();
	public List<String> listPath = new ArrayList<String>();  
	public List<Integer> listPathNumber = new ArrayList<Integer>();
	public List<String> listPathFirstImage = new ArrayList<String>();
	public List<Integer> listHasChoosed = new ArrayList<Integer>();
	private ArrayList<String> listHasChoosed_add = new ArrayList<String>();
	
	public static List<ArrayList<String>> listPathImages = new ArrayList<ArrayList<String>>();
	
	private void findViews(){
		relativeLayout = (RelativeLayout)findViewById(R.id.relat_AddFolder);
		ll1 = (LinearLayout)findViewById(R.id.linear_addFolder1);
		ll2 = (LinearLayout)findViewById(R.id.linear_addFolder2);
		back_addFolder = (ImageView)findViewById(R.id.back_AddFolder);
		choosed_addFolser = (TextView) findViewById(R.id.chosed_AddFolder);
		add_addFolder = (TextView) findViewById(R.id.add_AddFolder);
		textProgress_addFolder = (TextView)findViewById(R.id.textProgress_addFolder);
		listView = (ListView)findViewById(R.id.list_addFolder);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addfolder);
		
		findViews();
		
		choosed_addFolser.setText(hasChoosed+"");
		
		setLists();
		AddFolderListAdapter adapter = new AddFolderListAdapter(Activity_Addfolder.this);
		listView.setAdapter(adapter);
		//Toast.makeText(Addfolder.this, ""+lists.size(), 5000).show();------------------------------------------------------------
		
		back_addFolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(v.getContext(), 60);
				Activity_Addfolder.this.finish();
				overridePendingTransition(R.anim.left_in, R.anim.right_out);
			}
		});
		add_addFolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				if(hasChoosed>0){
					handler.sendEmptyMessage(0x202);

					//另一个线程~~ 处理图片复制
					new Thread(){
						public void run(){
							for(int i = 0; i<listHasChoosed.size(); i++){
								if(listHasChoosed.get(i) != 0){
									listHasChoosed_add.addAll(list_lists.get(i));
								}
							}
							//Toast.makeText(Activity_Addfolder.this,listHasChoosed+"~~~~~"+listHasChoosed.size(), 8000).show();
							ArrayList<String> toFiles = new ArrayList<String>();
							ImageAdd add = new ImageAdd();
							for(int i = 0 ; i<listHasChoosed_add.size();i++){
								//handler.sendEmptyMessage(0x204);
								//设置Message
								Message msg = new Message();
								Bundle bundle = new Bundle();
								bundle.putInt("progressInt", i);
								msg.setData(bundle);
								msg.what = 0x204;
								handler.sendMessage(msg);
								toFiles.add(Shock.PATH_IMAGE+"/"+(Shock.number1++));
								add.doCopy(listHasChoosed_add.get(i), toFiles.get(i));
							}
							handler.sendEmptyMessage(0x203);
						}
					}.start();
				}
			}
		});
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				Shock.vSimple(getApplicationContext(), 60);
				Intent intent = new Intent(Activity_Addfolder.this,Activity_Addimage.class);
				intent.putExtra("Position", position);
				if(listHasChoosed.get(position) != 0){
					intent.putExtra("Flag", true);
				}else {
					intent.putExtra("Flag", false);
				}
				startActivityForResult(intent, 1200);
			}
		});
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0x200:
				hasChoosed = 0;
				listHasChoosed_add = new ArrayList<String>();
				for (int i = 0;i<listHasChoosed.size();i++) {
					listHasChoosed.set(i, 0);
				}
				choosed_addFolser.setText(hasChoosed+"");
				AddFolderListAdapter adapter = new AddFolderListAdapter(Activity_Addfolder.this);
				listView.setAdapter(adapter);
				//listView.requestLayout();  ~~~~~~~~~~~~~~然并卵
				break;
				
			case 0x201://选一组,标题栏上面+++
				choosed_addFolser.setText(hasChoosed+"");
				break;
				
			case 0x202://显示ll2,隐藏ll1
				ll2.setVisibility(View.VISIBLE);
				ll1.setVisibility(View.GONE);
				break;
				
			case 0x203://隐藏ll2,显示ll1  +  刷新choosed_addFolser 和 gridView
				Intent intent =  getIntent();
				Activity_Addfolder.this.setResult(1100, intent);
				
				Toast.makeText(Activity_Addfolder.this, "已成功添加"+hasChoosed+"张图片", Toast.LENGTH_SHORT).show();
				handler.sendEmptyMessage(0x200);
				
				ll1.setVisibility(View.VISIBLE);
				ll2.setVisibility(View.INVISIBLE);
				break;
				
			case 0x204://显示图片复制的过程
				int i = msg.getData().getInt("progressInt");
				textProgress_addFolder.setText(i+"/"+hasChoosed);
				break;
				
			case 0x205://Toast显示不返回~~
				Toast.makeText(Activity_Addfolder.this, "正在复制图片,请勿返回", Toast.LENGTH_SHORT).show();
				break;
				
			default:
				break;
			}
		}
	};
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1200 && resultCode == 1200){
			handler.sendEmptyMessage(0x200);
			
			//Activity_Addimage 有改变的话 : MainActivity 也要刷新
			Intent intent =  getIntent();
			Activity_Addfolder.this.setResult(1100, intent);
		}
	}
	//复制图片的时候---->捕获back键--->提示不返回
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(ll2.isShown()){
			if(keyCode == KeyEvent.KEYCODE_BACK){
				handler.sendEmptyMessage(0x205);
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	//设置声音
	private void setSound(boolean flag){
		back_addFolder.setSoundEffectsEnabled(flag);
		choosed_addFolser.setSoundEffectsEnabled(flag);
		add_addFolder.setSoundEffectsEnabled(flag);
		listView.setSoundEffectsEnabled(flag);
	}
	//设置颜色
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
	@Override
	protected void onResume() {
		super.onResume();
		setSound(Shock.vVoice);
		setColor(Shock.vColor);
	};
	@Override
	protected void onDestroy() {
		super.onDestroy();
		hasChoosed = 0;
		listPathImages.clear();
	}
	private void  setLists(){
		names.clear();
		paths.clear();
		lists.clear();
		ImageAdd imageUtil = new ImageAdd();
		Cursor cursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
			//除去gif图
			if(!imageUtil.getFileType(name).equals("gif")){
				String string = cursor.getString(cursor.getColumnIndex(Media.DATA));
				names.add(name);
				Log.e("-----------", "~~~~~~~~~~~~~~~~~"+name);
				// 将图片保存路径添加到paths集合中
				paths.add(string.substring(0 , string.lastIndexOf("/")));   
			}
		}
		cursor.close();//关闭游标~~
		for(int i = 0 ;i<names.size();i++){
			Map<String , String> listItem = new HashMap<String, String>();
			listItem.put("name", names.get(i));
			listItem.put("path", paths.get(i));
			lists.add(listItem);
		}
		
		for(int i = 0 ;i< lists.size();i++){
			String pathString = lists.get(i).get("path");
			String nameString = lists.get(i).get("name");
			if(!pathString.equals("/storage/emulated/0/ImageTextManager/image")){ //   非本程序的文件夹
				if(!listPath.contains(pathString)){
					listPath.add(pathString);
					listPathNumber.add(1);
					listPathFirstImage.add(pathString+"/"+lists.get(i).get("name"));
					listHasChoosed.add(0);
					ArrayList<String> list_item_images = new ArrayList<String>();
					list_item_images.add(pathString+"/"+lists.get(i).get("name"));
					listPathImages.add(list_item_images);
					
					ArrayList<String> list_list = new ArrayList<String>();
					list_list.add(pathString+"/"+nameString);
					list_lists.add(list_list);
				}else {
					int k = listPath.indexOf(pathString);
					listPathNumber.set(k, listPathNumber.get(k)+1);
					listPathImages.get(k).add(pathString+"/"+lists.get(i).get("name"));
					list_lists.get(k).add(pathString+"/"+nameString);
					//listPathImages.set(k, listPathImages.get(k)); // 这里可能有问题
				}
			}
		}
		for(int i = 0; i<lists.size() ;i++){
			String pathString = lists.get(i).get("path");
			if(!pathString.equals("/storage/emulated/0/ImageTextManager/image")){//   非本程序的文件夹
				if(listPath.contains(pathString));
			}
		}
	}
	
	public class AddFolderListAdapter extends BaseAdapter
	{
		private Context mContext = null;
		private LayoutInflater mLayoutInflater = null;
		private AddFolderListHolder viewHolder;
		public List<Map<String , String>> lists = new ArrayList<Map<String,String>>();
		
		public class AddFolderListHolder{
			public ImageView imageView;
			public TextView text_name;
			public TextView text_number;
			public CheckBox checkBox;
		}
		public AddFolderListAdapter(Context context) {
			viewHolder = new AddFolderListHolder();
			mContext = context;

			mLayoutInflater = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			return listPath.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@SuppressLint("InflateParams")
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			convertView = mLayoutInflater.inflate(R.layout.addfolder_list, null);
			convertView.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, Shock.width_screen/4+10));
			viewHolder.imageView = (ImageView)convertView.findViewById(R.id.addFolder_image);
			viewHolder.text_name = (TextView)convertView.findViewById(R.id.addFolder_name);
			viewHolder.text_number = (TextView)convertView.findViewById(R.id.addFolder_number);
			viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.addFolder_checkbox);
			viewHolder.imageView.setLayoutParams(new RelativeLayout.LayoutParams(Shock.width_screen/4, Shock.width_screen/4));
			viewHolder.text_name.setTextSize(22);
			viewHolder.text_number.setTextSize(22);
			
			String s = listPath.get(position);
			s = s.substring(s.lastIndexOf("/"), s.length());
			viewHolder.text_name.setText(s);
			viewHolder.text_number.setText("共"+listPathNumber.get(position)+"张");
			
			if(listHasChoosed.get(position) == listPathNumber.get(position)){
				viewHolder.checkBox.setChecked(true);
			}else {
				viewHolder.checkBox.setChecked(false);
			}
			viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Shock.vSimple(getApplicationContext(), 60);
					if(isChecked){
						Activity_Addfolder.hasChoosed += listPathNumber.get(position);
						listHasChoosed.set(position, listPathNumber.get(position));
						handler.sendEmptyMessage(0x201);
					}else {
						Activity_Addfolder.hasChoosed -= listPathNumber.get(position);
						listHasChoosed.set(position, 0);
						handler.sendEmptyMessage(0x201);
					}
				}
			});
			String url = listPathFirstImage.get(position);
			//异步加载图片
			new Asy_loading(url, viewHolder.imageView,position,listPathFirstImage);
			return convertView;
		}
	}
}













