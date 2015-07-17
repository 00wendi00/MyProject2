/**
 *文件名:TestCase.java
 *项目-包:私密图片&文本管理器,cn.hainu.Test
 *作者:张文迪
 *时间:2015年6月15日
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
 * @author 张文迪
 * 类名:TestCase
 * 类说明:
 */
public class TestCase extends AndroidTestCase {
	public void test(){
		List<String>  names = new ArrayList<String>();
		List<String> paths = new ArrayList<String>();
		List<Map<String, String>> lists = new ArrayList<Map<String,String>>();
		
		Cursor cursor = getContext().getContentResolver().query(Media.EXTERNAL_CONTENT_URI, null, null, null, null);
		while(cursor.moveToNext()){
			String name = cursor.getString(cursor.getColumnIndex(Media.DISPLAY_NAME));
			byte[] data = cursor.getBlob(cursor.getColumnIndex(Media.DATA));//返回请求列字节数组的值
			// 将图片名添加到names集合中
			names.add(name);
			// 将图片保存路径添加到fileNames集合中
			paths.add(new String(data, 0, data.length - 1).replaceAll("/"+name, ""));  // 这个replaceAll精髓
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
