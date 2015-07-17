/**
 *�ļ���:ImageAdd.java
 *��Ŀ-��:˽��ͼƬ&�ı�������,cn.hainu.utils
 *����:���ĵ�
 *ʱ��:2015��6��23��
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
 * @author ���ĵ� 
 * ����:ImageAdd 
 * ��˵��: ����ͼƬ�� ����ָ�����ļ���~
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
	  * ��ȡ�ļ���׺��
     * @param fromFileString
     * @return �ļ���׺��
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
	 *            �����Ƶ��ļ�
	 * @param toFile
	 *            ���Ƶ�Ŀ¼�ļ�
	 * @param rewrite
	 *            �Ƿ����´����ļ�
	 *            <p>
	 *            �ļ��ĸ��Ʋ�������
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
			// �ر����롢�����
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
