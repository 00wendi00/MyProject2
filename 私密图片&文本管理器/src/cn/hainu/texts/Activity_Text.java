/**
 *�ļ���:Text.java
 *��Ŀ-��:˽��ͼƬ&�ı�������,cn.hainu.wendi
 *����:���ĵ�
 *ʱ��:2015��4��24��
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
 * @author ���ĵ�
 * ����:Text
 * ��˵��:
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
		
		editTitle.setFreezesText(true);//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~��֪������û��	
		//��������
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
		
		//���ǵ������   ������� �����,������һ����д��onCreate����
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

	//������ɫ
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
	//��������
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
		// �ı� �Ƿ��иı�~
		if(textString.equals(textStringBefore)&&titleString.equals(titleStringBefore)){
			kBool = true;
		}
		else {
			kBool = false;
		}
		//�ı� �Ƿ� �ĳɿ�
		if(textString.equals("")&&titleString.equals("")){
			nullBool = true ;
		}
		else {
			nullBool = false;
		}	
		write(textString,titleString);
		//Toast.makeText(Text.this, kBool+"~~~~~~", 5000).show();;
	    //Toast.makeText(Text.this, "д�����", 5000).show();
	}
	
	//�� ~~
	private String read(){
		try {
				File sdCardDir = Environment.getExternalStorageDirectory();
				//���ﻹ��Ҫ�ж� ��û������ļ���~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
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
	
	//д~~
	@SuppressLint("SimpleDateFormat")
	private void write(String textString,String titleString ){
		try {
				File sdCardDirFile = Environment.getExternalStorageDirectory();
				File textFile1 = new File(Shock.PATH_TEXT);
				if (!textFile1.exists()) {
					textFile1.mkdir();
				}
				//��ȡ ��׼ʱ��~~
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
				timeString = format.format(new Date());
				
				// �½����ı� + ����д��
				if(num==-1&&(   (!textString.equals(""))||(!titleString.equals(""))   )){
					num = 0;
				//������ �����ƶ�һ��,  �¼ӵ� �� 0.�� ��һ��λ��
					for(int i = total ;i > 0 ;i--){
						File fileEach = new File(Shock.PATH_TEXT+"/"+(i-1)+".bin");
						fileEach .renameTo( new File(Shock.PATH_TEXT+"/"+ i +".bin"));		
					//�������~~
					}
					File fileZeroFile = new File(Shock.PATH_TEXT+"/0.bin");
					fileZeroFile.delete();
					File textFile2 = new File(Shock.PATH_TEXT+"/"+num+".bin");
					RandomAccessFile raf = new RandomAccessFile(textFile2, "rw");
					//raf.seek(textFile2.length());
					raf.write((STR11+titleString+STR12+STR21+timeString+STR22+textString).getBytes());
					raf.close();
				}     //�½����ı� + ������д��
				else if (num==-1&&nullBool) {
				}     // ���ı� + û�� �ı�
				else if(num!= -1 && kBool ){		
					
				}     //���ı� + ��Ϊ ��   !!!!�� �ж����: �Ƿ��Ϊ��,�ٿ� ��һ��~�иı�
				else if (num!= -1 && nullBool) {
					File numFile = new File(Shock.PATH_TEXT+"/"+num+".bin"); 
					numFile.delete();
					for(int i = num;i<total;i++){
						File fileEach = new File(Shock.PATH_TEXT+"/"+(i+1)+".bin");
						fileEach.renameTo(new File(Shock.PATH_TEXT+"/"+ i +".bin"));
					}
				}
				//���ı� + �иı�
				else if (num!= -1 && !kBool) {
					 //��ɾ����ǰ��file
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








