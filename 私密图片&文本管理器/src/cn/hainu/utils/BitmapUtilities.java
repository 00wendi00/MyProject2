/**
 *�ļ���:BitmapUtilities.java
 *��Ŀ-��:˽��˽��ͼƬ&�ı�������,cn.hainu.utils
 *����:���ĵ�
 *ʱ��:2015��5��4��
 */
package cn.hainu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author ���ĵ�
 * ����:BitmapUtilities
 * ��˵��: ����ಢû���õ�~~ ������Asy_loading����
 */
public class BitmapUtilities {

    public BitmapUtilities() {
            // TODO Auto-generated constructor stub
    }
    
    /*public static Bitmap getBitmapThumbnail(String path,int width,int height){
            Bitmap bitmap = null;
            //������԰�������СͼƬ��
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 4;//��͸߶���ԭ����1/4
            bitmap = BitmapFactory.decodeFile(path, opts); 
            
            ��һ���ģ�
                �������ǡ����inSampleSize�ǽ��������Ĺؼ�֮һ��BitmapFactory.Options�ṩ����һ����ԱinJustDecodeBounds��
               ����inJustDecodeBoundsΪtrue��decodeFile��������ռ䣬���ɼ����ԭʼͼƬ�ĳ��ȺͿ�ȣ���opts.width��opts.height��
               ������������������ͨ��һ�����㷨�����ɵõ�һ��ǡ����inSampleSize��
            BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts); 
        opts.inSampleSize = Math.max((int)(opts.outHeight / (float) height), (int)(opts.outWidth / (float) width));
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, opts);
        return bitmap;
    }*/
    
    public static Bitmap getBitmapThumbnail(Bitmap bmp,int reqWidth,int reqHeight,String url){
            Bitmap bitmap = null;
            if(bmp != null ){
                    int width = bmp.getWidth();
                    int height = bmp.getHeight();
                    if(width != 0 && height !=0){
                            /*Matrix matrix = new Matrix();
                            float scaleWidth = ((float) width / bmpWidth);
                            float scaleHeight = ((float) height / bmpHeight);
                            matrix.postScale(scaleWidth, scaleHeight);
                            bitmap = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight, matrix, true); */
                    	int inSampleSize = 1;
                        if (height > reqHeight || width > reqWidth) {
                            final int halfHeight = height / 2;
                            final int halfWidth = width / 2;
                            while ((halfHeight / inSampleSize) > reqHeight&& (halfWidth / inSampleSize) > reqWidth) {
                                inSampleSize *= 4;
                            }
                        }
                        BitmapFactory.Options options = new BitmapFactory.Options();
                        options.inSampleSize = inSampleSize;
                        bitmap = BitmapFactory.decodeFile(url, options);
                    }else{
                            bitmap = bmp;
                    }
            }
            return bitmap;
    }

}

