package com.wearme.zfc.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wearme.zfc.R;
import com.wearme.zfc.bean.User;
import com.wearme.zfc.bean.WeightResult;
import com.wearme.zfc.db.DatabaseManager;
import com.wearme.zfc.utils.CalulateTools;
import com.wearme.zfc.utils.DateUtils;
import com.wearme.zfc.widget.LineView;
import com.wearme.zfc.widget.RoundImageView;

public class ChartActivity extends BaseActivity implements OnClickListener {

	private TextView mTvNomalData;

	private TextView mTvYourData;

	private TextView mTvYouTip;

	private LineView mLineView;

	private ImageView mIvLeft;

	private ImageView mIvRight;

	private RoundImageView mIvPhoto;

	private TextView mTvMainInfo;

	private TextView mTvDanWeiValue;

	private TextView mTvBack;

	private void initView() {
		mHVScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		mTvBack = (TextView) findViewById(R.id.tv_back);
		mTvDanWeiValue = (TextView) findViewById(R.id.tv_danwei_value);
		mTvMainInfo = (TextView) findViewById(R.id.tv_main_info);
		mTvName = (TextView) findViewById(R.id.tv_name);
		mIvPhoto = (RoundImageView) findViewById(R.id.iv_photo);
		mTvNian = (TextView) findViewById(R.id.tv_nian);
		mTvMonth = (TextView) findViewById(R.id.tv_yue);

		mIvRight = (ImageView) findViewById(R.id.iv_date_right);
		mIvLeft = (ImageView) findViewById(R.id.iv_date_left);
		mTvDateValue = (TextView) findViewById(R.id.tv_value);
		mLineView = (LineView) findViewById(R.id.line_view);
		mTvNomalData = (TextView) findViewById(R.id.tv_nomal_data);
		mTvYourData = (TextView) findViewById(R.id.tv_your_data);
		mTvYouTip = (TextView) findViewById(R.id.tv_give_tip);

		mIvLeft.setOnClickListener(this);
		mIvRight.setOnClickListener(this);
		mTvNian.setOnClickListener(this);
		mTvBack.setOnClickListener(this);
		mTvMonth.setOnClickListener(this);
	}

	private String mPrefenceValue;

	private void initDataInfoByType(int type) {
		switch (type) {
		case 0:
			mTvMainInfo.setText(mContext.getString(R.string.fat_value));
			mTvDanWeiValue.setText("%");
			break;
		case 1:
			mTvMainInfo.setText(mContext.getString(R.string.calorie_value));
			mTvDanWeiValue.setText("Cal");
			break;
		case 2:
			mTvMainInfo.setText(mContext.getString(R.string.water_value));
			mTvDanWeiValue.setText("%");
			break;
		case 3:
			mTvMainInfo.setText(mContext.getString(R.string.muscle_value));
			mTvDanWeiValue.setText("%");
			break;
		case 4:
			mTvMainInfo.setText(mContext.getString(R.string.visceral_fat_value));
			mTvDanWeiValue.setText(mContext.getString(R.string.visceral_fat_percent_grade));
			break;
		case 5:
			mTvMainInfo.setText(mContext.getString(R.string.bone_value));
			mTvDanWeiValue.setText("KG");
			break;
		case 6:
			mTvMainInfo.setText(mContext.getString(R.string.target_weight));
			mTvDanWeiValue.setText("KG");
			break;
		case 7:
			mTvMainInfo.setText("BMI");
			mTvDanWeiValue.setText("BMI");
			break;
		default:
			break;
		}

		mTvNomalData.setText(CalulateTools.setNomalData(mCurrentUser, type));
		mPrefenceValue = CalulateTools.setNomalData(mCurrentUser, type);
		mTvYourData.setText(mData);
		mTvYouTip.setText(CalulateTools.setUserStatusInfo(mContext,
				mCurrentUser, mWeightResult, type));
	}
	
	private int mMaxDay = 7 ;

	private void initData() {
		if (mPrefenceValue != null) {
			mLineView.setRefenceLine(true);
		} else {
			mLineView.setRefenceLine(false);
		}
		
		setChartContentData();
		
		ArrayList<String> bottomList = initButtomTextList();
		mLineView.setBottomTextList(bottomList);
		
		mLineView.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY);
		mLineView.setmType(mType);
		mLineView.setFdataLists(fdataList);
		mLineView.setDataList(dataLists);
		mLineView.setDrawDotLine(true);
		
