/**
 * PublcNumber
 * App.java 
 * 2015-1-6-上午10:26:00
 * 2015 万家恒通公司-版权所有
 * 
 */
package com.wearme.zfc.application;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.wearme.zfc.utils.CrashHandler;

/**
 * 
 * @description:管理整个应用程序的状态信息
 * author : liujw
 * modify : 
 * 2015-1-19 上午11:42:38
 *
 * 
 */
public class App extends android.app.Application {


	private List<Activity> activityList = new LinkedList<Activity>();
	
	private static Context mContext ; 

	/**
	 * mContext
	 *
	 * @return  the mContext
	 * @since   1.0.0
	 */
	
	public static Context getAppContext() {
		return mContext;
	}

	private static App mInstance;

	public App() {
		super();
	}
	
	public static App getInstance() {
		if (null == mInstance) {
			mInstance = new App();
		}
		return mInstance;
	}

	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	public void exit(){
	 for(Activity activity:activityList){
		 activity.finish();
	  }
	}
	
	private final static String TAG = "App" ;
	
	public void initImageLoader(Context context) {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		ImageLoader.getInstance().init(config);
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	/*	CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());*/
		
		mContext = getApplicationContext();
		initImageLoader(mContext);
		
	}
	
	public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
	 }

}
