/**
 *文件名:Addimage.java
 *项目-包:私密图片&文本管理器,cn.hainu.Images
 *作者:张文迪
 *时间:2015年6月17日
 */
package cn.hainu.Images;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.hainu.utils.Asy_loading;
import cn.hainu.utils.ImageAdd;
import cn.hainu.utils.Shock;
import cn.hainu.wendi.R;

/**
 * ++++   这个图片的加载方式 :　显示表面-->加载-->可点击(可选中 checkbox) , 没显示出来的 选不中checkbox
 * @author 张文迪 类名: Addimage 类说明:添加图片~
 */
public class Activity_Addimage extends Activity {
	private int position = 0;
	private boolean flag_folderChoosed = false;  //进入这个item之前 是否选中了这个item
	private LinearLayout ll1,ll2;
	private TextView textProgress_AddImage;
	private TextView text_ImageChoosed,add_Image;
	private ImageView back_addImage;
	private GridView gridView;
	private	RelativeLayout relativeLayout;
	private int width = Shock.width_screen / 3;
	private List<String> listImagePath = new ArrayList<String>();
	private List<String> listImagePath_hasChoosed = new ArrayList<String>();
	private List<Boolean> listImageFlag = new ArrayList<Boolean>();
	
	public static int number_checked = 0;
	
	private void findViews(){
		ll1 = (LinearLayout)findViewById(R.id.linear_AddImage1);
		ll2 = (LinearLayout)findViewById(R.id.linear_AddImage2);
		textProgress_AddImage = (TextView)findViewById(R.id.textProgress_AddImage);
		relativeLayout = (RelativeLayout)findViewById(R.id.relat_AddImage);
		text_ImageChoosed = (TextView) findViewById(R.id.chosed_AddImage);
		add_Image = (TextView)findViewById(R.id.add_AddImage);
		back_addImage = (ImageView) findViewById(R.id.back_AddImage);
		gridView = (GridView) findViewById(R.id.grid_AddImage);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addimage);
		Intent intent = getIntent();
		position =  intent.getExtras().getInt("Position");
		flag_folderChoosed = intent.getExtras().getBoolean("Flag");
		
		findViews();
		
