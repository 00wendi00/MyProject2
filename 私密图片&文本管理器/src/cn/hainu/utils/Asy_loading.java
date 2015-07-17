/**
 *文件名:Asy_loading.java
 *项目-包:私密图片&文本管理器,cn.hainu.utils
 *作者:张文迪
 *时间:2015年6月15日
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
 * @author 张文迪
 * 类名:Asy_loading
 * 类说明:  异步加载图片 : AbslistView可重用
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
		//首先我们先通过cancelPotentialLoad方法去判断imageview是否有线程正在为它加载图片资源，
        //如果有现在正在加载，那么判断加载的这个图片资源（url）是否和现在的图片资源一样，不一样则取消之前的线程（之前的下载线程作废）。
        //见下面cancelPotentialLoad方法代码
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
    //这个方法 不靠谱啊~~
    //如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响
    private static Bitmap createScaleBitmap(Bitmap src, int dstWidth,int dstHeight) {
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { 
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }
    //获取bitmap 略缩图
    private Bitmap getBitmapFromUrl(String url){
            /*Bitmap bitmap = null;
            bitmap = MainActivity.gridviewBitmapCaches.get(url);
            if(bitmap != null){
                    System.out.println(url);
                    return bitmap;
            }
            //url = url.substring(0, url.lastIndexOf("/"));//处理之前的路径区分，否则路径不对，获取不到图片
            //bitmap = BitmapFactory.decodeFile(url);
            //这里我们不用BitmapFactory.decodeFile(url)这个方法
            //用decodeFileDescriptor()方法来生成bitmap会节省内存
            //查看源码对比一下我们会发现decodeFile()方法最终是以流的方式生成bitmap
            //而decodeFileDescriptor()方法是通过Native方法decodeFileDescriptor生成bitmap的
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

    //加载图片的异步任务        
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
                // 相同的url已经在加载中.
                return false;
            }
        }
        return true;
    }
    //当 loadImageTask.cancel(true)被执行的时候，则AsyncLoadImageTask 就会被取消，
    //当AsyncLoadImageTask 任务执行到onPostExecute的时候，如果这个任务加载到了图片，
    //它也会把这个bitmap设为null了。 
    //getAsyncLoadImageTask代码如下：
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
    //该类功能为：记录imageview加载任务并且为imageview设置默认的drawable
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
