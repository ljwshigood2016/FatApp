package com.wearme.zfc.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wearme.zfc.R;
import com.wearme.zfc.blue.BluetoothLeService;

/**
 * @author liujw
 * @description:
 *
 */
public class BaseActivity extends FragmentActivity{

	public Context mContext ;
	
	protected ReConnectBroadcast mReConnectBroadcast ;
	
	private ProgressOneClick mProgressOneClick ;
	
	private class ReConnectBroadcast  extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String address = intent.getStringExtra("address");
		}
	}
	
	private class ProgressOneClick  extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
		}
	}

	protected DisplayImageOptions mOptionsStyle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		mOptionsStyle = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_launcher)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
		.cacheOnDisk(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		mProgressOneClick = new ProgressOneClick();
		
		registerOneClickBroadcast();
		
		mContext = BaseActivity.this ;
		mReConnectBroadcast = new ReConnectBroadcast();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		registerReconnectBroadcast();
		registerReceiver(mGattUpdateReceiver,makeGattUpdateIntentFilter());
	}
	
	
	private void registerReconnectBroadcast() {
		//IntentFilter filter = new IntentFilter();
		//filter.addAction(Constant.RECONNECT);
		//registerReceiver(mReConnectBroadcast, filter);
	};
	
	private void registerOneClickBroadcast() {
		//IntentFilter filter = new IntentFilter();
		//filter.addAction(Constant.ONECLICK);
		//registerReceiver(mProgressOneClick, filter);
	};

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mGattUpdateReceiver);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		return intentFilter;
	}

	private Handler mHandler = new Handler();
	
	private Intent mIntent ;
	
	Runnable mDisconnectRunnable = new Runnable() {
		
		@Override
		public void run() {
			
			if(mIntent == null){
				return ;
			}
			//progressDeviceDisconnect(mIntent);
		}
	};
	
	
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			mIntent = intent ;
			if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				mHandler.postDelayed(mDisconnectRunnable, 3000);
			} else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				mHandler.removeCallbacks(mDisconnectRunnable);
			}
		}
	};
	
}