		back_addImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				Activity_Addimage.this.finish();
				overridePendingTransition(R.anim.left_in, R.anim.right_out);
			}
		});
		

		listImagePath = Activity_Addfolder.listPathImages.get(position);
		if(flag_folderChoosed){
			number_checked = listImagePath.size();
			for(int i = 0; i<listImagePath.size();i++){
				listImageFlag.add(true);
			}
		}else {
			for(int i = 0; i<listImagePath.size();i++){
				listImageFlag.add(false);
			}
		}
		text_ImageChoosed.setText(number_checked+"");
		AddImageListAdapter adapter = new AddImageListAdapter(Activity_Addimage.this);
		gridView.setAdapter(adapter);
		
		add_Image.setOnClickListener(new OnClickListener() {
			@SuppressLint("SimpleDateFormat") @Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				
				
				if(number_checked>0){
					handler.sendEmptyMessage(0x202);

					//另一个线程~~ 处理图片复制
					new Thread(){
						public void run(){
							for(int i = 0;i<listImageFlag.size();i++){
								if(listImageFlag.get(i)){
									listImagePath_hasChoosed.add(listImagePath.get(i));
								}
							}
							//Toast.makeText(Activity_Addfolder.this,listHasChoosed+"~~~~~"+listHasChoosed.size(), 8000).show();
							ArrayList<String> toFiles = new ArrayList<String>();
							ImageAdd add = new ImageAdd();
							for(int i = 0 ; i<listImagePath_hasChoosed.size();i++){
								//handler.sendEmptyMessage(0x204);
								//设置Message
								Message msg = new Message();
								Bundle bundle = new Bundle();
								bundle.putInt("progressInt", i);
								msg.setData(bundle);
								msg.what = 0x204;
								handler.sendMessage(msg);
								toFiles.add(Shock.PATH_IMAGE+"/"+(Shock.number1++) + new SimpleDateFormat("."));  // ~~~~~~~~~~~~~~~~~~~~~~~~~~
								add.doCopy(listImagePath_hasChoosed.get(i), toFiles.get(i));
							}
							handler.sendEmptyMessage(0x203);
						}
					}.start();
				}
				
				
				
			}
		});
	}
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			switch (msg.what) {
			case 0x201:
				text_ImageChoosed.setText(number_checked+"");
				break;
				
			case 0x202://显示ll2,隐藏ll1
				ll2.setVisibility(View.VISIBLE);
				ll1.setVisibility(View.GONE);
				break;
				
			case 0x203://隐藏ll2,显示ll1  +  刷新choosed_addFolser 和 gridView
				Intent intent =  getIntent();
				Activity_Addimage.this.setResult(1200, intent);
				
				Toast.makeText(Activity_Addimage.this, "已成功添加"+number_checked+"张图片", Toast.LENGTH_SHORT).show();
				number_checked = 0;
				flag_folderChoosed = false;
				listImagePath_hasChoosed = new ArrayList<String>();
				for(int i = 0;i<listImagePath.size();i++){
					listImageFlag.set(position, false);
				}
				text_ImageChoosed.setText(number_checked+"");
						
				AddImageListAdapter adapter = new AddImageListAdapter(Activity_Addimage.this);
				gridView.setAdapter(adapter);
				//listView.requestLayout();  ~~~~~~~~~~~~~~然并卵
				ll1.setVisibility(View.VISIBLE);
				ll2.setVisibility(View.INVISIBLE);
				break;
				
			case 0x204://显示图片复制的过程
				int i = msg.getData().getInt("progressInt");
				textProgress_AddImage.setText(i+"/"+number_checked);
				break;
				
			case 0x205://Toast显示不返回~~
				Toast.makeText(Activity_Addimage.this, "正在复制图片,请勿返回", Toast.LENGTH_SHORT).show();
				break;

			default:
				break;
			}
		}
	};
	//设置声音
	private void setSound(boolean flag){
		back_addImage.setSoundEffectsEnabled(flag);
		text_ImageChoosed.setSoundEffectsEnabled(flag);
		add_Image.setSoundEffectsEnabled(flag);
		gridView.setSoundEffectsEnabled(flag);
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
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		number_checked = 0 ;
	}

	public class AddImageListAdapter extends BaseAdapter {
		
		private Context mContext = null;
		private LayoutInflater mLayoutInflater = null;
		private AddImageListHolder viewHolder;

		public class AddImageListHolder {
			public ImageView imageView;
			public CheckBox checkBox;
		}
		public AddImageListAdapter(Context context) {
			// viewHolder = new AddImageListHolder();
			mContext = context;
			mLayoutInflater = LayoutInflater.from(context);
			
		}
		@Override
		public int getCount() {
			return listImagePath.size();
		}
		@Override
		public Object getItem(int position) {
			return null;
		}
		@Override
		public long getItemId(int position) {
			return position;
		}
		@SuppressLint({ "ViewHolder", "InflateParams", "NewApi" })
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				viewHolder = new AddImageListHolder();
				convertView = mLayoutInflater.inflate(R.layout.addimage_list,null);
				viewHolder.checkBox = (CheckBox)convertView.findViewById(R.id.addImage_checkBox);
				AbsListView.LayoutParams lp0 = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, width);
				convertView.setLayoutParams(lp0);
				viewHolder.imageView = (ImageView) convertView.findViewById(R.id.addImage_image);
				
				viewHolder.checkBox.setChecked(flag_folderChoosed);

				convertView.setTag(viewHolder);
			} else {
				viewHolder = (AddImageListHolder) convertView.getTag();
			}
			viewHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					Shock.vSimple(getApplicationContext(), 60);
					if(isChecked){
						number_checked++;
						listImageFlag.set(position, true);
						handler.sendEmptyMessage(0x201);
					}else {
						number_checked--;
						listImageFlag.set(position, false);
						handler.sendEmptyMessage(0x201);
					}
				}
			});
			String url = listImagePath.get(position);
			new Asy_loading(url, viewHolder.imageView, position, listImagePath);
			return convertView;
		}
	}
}
