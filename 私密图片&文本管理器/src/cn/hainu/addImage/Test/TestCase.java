/**
 *�ļ���:TestCase.java
 *��Ŀ-��:˽��ͼƬ&�ı�������,cn.hainu.Test
 *����:���ĵ�
 *ʱ��:2015��6��15��
 */
package cn.hainu.addImage.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.test.AndroidTestCase;

/**
 * @author ���ĵ�
 * ����:TestCase
 * ��˵��:
 */
public class TestCase extends AndroidTestCase {
	public void test(){
		List<String>  names = new ArrayList<String>();
		List<String> paths = new ArrayList<String>();
		List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
		
		Cursor cursor = getContext().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
			byte[] data = cursor.getBlob(cursor.getColumnIndex(Media.DATA));//�����������ֽ������ֵ
			// ��ͼƬ����ӵ�names������
			names.add(name);
			// ��ͼƬ����·����ӵ�fileNames������
			paths.add(new String(data, 0, data.length - 1).replaceAll("/"+name, ""));  // ���replaceAll����
		}
		for(int i = 0 ;i<names.size();i++){
			Map<String , String> listItem = new HashMap<String, String>();
			listItem.put("name", names.get(i));
			listItem.put("path", paths.get(i));
			lists.add(listItem);
		}
		
//		AddFolderListAdapter adapter = new AddFolderListAdapter(getContext(),lists);
//		
//		Log.e("------listPath", adapter.listPath.toString());
//		Log.e("------listPathNumber", adapter.listPathNumber.toString());
//		Log.e("------listPath.size()", adapter.listPath.size()+"");
//		Log.e("------listPathFirstImage", adapter.listPathFirstImage.toString());
	}
}
