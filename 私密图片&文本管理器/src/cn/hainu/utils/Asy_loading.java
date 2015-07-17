/**
 *�ļ���:Asy_loading.java
 *��Ŀ-��:˽��ͼƬ&�ı�������,cn.hainu.utils
 *����:���ĵ�
 *ʱ��:2015��6��15��
 */
package cn.hainu.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;
import cn.hainu.wendi.Activity_Main;

/**
 * @author ���ĵ�
 * ����:Asy_loading
 * ��˵��:  �첽����ͼƬ : AbslistView������
 */
public class Asy_loading {
	private String image_url =""; 
	private ImageView imageView;
	private int position;
	private List<String> url_list = new ArrayList<String>();
	public Asy_loading(String url,ImageView imageView,int position,List<String> url_list){
		this.image_url = url;
		this.imageView = imageView;
		this.position = position;
		this.url_list = url_list;
		doIt();
	}
	private void doIt(){
		//����������ͨ��cancelPotentialLoad����ȥ�ж�imageview�Ƿ����߳�����Ϊ������ͼƬ��Դ��
        //������������ڼ��أ���ô�жϼ��ص����ͼƬ��Դ��url���Ƿ�����ڵ�ͼƬ��Դһ������һ����ȡ��֮ǰ���̣߳�֮ǰ�������߳����ϣ���
        //������cancelPotentialLoad��������
        if (cancelPotentialLoad(image_url, imageView)) {
        	AsyncLoadImageTask task = new AsyncLoadImageTask(imageView);
        	LoadedDrawable loadedDrawable = new LoadedDrawable(task);
        	imageView.setImageDrawable(loadedDrawable);
        	//viewHolder.imageview_thumbnail.setScaleType(ScaleType.CENTER_CROP);
        	task.execute(position);
        }
	}
	private static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
            inSampleSize *= 4;
        }
        return inSampleSize;
    }
    //������� �����װ�~~
    //����ǷŴ�ͼƬ��filter�����Ƿ�ƽ�����������СͼƬ��filter��Ӱ��
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { 
            src.recycle(); // �ͷ�Bitmap��native��������
        }
        return dst;
    }
    //��ȡbitmap ����ͼ
    private Bitmap getBitmapFromUrl(String url){
            /*Bitmap bitmap = null;
            bitmap = MainActivity.gridviewBitmapCaches.get(url);
            if(bitmap != null){
                    System.out.println(url);
                    return bitmap;
            }
            //url = url.substring(0, url.lastIndexOf("/"));//����֮ǰ��·�����֣�����·�����ԣ���ȡ����ͼƬ
            //bitmap = BitmapFactory.decodeFile(url);
            //�������ǲ���BitmapFactory.decodeFile(url)�������
            //��decodeFileDescriptor()����������bitmap���ʡ�ڴ�
            //�鿴Դ��Ա�һ�����ǻᷢ��decodeFile()���������������ķ�ʽ����bitmap
            //��decodeFileDescriptor()������ͨ��Native����decodeFileDescriptor����bitmap��
            try {
                    FileInputStream is = new FileInputStream(url);
                    bitmap = BitmapFactory.decodeFileDescriptor(is.getFD());
            } catch (FileNotFoundException e) {
                    e.printStackTrace();
            } catch (IOException e) {
                    e.printStackTrace();
            }      
            bitmap = BitmapUtilities.getBitmapThumbnail(bitmap,width,height,url);
            return bitmap;*/
    	final BitmapFactory.Options options = new BitmapFactory.Options();
    	options.inJustDecodeBounds = true;
    	BitmapFactory.decodeFile(url, options);
    	options.inPreferredConfig = Bitmap.Config.RGB_565;
    	options.inSampleSize = calculateInSampleSize(options, Shock.width_screen/4, Shock.width_screen/4);
    	options.inJustDecodeBounds = false;
    	Bitmap bitmap = BitmapFactory.decodeFile(url, options);
    	return bitmap;
    }

    //����ͼƬ���첽����        
    private class AsyncLoadImageTask extends AsyncTask<Integer, Void, Bitmap>{
            private String url = null;
            private final WeakReference<ImageView> imageViewReference;
            
            public AsyncLoadImageTask(ImageView imageview) {
                    super();
                    imageViewReference = new WeakReference<ImageView>(imageview);
            }

            @Override
            protected Bitmap doInBackground(Integer... params) {
                    Bitmap bitmap = null;
                    this.url = url_list.get(params[0]);                        
                    bitmap = getBitmapFromUrl(url);
                    Activity_Main.gridviewBitmapCaches.put(url_list.get(params[0]), bitmap);                        
                    return bitmap;
            }
            @Override
            protected void onPostExecute(Bitmap resultBitmap) {
                    if(isCancelled()){
                            resultBitmap = null;
                    }
                    if(imageViewReference != null){
                            ImageView imageview = imageViewReference.get();
                            AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
                        if (this == loadImageTask) {
                                imageview.setImageBitmap(resultBitmap);
                        }
                    }
                    super.onPostExecute(resultBitmap);
            }                                                        
    }
    private boolean cancelPotentialLoad(String url,ImageView imageview){
            AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);

        if (loadImageTask != null) {
            String bitmapUrl = loadImageTask.url;
            if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
                    loadImageTask.cancel(true);                        
            } else {
                // ��ͬ��url�Ѿ��ڼ�����.
                return false;
            }
        }
        return true;
    }
    //�� loadImageTask.cancel(true)��ִ�е�ʱ����AsyncLoadImageTask �ͻᱻȡ����
    //��AsyncLoadImageTask ����ִ�е�onPostExecute��ʱ��������������ص���ͼƬ��
    //��Ҳ������bitmap��Ϊnull�ˡ� 
    //getAsyncLoadImageTask�������£�
    private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageview){
            if (imageview != null) {
            Drawable drawable = imageview.getDrawable();
            if (drawable instanceof LoadedDrawable) {
                    LoadedDrawable loadedDrawable = (LoadedDrawable)drawable;
                return loadedDrawable.getLoadImageTask();
            }
        }
        return null;
    }
    //���๦��Ϊ����¼imageview����������Ϊimageview����Ĭ�ϵ�drawable
    public static class LoadedDrawable extends ColorDrawable{
            private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;
        public LoadedDrawable(AsyncLoadImageTask loadImageTask) {
            super(Color.TRANSPARENT);
            loadImageTaskReference =new WeakReference<AsyncLoadImageTask>(loadImageTask);
        }
        public AsyncLoadImageTask getLoadImageTask() {
            return loadImageTaskReference.get();
        }
    }
}
