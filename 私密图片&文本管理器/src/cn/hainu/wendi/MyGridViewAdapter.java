/**
 *文件名:MyGridViewAdapter.java
 *项目-包:私密GridViewTest,com.example.gridviewtest
 *作者:张文迪
 *时间:2015年5月4日
 */
package cn.hainu.wendi;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import cn.hainu.utils.Asy_loading;
import cn.hainu.utils.Shock;

/**
 * @author 张文迪 类名:MyGridViewAdapter 类说明:
 */
@SuppressLint({ "InflateParams", "UseSparseArrays" })
public class MyGridViewAdapter extends BaseAdapter {
	protected static boolean isMulChoice = false; // 是否多选
	private Context mContext = null;
	private LayoutInflater mLayoutInflater = null;
	private List<String> mList = null;
	protected static boolean checkBox_flag = false;
	protected static boolean check_flag = false;
	protected static int k = 0;
	private MyGridViewHolder viewHolder = null;
	private int width = Shock.width_screen / 3;// 每个Item的宽度,可以根据实际情况修改

	public static class MyGridViewHolder {
		public RelativeLayout relativeLayout;
		public CheckBox checkBox;
		public ImageView imageview_thumbnail;
	}

	public MyGridViewAdapter(Context context, List<String> list) {
		this.mContext = context;
		this.mList = list;
		mLayoutInflater = LayoutInflater.from(context);
		/*
		 * for(int i = 0 ;i<list.size();i++){ check.put(i, false); }
		 */
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return 0;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			viewHolder = new MyGridViewHolder();
			convertView = mLayoutInflater.inflate(
					R.layout.layout_my_gridview_item, null);
			viewHolder.relativeLayout = (RelativeLayout) convertView
					.findViewById(R.id.relat_imageList);
			viewHolder.checkBox = (CheckBox) convertView
					.findViewById(R.id.check_box);
			viewHolder.imageview_thumbnail = (ImageView) convertView
					.findViewById(R.id.imageview_thumbnail);
			AbsListView.LayoutParams lp1 = new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, width);
			convertView.setLayoutParams(lp1);

			convertView.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					// if(event.getAction() == MotionEvent.ACTION_DOWN) 测试
					// Log.d("",
					// "----------------------------------!!!!!!!!!!!!!!convertview----------------------------"+position);
					return false;
				}
			});
			if (isMulChoice) {
				Log.e("--------",position+"~~~~~~~~~");
				viewHolder.checkBox.setChecked(Activity_Main.check1.get(position));
				viewHolder.checkBox.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
							if (Activity_Main.check1.get(position)) {
								Shock.vSimple(mContext, 60);
								viewHolder.checkBox.setChecked(false);
								Activity_Main.check1.set(position, false);
								Activity_Main.deleteNumber1--;
								// Log.d("",
								// "----------------------------------!!!!!!!!!!!!!!取消1---"+position);
							} else {
								Shock.vSimple(mContext, 60);
								viewHolder.checkBox.setChecked(true);
								Activity_Main.check1.set(position, true);
								Activity_Main.deleteNumber1++;
								// Log.d("",
								// "----------------------------------!!!!!!!!!!!!!!选择1---"+position);
							}
					}
				});
//				viewHolder.checkBox.setOnTouchListener(new OnTouchListener() {
//					@Override
//					public boolean onTouch(View v, MotionEvent event) {
//						
//						return false;
//					}
//				});
			}
			// viewHolder.checkBox.setEnabled(false);
			if (checkBox_flag) {
				viewHolder.checkBox.setVisibility(View.VISIBLE);
			}
			/*
			 * if(position == k) viewHolder.checkBox.setChecked(check_flag);
			 */
			check_flag = false;

			// ????????????????????这是个问题啊 ~ 在可选中状态下: 点击checkbox有声音的问题
			// viewHolder.relativeLayout.setSoundEffectsEnabled(MainActivity.voice_checkBox_item);
			// convertView.setSoundEffectsEnabled(MainActivity.voice_checkBox_item);
			// viewHolder.checkBox.setSoundEffectsEnabled(MainActivity.voice_checkBox_item);
			// viewHolder.imageview_thumbnail.setSoundEffectsEnabled(MainActivity.voice_checkBox_item);
			// viewHolder.textview_test =
			// (TextView)convertView.findViewById(R.id.textview_test);
			convertView.setTag(viewHolder); // ???????????????????????????????????????????
		} else {
			viewHolder = (MyGridViewHolder) convertView.getTag();
		}
		String url = mList.get(position);

		// 异步加载
		new Asy_loading(url, viewHolder.imageview_thumbnail, position, mList);
		return convertView;
	}
}
