/**
 *文件名:Text.java
 *项目-包:私密图片&文本管理器,cn.hainu.wendi
 *作者:张文迪
 *时间:2015年4月24日
 */
package cn.hainu.texts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.hainu.utils.Shock;
import cn.hainu.wendi.R;
import cn.hainu.wendi.Activity_Setting;


/**
 * @author 张文迪
 * 类名:Text
 * 类说明:
 */
public class Activity_Text extends Activity
{
	private RelativeLayout relativeLayout;
	private final String STR11 = "####title&&&&",STR12 = "####title####",STR21 = "####time&&&&",STR22 = "####time####";
	private ImageView imageView;
	private TextView textView;
	private EditText editText,editTitle;
	private String textStringBefore = "";
	private String titleStringBefore = "";
	private String timeString = "";
	boolean kBool = true,nullBool = true;
	private int num = 0,total = 0;
	
	private void findViews() {
		setContentView(R.layout.text_view);
		relativeLayout = (RelativeLayout)findViewById(R.id.text_relay);
		textView = (TextView)findViewById(R.id.shezhi);
		imageView = (ImageView)findViewById(R.id.backText);
		editText = (EditText)findViewById(R.id.editView);
		editTitle = (EditText)findViewById(R.id.editTitle);	
	}
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		findViews();
		
		editTitle.setFreezesText(true);//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~不知道有用没有	
		//设置声音
		setSound(Shock.vVoice);
		editText.setTextSize(Shock.vSize);
			
		textView.setFocusable(true);
		textView.setFocusableInTouchMode(true);
		textView.requestFocus();	

