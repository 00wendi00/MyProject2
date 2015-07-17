/**
 *文件名:ImageAdd.java
 *项目-包:私密图片&文本管理器,cn.hainu.utils
 *作者:张文迪
 *时间:2015年6月23日
 */
package cn.hainu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;

/**
 * @author 张文迪 
 * 类名:ImageAdd 
 * 类说明: 复制图片到 程序指定的文件夹~
 */
public class ImageAdd {
	/*String fromString, toString;
	//File fromFile, toFile;
	//ArrayList<String> lastNames;

	public ImageAdd() {
		//lastName = getFileType(fromFileString);
		//fromFile = new File(fromFileString);
		//toFile = new File(toFileString+"."+lastName);
	}*/
	public void doCopy(String  fromString, String toString){
			String lastName = getFileType(fromString);
			File fromFile = new File(fromString);
			File toFile = new File(toString+"."+lastName);
			copyfile(fromFile, toFile);
	}
	/** 
	  * 获取文件后缀名
     * @param fromFileString
     * @return 文件后缀名
     */
	@SuppressLint("DefaultLocale")
	public String getFileType(String fromFileString) {
		if (fromFileString != null) {
			String fileType = null;
			int typeIndex = fromFileString.lastIndexOf(".");
			if (typeIndex != -1) {
				fileType = fromFileString.substring(typeIndex + 1)
						.toLowerCase();
			}
			return fileType;
		}
		return "";
	}
	/**
	 * 
	 * @param fromFile
	 *            被复制的文件
	 * @param toFile
	 *            复制的目录文件
	 * @param rewrite
	 *            是否重新创建文件
	 *            <p>
	 *            文件的复制操作方法
	 */
	public void copyfile(File fromFile,File toFile) {

		if (!fromFile.exists()) {
			return;
		}

		if (!fromFile.isFile()) {
			return;
		}
		if (!fromFile.canRead()) {
			return;
		}
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		// if (toFile.exists() && rewrite) {
		// toFile.delete();
		// }

		try {
			FileInputStream fosfrom = new FileInputStream(fromFile);
			FileOutputStream fosto = new FileOutputStream(toFile);

			byte[] bt = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			// 关闭输入、输出流
			fosfrom.close();
			fosto.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean getResult() {
		return false;
	}
}
