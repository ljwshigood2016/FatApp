/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wearme.zfc.ui;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu.OnClosedListener;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.wearme.zfc.R;
import com.wearme.zfc.adapter.LeftMenuAdapter;
import com.wearme.zfc.adapter.LeftMenuAdapter.InvisiableMenu;
import com.wearme.zfc.adapter.ViewPagerAdapter;
import com.wearme.zfc.bean.User;
import com.wearme.zfc.bean.WeightResult;
import com.wearme.zfc.blue.BluetoothLeService;
import com.wearme.zfc.blue.GattAttributes;
import com.wearme.zfc.db.DatabaseManager;
import com.wearme.zfc.listener.ShakeListener;
import com.wearme.zfc.listener.ShakeListener.OnShakeListener;
import com.wearme.zfc.utils.DateUtils;
import com.wearme.zfc.utils.FormatUtils;
import com.wearme.zfc.widget.CommonDialog;

/**
 * Activity for scanning and displaying available Bluetooth LE devices.
 * 
 */
public class MainActivity extends SlidingFragmentActivity implements OnPageChangeListener,OnClickListener,InvisiableMenu{
	

	private BluetoothAdapter mBluetoothAdapter;

	private boolean mScanning;

	private Handler mHandler;

	private static final int REQUEST_ENABLE_BT = 1;

	// Stops scanning after 10 seconds.
	private static final long SCAN_PERIOD = 10000;

	private ViewPager mViewPaper ;
	
	private ArrayList<View> mViewList ;
	
	private ViewPagerAdapter mViewPaperAdapter ;
	
	private SlidingMenu slidingMenu;
	