		mHandler.postDelayed(runnable, 100);
	}

	private ArrayList<String> initButtomTextList() {
		
		String txtDate = mYear + "-" + mMonth ;
		ArrayList<String> bottomList = new ArrayList<String>();
		if (isSelectYear) {
			if (mPrefenceValue != null) {
				for (int i = 0; i < 12 + 4; i++) {
					bottomList.add(String.valueOf(i + 1));
				}
			} else {
				for (int i = 0; i < 12; i++) {
					bottomList.add(String.valueOf(i + 1));
				}
			}

		} else {
			String[] dateArray = txtDate.split("-");
			int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
					Integer.valueOf(dateArray[1]));
			if (mPrefenceValue != null) {
				for (int i = 0; i < mMaxDay + 4; i++) {
					bottomList.add(String.valueOf(i + 1));
				}
			} else {
				for (int i = 0; i < days; i++) {
					bottomList.add(String.valueOf(i + 1));
				}
			}
		}
		return bottomList;
	}

	private void initChartDataByType(int type, ArrayList<Integer> dataList,
			ArrayList<Float> fdataList, boolean isYear, String txtDate) {
		String[] dateArray = txtDate.split("-");
		switch (type) {
		case 0:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager.selectWeightListByYear(Integer.valueOf(dateArray[0]),mUserId);
				
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(weightResult.getFatContent());
							dataList.add((int) (weightResult.getFatContent() * 10));
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			} else {
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),Integer.valueOf(dateArray[1]));
				
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(weightResult.getFatContent());
							dataList.add((int) (weightResult.getFatContent() * 10));
							break;
						}
					}
					
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}

			break;
		case 1:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager
						.selectWeightListByYear(Integer.valueOf(dateArray[0]),
								mUserId);
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(Float.valueOf(weightResult.getCalorie()));
							dataList.add(weightResult.getCalorie() * 10);
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}
			} else {
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
						Integer.valueOf(dateArray[1]));
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(Float.valueOf(weightResult
									.getCalorie()));
							dataList.add(weightResult.getCalorie() * 10);
							break;
						}
					}
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}
			break;
		case 2:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager
						.selectWeightListByYear(Integer.valueOf(dateArray[0]),
								mUserId);
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(weightResult.getWaterContent());
							dataList.add((int) (weightResult.getWaterContent() * 10));
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}
			} else {
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
						Integer.valueOf(dateArray[1]));
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(weightResult.getWaterContent());
							dataList.add((int) (weightResult.getWaterContent() * 10));
							break;
						}
					}
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}
			break;
		case 3:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager
						.selectWeightListByYear(Integer.valueOf(dateArray[0]),
								mUserId);
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(weightResult.getMuscleContent());
							dataList.add((int) (weightResult.getMuscleContent() * 10));
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}
			} else {
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
						Integer.valueOf(dateArray[1]));
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(weightResult.getMuscleContent());
							dataList.add((int) (weightResult.getMuscleContent() * 10));
							break;
						}
					}
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}
			break;
		case 4:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager
						.selectWeightListByYear(Integer.valueOf(dateArray[0]),
								mUserId);
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(weightResult.getVisceralFatContent());
							dataList.add((int) (weightResult
									.getVisceralFatContent() * 10));
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}
			} else {
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
						Integer.valueOf(dateArray[1]));
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(weightResult.getVisceralFatContent());
							dataList.add((int) (weightResult
									.getVisceralFatContent() * 10));
							break;
						}
					}
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}
			break;
		case 5:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager
						.selectWeightListByYear(Integer.valueOf(dateArray[0]),
								mUserId);
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(weightResult.getBoneContent());
							dataList.add((int) (weightResult.getBoneContent() * 10));
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}
			} else {
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
						Integer.valueOf(dateArray[1]));
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(weightResult.getBoneContent());
							dataList.add((int) (weightResult.getBoneContent() * 10));
							break;
						}
					}
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}
			break;
		case 7:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager
						.selectWeightListByYear(Integer.valueOf(dateArray[0]),
								mUserId);
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(weightResult.getBmi());
							dataList.add((int) (weightResult.getBmi() * 10));
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}
			} else {
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
						Integer.valueOf(dateArray[1]));
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(weightResult.getBmi());
							dataList.add((int) (weightResult.getBmi() * 10));
							break;
						}
					}
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}

			break;
		case 6:
			if (isYear) {
				List<WeightResult> weightList = mDatabaseManager
						.selectWeightListByYear(Integer.valueOf(dateArray[0]),
								mUserId);
				for (int i = 0; i < 12; i++) {
					boolean isFind = false;
					for (int j = 0; j < weightList.size(); j++) {
						WeightResult weightResult = weightList.get(j);
						int mounth = weightResult.getMonth();
						if ((mounth - 1) == i) {
							isFind = true;
							fdataList.add(weightResult.getWeight());
							dataList.add((int) (weightResult.getWeight() * 10));
							break;
						}
					}
					if (!isFind) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}
			} else {
				
				List<WeightResult> WeightList1 = mDatabaseManager
						.selectWeightListByMonth(mUserId);
				List<WeightResult> WeightList = mDatabaseManager
						.selectWeightListByMonth(Integer.valueOf(dateArray[0]),
								Integer.valueOf(dateArray[1]), mUserId);
				
				for(int i = 0 ;i < WeightList.size();i++){
					WeightResult weight = WeightList.get(i);
					if(weight.getDay() > mMaxDay){
						mMaxDay = weight.getDay() ;
					}
				}
				
				int days = DateUtils.getDay(Integer.valueOf(dateArray[0]),
						Integer.valueOf(dateArray[1]));
				for (int i = 0; i < days; i++) {
					boolean isFind = false;
					for (int j = 0; j < WeightList.size(); j++) {
						WeightResult weightResult = WeightList.get(j);
						if (i == (weightResult.getDay() - 1)) {
							isFind = true;
							fdataList.add(weightResult.getWeight());
							dataList.add((int) (weightResult.getWeight() * 10));
							break;
						}
					}
					if (!isFind && i < mMaxDay) {
						dataList.add(0);
						fdataList.add(0f);
					}
				}

			}
			
			break;
		default:
			break;
		}
		
		if (mPrefenceValue != null) {
			String[] prefenceValue = mPrefenceValue.split("~");

			int firstPrefenceValue = (int) (Float.valueOf(prefenceValue[0]) * 10);

			int secondPrefenceValue = (int) (Float.valueOf(prefenceValue[1]) * 10);

			dataList.add(firstPrefenceValue);
			dataList.add(firstPrefenceValue);

			dataList.add(secondPrefenceValue);
			dataList.add(secondPrefenceValue);

			fdataList.add(Float.valueOf(prefenceValue[0]));
			fdataList.add(Float.valueOf(prefenceValue[0]));

			fdataList.add(Float.valueOf(prefenceValue[1]));
			fdataList.add(Float.valueOf(prefenceValue[1]));
		}
	}

	private ArrayList<Integer> dataList ;
	
	private ArrayList<Float> fdataList ;
	
	private ArrayList<ArrayList<Integer>> dataLists ;
	
	
	private void setChartContentData() {
		
		dataList = new ArrayList<Integer>();
		fdataList = new ArrayList<Float>();
		dataLists = new ArrayList<ArrayList<Integer>>();
		
		String txtDate = mYear + "-" + mMonth;
		
		initChartDataByType(mType, dataList, fdataList, isSelectYear, txtDate);
		
		dataLists.add(dataList);
	}

	private int mType;

	private int mUserId;

	private String mData;

	private void getIntentData() {
		Intent intent = getIntent();
		mUserId = intent.getIntExtra("userId", -1);
		mType = intent.getIntExtra("type", -1);
		mData = intent.getStringExtra("data");
	}

	private TextView mTvName;

	private User mCurrentUser;

	private WeightResult mWeightResult;

	private void initUserInfo() {
		mCurrentUser = mDatabaseManager.selectUserInfo(mUserId);
		mWeightResult = mDatabaseManager.selectWeightByUserId(mUserId);
		initUserPhotoInfo("file://" + mCurrentUser.getPhoto(), mIvPhoto);
		mTvName.setText(mCurrentUser.getNickname());
	}

	private String mCurrentDate;

	private int mYear, mMonth, mDay;

	private DatabaseManager mDatabaseManager;

	private boolean isSelectYear = false;

	private TextView mTvDateValue;

	private TextView mTvNian;

	private TextView mTvMonth;

	private void initUserPhotoInfo(String url, RoundImageView iv) {
		ImageLoader.getInstance().displayImage(url, iv, mOptionsStyle,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {

					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {

					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {

					}
				});
	}

	private HorizontalScrollView mHVScrollView;

	private int mIntegerCurrentDate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart_activity);
		mContext = ChartActivity.this;

		mDatabaseManager = DatabaseManager.getInstance(mContext);

		mCurrentDate = DateUtils.getFormatDate(System.currentTimeMillis());

		mYear = Integer.valueOf(mCurrentDate.split("-")[0]);
		mMonth = Integer.valueOf(mCurrentDate.split("-")[1]);
		mDay = Integer.valueOf(mCurrentDate.split("-")[2]);
		String month = "" ;
		String day = "" ;
		if(mMonth < 10){
			month = "0"+mMonth ;
		}else{
			month = String.valueOf(mMonth) ;
		}
		
		if(mDay < 10){
			day = "0"+mDay ;
		}else{
			day = String.valueOf(mDay) ;
		}
		
		String date = String.valueOf(mYear) + month + day;
		mIntegerCurrentDate = Integer.valueOf(date);

		initView();
		getIntentData();

		mTvDateValue.setText(mYear + "-" + mMonth);
		mTvNian.setTextColor(mContext.getResources().getColor(
				R.color.color_dark));
		mTvMonth.setTextColor(mContext.getResources().getColor(
				R.color.color_blue));
		initUserInfo();

		initDataInfoByType(mType);
		initData();
	}

	private Handler mHandler = new Handler();

	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			if (isSelectYear) {
				if (mMonth > 8) {
					int offset = (mLineView.getWidth() / 12) * (mMonth - 4);
					mHVScrollView.smoothScrollTo(offset, 0);
				}

			} else {
				if (mDay > 8) {
					int offset = (mLineView.getWidth() / 30) * (mDay - 4);
					mHVScrollView.smoothScrollTo(offset, 0);
				}
			}
			mLineView.setSelectedDot();
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_nian:
			mHVScrollView.smoothScrollTo(1000, 0);
			isSelectYear = true;
			mTvNian.setTextColor(mContext.getResources().getColor(
					R.color.color_blue));
			mTvMonth.setTextColor(mContext.getResources().getColor(
					R.color.color_dark));
			mTvDateValue.setText(String.valueOf(mYear));
			initData();
			break;
		case R.id.tv_yue:
			isSelectYear = false;
			mTvNian.setTextColor(mContext.getResources().getColor(
					R.color.color_dark));
			mTvMonth.setTextColor(mContext.getResources().getColor(
					R.color.color_blue));
			mTvDateValue.setText(mYear + "-" + mMonth);
			initData();
			break;
		case R.id.iv_date_left:
			if (isSelectYear) {
				mYear--;
				if (mYear < 1986) {
					return;
				}

				String month = "";
				String day = "";
				if (String.valueOf(mMonth).length() == 1) {
					month = "0" + String.valueOf(mMonth);
				} else {
					month = String.valueOf(mMonth);
				}

				if (String.valueOf(mDay).length() == 1) {
					day = "0" + String.valueOf(mDay);
				} else {
					day = String.valueOf(mDay);
				}

				String str = String.valueOf(mYear) + month + day;

				int date = Integer.valueOf(str);
				if (mIntegerCurrentDate < date) {
					Toast.makeText(mContext,
							mContext.getString(R.string.aleary_new_day), 1)
							.show();
					break;
				}

				mTvDateValue.setText(String.valueOf(mYear));

				initData();
			} else {
				mMonth--;
				if (mMonth == 0) {
					mYear--;
					mMonth = 12;
					if (mYear < 1986) {
						return;
					}
				}

				String month = "";
				String day = "";
				if (String.valueOf(mMonth).length() == 1) {
					month = "0" + String.valueOf(mMonth);
				} else {
					month = String.valueOf(mMonth);
				}

				if (String.valueOf(mDay).length() == 1) {
					day = "0" + String.valueOf(mDay);
				} else {
					day = String.valueOf(mDay);
				}

				String str = String.valueOf(mYear) + month + day;

				int date = Integer.valueOf(str);
				if (mIntegerCurrentDate < date) {
					Toast.makeText(mContext,
							mContext.getString(R.string.aleary_new_day), 1)
							.show();
					break;
				}

				mTvDateValue.setText(mYear + "-" + mMonth);
				initData();
			}

			break;
		case R.id.iv_date_right:
			if (isSelectYear) {
				mYear++;
				if (mYear > 2050) {
					return;
				}

				String month = "";
				String day = "";
				if (String.valueOf(mMonth).length() == 1) {
					month = "0" + String.valueOf(mMonth);
				} else {
					month = String.valueOf(mMonth);
				}

				if (String.valueOf(mDay).length() == 1) {
					day = "0" + String.valueOf(mDay);
				} else {
					day = String.valueOf(mDay);
				}

				String str = String.valueOf(mYear) + month + day;
				int date = Integer.valueOf(str);
				if (mIntegerCurrentDate < date) {
					Toast.makeText(mContext,
							mContext.getString(R.string.aleary_new_day), 1)
							.show();
					break;
				}

				mTvDateValue.setText(String.valueOf(mYear));

				initData();
			} else {
				mMonth++;
				if (mMonth > 12) {
					mYear++;
					mMonth = 1;
					if (mYear > 2050) {
						return;
					}
				}

				String month = "";
				String day = "";
				if (String.valueOf(mMonth).length() == 1) {
					month = "0" + String.valueOf(mMonth);
				} else {
					month = String.valueOf(mMonth);
				}

				if (String.valueOf(mDay).length() == 1) {
					day = "0" + String.valueOf(mDay);
				} else {
					day = String.valueOf(mDay);
				}

				String str = String.valueOf(mYear) + month + day;
				int date = Integer.valueOf(str);
				if (mIntegerCurrentDate < date) {
					Toast.makeText(mContext,
							mContext.getString(R.string.aleary_new_day), 1)
							.show();
					break;
				}

				mTvDateValue.setText(mYear + "-" + mMonth);
				initData();
			}
			break;
		default:
			break;
		}
	}

}