	    textView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				Intent intent = new Intent(Activity_Text.this,Activity_Setting.class);
				startActivity(intent);
				overridePendingTransition(R.anim.right_in,R.anim.left_out);
			}
		});
		
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Shock.vSimple(getApplicationContext(), 60);
				Activity_Text.this.finish();
				overridePendingTransition(R.anim.left_in, R.anim.right_out);
			}
		});
		
		//考虑到会存在   点击设置 的情况,所以这一部分写在onCreate里面
		Intent intent = getIntent();
		num = intent.getExtras().getInt("Num");
	    if(num !=-1){
	    	textStringBefore = read();
	    	int i1 = textStringBefore.indexOf(STR12);
	    	titleStringBefore = textStringBefore.substring(13,i1);
	    	editTitle.setText(titleStringBefore);
	    	
	    	int i2 = textStringBefore.indexOf(STR22);
	    	textStringBefore = textStringBefore.substring(i2+12, textStringBefore.length());
	    	editText.setText(textStringBefore);
		}
	    total = intent.getExtras().getInt("Total");
	}

	//设置颜色
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void setColor(String string){
		int i= 0;
		if(string.equals("hui")){
			i = getResources().getColor(R.color.grey);
			relativeLayout.setBackgroundColor(i);
			editTitle.setBackground(getResources().getDrawable(R.drawable.textview_br_grey));
		}else if (string.equals("lan")) {
			i = getResources().getColor(R.color.blue);
			relativeLayout.setBackgroundColor(i);
			editTitle.setBackground(getResources().getDrawable(R.drawable.textview_br_blue));
		}else{
			i = getResources().getColor(R.color.pink);
			relativeLayout.setBackgroundColor(i);
			editTitle.setBackground(getResources().getDrawable(R.drawable.textview_br_pink));
		}
	}
	//设置声音
	private void setSound(boolean flag){
		imageView.setSoundEffectsEnabled(flag);
		textView.setSoundEffectsEnabled(flag);
	}
	protected void onStart() {
		super.onStart();	
		
	};
	protected void onResume(){
		super.onResume();
		setSound(Shock.vVoice);
		setColor(Shock.vColor);
		editText.setTextSize(Shock.vSize);
	}
	protected void onPause() {
		super.onPause();
		
	};
	@Override
	public void onDestroy(){
		super.onDestroy();
		String textString = editText.getText().toString();
		String titleString = editTitle.getText().toString();
		// 文本 是否有改变~
		if(textString.equals(textStringBefore)&&titleString.equals(titleStringBefore)){
			kBool = true;
		}
		else {
			kBool = false;
		}
		//文本 是否 改成空
		if(textString.equals("")&&titleString.equals("")){
			nullBool = true ;
		}
		else {
			nullBool = false;
		}	
		write(textString,titleString);
		//Toast.makeText(Text.this, kBool+"~~~~~~", 5000).show();;
	    //Toast.makeText(Text.this, "写入完毕", 5000).show();
	}
	
	//读 ~~
	private String read(){
		try {
				File sdCardDir = Environment.getExternalStorageDirectory();
				//这里还需要判定 有没有这个文件夹~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
				FileInputStream fis = new FileInputStream(sdCardDir.getCanonicalPath()+"/ImageTextManager/text"+"/"+num+".bin");
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				StringBuilder sb = new StringBuilder("");
				String line = null;
				while((line = br.readLine()) != null){			
					sb.append(line);
				}
				br.close();								
				return sb.toString();
		} catch (Exception e) {
			
		}
		return null;
	}
	
	//写~~
	@SuppressLint("SimpleDateFormat")
	private void write(String textString,String titleString ){
		try {
				File sdCardDirFile = Environment.getExternalStorageDirectory();
				File textFile1 = new File(Shock.PATH_TEXT);
				if (!textFile1.exists()) {
					textFile1.mkdir();
				}
				//获取 标准时间~~
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				timeString = format.format(new Date());
				
				// 新建的文本 + 存在写入
				if(num==-1&&(   (!textString.equals(""))||(!titleString.equals(""))   )){
					num = 0;
				//集体向 后面移动一个,  新加的 给 0.在 第一个位置
					for(int i = total ;i > 0 ;i--){
						File fileEach = new File(Shock.PATH_TEXT+"/"+(i-1)+".bin");
						fileEach .renameTo( new File(Shock.PATH_TEXT+"/"+ i +".bin"));		
					//这个方法~~
					}
					File fileZeroFile = new File(Shock.PATH_TEXT+"/0.bin");
					fileZeroFile.delete();
					File textFile2 = new File(Shock.PATH_TEXT+"/"+num+".bin");
					RandomAccessFile raf = new RandomAccessFile(textFile2, "rw");
					//raf.seek(textFile2.length());
					raf.write((STR11+titleString+STR12+STR21+timeString+STR22+textString).getBytes());
					raf.close();
				}     //新建的文本 + 不存在写入
				else if (num==-1&&nullBool) {
				}     // 老文本 + 没有 改变
				else if(num!= -1 && kBool ){		
					
				}     //老文本 + 改为 空   !!!!先 判定这个: 是否改为空,再看 下一步~有改变
				else if (num!= -1 && nullBool) {
					File numFile = new File(Shock.PATH_TEXT+"/"+num+".bin"); 
					numFile.delete();
					for(int i = num;i<total;i++){
						File fileEach = new File(Shock.PATH_TEXT+"/"+(i+1)+".bin");
						fileEach.renameTo(new File(Shock.PATH_TEXT+"/"+ i +".bin"));
					}
				}
				//老文本 + 有改变
				else if (num!= -1 && !kBool) {
					 //先删除当前的file
					File numFile = new File(Shock.PATH_TEXT+"/"+num+".bin"); 
					numFile.delete();
					for(int i = num;i>0;i--)
					{
						File fileEach = new File(Shock.PATH_TEXT+"/"+(i-1)+".bin");
						fileEach.renameTo(new File(Shock.PATH_TEXT+"/"+i+".bin"));
					}
					File fileZeroFile = new File(Shock.PATH_TEXT+"/0.bin");
					OutputStream outputStream = new FileOutputStream(fileZeroFile);
					OutputStreamWriter out = new OutputStreamWriter(outputStream);
					/*out.write("", 0, textStringBefore.length());
					out.flush();*/
					out.write(STR11+titleString+STR12+STR21+timeString+STR22+textString);
					out.close();
				} 
				else {
					//Toast.makeText(Text.this, "####################", 5000).show();
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}