	private void initSlideMenuView() {
		initLeftMenu();
		slidingMenu = getSlidingMenu();
		slidingMenu.setMenu(left_view);
		slidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		slidingMenu.setFadeDegree(0.1f);
		slidingMenu.setBackgroundColor(Color.WHITE);
		slidingMenu.setBehindScrollScale(0f);
		slidingMenu.setMode(SlidingMenu.LEFT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		slidingMenu.setOnClosedListener(new OnClosedListener() {

			@Override
			public void onClosed() {
				
			}
		});
	}
	
	private View left_view ;
	
	private ListView mLvLeft ;
	
	private LeftMenuAdapter mLeftMenuAdapter ;
	
	private void initLeftMenu() {
		mLeftMenuAdapter = new LeftMenuAdapter(mContext,this);
		left_view = mInflater.inflate(R.layout.layout_left_menu, null);
		mLvLeft = (ListView)left_view.findViewById(R.id.lv_left_menu);
		mLvLeft.setAdapter(mLeftMenuAdapter);
		mLvLeft.setOnItemClickListener(mLeftMenuAdapter);
	}
	
	private LayoutInflater mInflater ;
	
	
	private void initViewPager() {
		mViewPaper = (ViewPager) findViewById(R.id.vp);
		mViewList = new ArrayList<View>();
		mViewList.clear();
		for(int i = 0 ;i < mUserList.size() ;i++){
			mViewList.add(mInflater.inflate(R.layout.layout_user_info, null));	
			mCurrentUser = mUserList.get(0);
		}
		mViewList.add(mInflater.inflate(R.layout.layout_register, null));
		
		mViewPaperAdapter = new ViewPagerAdapter(mContext,mViewList,mUserList) ; 
		mViewPaperAdapter.setmOptionsStyle(mOptionsStyle);
		mViewPaper.setAdapter(mViewPaperAdapter);
		mViewPaperAdapter.setmInvisiableMenu(this);
		mViewPaper.setCurrentItem(mCurrentPosition);
		mViewPaper.setOnPageChangeListener(this);
	}
	
	private BluetoothLeService mBluetoothLeService;

    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
            if (!mBluetoothLeService.initialize()) {
                finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
	
    private  IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    
    private Context mContext ;
    
    private ShakeListener mShakeListener;
	
	private void initShackListener() {
		mShakeListener = new ShakeListener(mContext);
		mShakeListener.setOnShakeListener(shakeListener);
	}
	
    
    OnShakeListener shakeListener = new OnShakeListener() {

		@Override
		public void onShake() {
			CommonDialog.getInstance(mContext).showDialog(mContext,0,null);
			scanLeDevice(true);
		}
	};
	
	private DatabaseManager mDataBaseManager ;
	
	private List<User> mUserList ;
	
	private void initUserDataList(){
		mUserList = mDataBaseManager.selectUserList();
		if(mUserList.size() != 0){
			mCurrentWeightResult = mDataBaseManager.selectWeightByUserId(mUserList.get(0).getUid());
		}
	}
	
	private DisplayImageOptions mOptionsStyle ;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.test);
		mContext = MainActivity.this ;
		
		mDataBaseManager = DatabaseManager.getInstance(mContext);
		
		mInflater = getLayoutInflater();
		
		initUserDataList();
		initViewPager();
		initSlideMenuView();
		initShackListener();
		
		mOptionsStyle = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_launcher)
			.showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true)
			.cacheOnDisk(true).considerExifParams(true)
			.bitmapConfig(Bitmap.Config.RGB_565).build();
		
		mHandler = new Handler();
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
		}

		final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);

		mBluetoothAdapter = bluetoothManager.getAdapter();

		if (mBluetoothAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported,Toast.LENGTH_SHORT).show();
			finish();
			return;
		}
		
		Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        getApplicationContext().bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
        
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!mBluetoothAdapter.isEnabled()) {
			if (!mBluetoothAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		
		mShakeListener.start();
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
			finish();
			return;
		}else if(requestCode == 54){
			initUserDataList();
			initViewPager();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onPause() {
		super.onPause();
		scanLeDevice(false);
		mShakeListener.stop();
		unregisterReceiver(mGattUpdateReceiver);
	}
	
	private boolean isFirst = true;
	
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			Log.v("MainActivity","####################runnable : "+isFirst);
			if(isFirst){
				String message = getUserDetailMessage(mCurrentUser);	
				Log.v("MainActivity","####################message : "+message);
				mBluetoothLeService.sendMessage(message);
				isFirst = false ;
			}
		}
	};
	
	
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
	        @Override
	        public void onReceive(Context context, Intent intent) {
	            final String action = intent.getAction();
	            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
	            	
	            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
	            	mViewPaperAdapter.notifyViewPaperConnect(false);
	            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
	            	Log.e("MainActivity","############################displayGattServices : ");
	            	displayGattServices(mBluetoothLeService.getSupportedGattServices());
	            	CommonDialog.getInstance(mContext).dismissDialog();
	            	scanLeDevice(false);
	            	mViewPaperAdapter.notifyViewPaperConnect(true);
	                
	            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
	            	byte[] msg = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
	            	if(isFirst){
		             	parseDataAvalidate(msg);
		            	mHandler.postDelayed(runnable, 2000);
	            	}else{
	            		parseUserDetailInfo(msg);
	            	}
	            }
	        }
    };
	
    
    
    private String getUserDetailMessage(User user){
    	//10 00 00 15 a0
    	//10 00 01 90 a3
    	//100001800ac
    	//10 00 00 19 a3
    	// 000000000224ffffffffffffffffffffffffff00
    	String message = "";
    	int gender = user.getGender();
    	int age = user.getAge();
    	float height = user.getHeight();
    	
    	String hexGender = Integer.toHexString(gender);
    	String hexAge = Integer.toHexString(age);
    	String hexHeight = Integer.toHexString((int)height);
    	
    	if(hexGender.length() == 1){
    		if(hexGender.equals("1")){
    			hexGender = "01";
    		}else{
    			hexGender = "00";		
    		}
    	}
    	
    	if(hexAge.length() == 1){
    		hexAge = "0" + hexAge;
    	}
    	
    	if(hexHeight.length() == 1){
    		hexHeight = "0" + hexHeight;
    	}
    	
    	message = "1000" + hexGender + hexAge + hexHeight;
    	return message ;
    }
    
    private void parseDataAvalidate(byte[] data){
    	
    	if(mCurrentUser == null){
    		return ;
    	}
    	
    	String message = FormatUtils.byte2hex(data) ;
    	String weight = message.substring(8, 12);
    	int DecimWeight = Integer.parseInt(weight,16);
    	float floatWeight = (float)DecimWeight / (float)10 ;
    	
    	if(mCurrentUser != null){
    		mCurrentUser.setWeight(Float.valueOf(floatWeight));
    		mCurrentWeightResult.setWeight(Float.valueOf(floatWeight));
    	}
    	
    	mViewPaperAdapter.notifyDataSetChanged();
    	
    	Log.v("MainActivity","###########################BluetoothLeService.ACTION_DATA_AVAILABLE " + message);
    }
    
    private WeightResult mCurrentWeightResult ;
    
    private void parseUserDetailInfo(byte[] data){
    	
    	isFirst = true ;
    	
    	if(mCurrentWeightResult == null){
    		return  ;
    	}
    	
    	String message = FormatUtils.byte2hex(data) ;
    	
    	String reg=".*ff.*";  
    	boolean flag = message.matches(reg);
    	
        if(!flag){
        	
    		String fat = message.substring(12, 16);
    		String water = message.substring(16, 20);
    		String bone = message.substring(20, 24);
    		String Muscle = message.substring(24, 28);
    		String VisceralFat = message.substring(28, 30);
    		String Calorie = message.substring(30, 34);
    		String BMI = message.substring(34, 38);
    		
    		float floatFat = Float.valueOf(Integer.parseInt(fat, 16)) / (float)10 ;
    		float floatWater = Float.valueOf(Integer.parseInt(water,16)) / (float)10 ;
    		float floatBone = Float.valueOf(Integer.parseInt(bone,16)) / (float)10 ;
    		float floatMuscle = Float.valueOf(Integer.parseInt(Muscle,16)) / (float)10 ;
    		float floatVisceralFat = Float.valueOf(Integer.parseInt(VisceralFat,16)) ;
    		int floatCalorie = Integer.parseInt(Calorie,16);
    		float floatBMI = Float.valueOf( Integer.parseInt(BMI,16)) / (float)10 ;

    		String date = DateUtils.getFormatDate(System.currentTimeMillis());
        	String[] dateArray = date.split("-");
        	
        	if(mCurrentWeightResult.getOrg() == 1){
        		
        		mCurrentWeightResult.setUid(mCurrentUser.getUid());
        		mCurrentWeightResult.setWeight(mCurrentUser.getWeight());
        		mCurrentWeightResult.setFatContent(floatFat);
        		mCurrentWeightResult.setWaterContent(floatWater);
        		mCurrentWeightResult.setBoneContent(floatBone);
        		mCurrentWeightResult.setMuscleContent(floatMuscle);
        		mCurrentWeightResult.setVisceralFatContent(floatVisceralFat);
        		mCurrentWeightResult.setCalorie(floatCalorie);
        		mCurrentWeightResult.setBmi(floatBMI);
        		mCurrentWeightResult.setOrg(0);
        		
        		mCurrentWeightResult.setYear(Integer.valueOf(dateArray[0]));
        		
        		mCurrentWeightResult.setMonth(Integer.valueOf(dateArray[1]));
	    		mCurrentWeightResult.setDay(Integer.valueOf(dateArray[2]));
        		
	    		mCurrentWeightResult.setRecordDate(date);
        		
        	}else{
        		
	    		float proiorFatContent = mCurrentWeightResult.getFatContent();
	    		float avgFatContent  = (proiorFatContent + floatFat) / 2 ;
	    				
	    		mCurrentWeightResult.setFatContent(avgFatContent);
	    		
	    		float proiorWaterContent = mCurrentWeightResult.getWaterContent();
	    		float avgWaterContent  = (proiorWaterContent + floatWater) / 2 ;
	    		mCurrentWeightResult.setWaterContent(avgWaterContent);
	    		
	    		float proiorBoneContent = mCurrentWeightResult.getBoneContent();
	    		float avgBoneContent  = (proiorBoneContent + floatBone) / 2 ;
	    		
	    		mCurrentWeightResult.setBoneContent(avgBoneContent);
	    		
	    		float proiorMuscleContent = mCurrentWeightResult.getMuscleContent();
	    		float avgMuscleContent  = (proiorMuscleContent + floatMuscle) / 2 ;
	    		mCurrentWeightResult.setMuscleContent(avgMuscleContent);
	    		
	    		float proiorVisceralFatContent = mCurrentWeightResult.getVisceralFatContent();
	    		float avgVisceralFatContent  = (proiorVisceralFatContent + floatVisceralFat) / 2 ;
	    		
	    		mCurrentWeightResult.setVisceralFatContent(avgVisceralFatContent);
	    		
	    		int proiorCalorie = mCurrentWeightResult.getCalorie();
	    		int avgproiorCalorie = (proiorCalorie + floatCalorie) / 2 ;
	    		mCurrentWeightResult.setCalorie(avgproiorCalorie);
	    		
	    		
	    		float proiorBmi = mCurrentWeightResult.getBmi();
	    		float avgBmi = (proiorBmi + floatBMI) / 2 ;
	    		mCurrentWeightResult.setBmi(avgBmi);
	    		
	    		mCurrentWeightResult.setYear(Integer.valueOf(dateArray[0]));
	    		mCurrentWeightResult.setMonth(Integer.valueOf(dateArray[1]));
	    		mCurrentWeightResult.setDay(Integer.valueOf(dateArray[2]));
	    		
	    		mCurrentWeightResult.setRecordDate(date);
	    		mCurrentWeightResult.setUid(mCurrentUser.getUid());
        	}
        	
    	}	
        
        if(mDataBaseManager.isInsertRecordWeight(mCurrentWeightResult,mCurrentUser)){
        	mDataBaseManager.insertWeightResult(mCurrentWeightResult,mCurrentUser.getUid());
        	Toast.makeText(mContext, "isInsertRecordWeight", 1).show() ;
        }else{
        	mDataBaseManager.updateWeightResult(mCurrentWeightResult);
        }
        
        mViewPaperAdapter.notifyDataSetChanged();
    	
    	Log.v("MainActivity","###########################parseUserDetailInfo " + message);
    	
    }
    
	
	private void displayGattServices(List<BluetoothGattService> gattServices) {
		if (gattServices == null)
			return;
		for (BluetoothGattService gattService : gattServices) {
			//f433bd80
			//06aba6de
			if (gattService.getUuid().toString().startsWith("f433bd80")) {
				List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
				for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
					if (gattCharacteristic.getUuid().toString().startsWith("29f11080")) {
						
						GattAttributes.mobileToEquipmentCharacteristic = gattCharacteristic;
						
					}else if (gattCharacteristic.getUuid().toString().startsWith("1a2ea400")) {
						mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
					}
				}
			} 
			
			if (gattService.getUuid().toString().startsWith("06aba6de")) {
				List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
				for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
					if (gattCharacteristic.getUuid().toString().startsWith("29f11080")) {
						
						GattAttributes.mobileToEquipmentCharacteristic = gattCharacteristic;
						
					}else if (gattCharacteristic.getUuid().toString().startsWith("1a2ea400")) {
						mBluetoothLeService.setCharacteristicNotification(gattCharacteristic, true);
					}
				}
			} 
		}
	}
	
	
	private void scanLeDevice(final boolean enable) {
		if (enable) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBluetoothAdapter.stopLeScan(mLeScanCallback);
					CommonDialog.getInstance(mContext).dismissDialog();
				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBluetoothAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBluetoothAdapter.stopLeScan(mLeScanCallback);
		}
		invalidateOptionsMenu();
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {

		@Override
		public void onLeScan(final BluetoothDevice device, int rssi,byte[] scanRecord) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if(device != null && device.getName() != null){
						if(device.getName().startsWith("VScale") || device.getName().startsWith("WEARME")){
							Log.v("MainActivity","#####################device.getAddress() : "+device.getName());
							mBluetoothLeService.connect(device.getAddress());	
						}
							
					}
				}
			});
		}
	};
	
	protected void onDestroy() {
		super.onDestroy();
		CommonDialog.getInstance(mContext).dismissDialog();
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	private User mCurrentUser ;
	
	private int mCurrentPosition = 0 ;
	
	@Override
	public void onPageSelected(int position) {
		mCurrentPosition = position ;
		if(position <= mUserList.size() - 1){
			mCurrentUser = mUserList.get(position);
			mCurrentWeightResult = mDataBaseManager.selectWeightByUserId(mUserList.get(position).getUid());
		}
	}
	
	public void setSliceMenuVisiable(){
		this.getSlidingMenu().showMenu();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_menu:
			setSliceMenuVisiable();
			break;

		default:
			break;
		}
	}


	@Override
	public void invisiableMenu() {
		setSliceMenuVisiable();
	}


	@Override
	public void visiableMene() {
		this.getSlidingMenu().showContent();
	}

}
