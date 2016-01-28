package com.wearme.zfc.ui;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import com.wearme.zfc.utils.PictureUtil;
import com.wearme.zfc.wheelview.OnWheelScrollListener;
import com.wearme.zfc.wheelview.WheelView;
import com.wearme.zfc.wheelview.adapter.NumericWheelAdapter;
import com.wearme.zfc.wheelview.adapter.TextWheelAdapter;
import com.wearme.zfc.widget.CommonDialog;
import com.wearme.zfc.widget.Constants;
import com.wearme.zfc.widget.RoundImageView;
import com.wearme.zfc.widget.SelectPicPopupWindow;

public class RegisterActivity extends BaseActivity implements OnClickListener{
	
	private PopupWindow mPopupWindow;
	
	private LayoutInflater mLayoutInflater ;
	
	private DatabaseManager mDatabaseManager ;
	
	private EditText mEtNickName ;
	
	private RoundImageView mIvAddUser ;

	private String mCurrentFilePath = "";
	
	private TextView mTvSuggestWeight ;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == Constants.UNDO){
			return ;
		}
		if(requestCode == Constants.CAMERA_CAPTURE){
			if (resultCode == Activity.RESULT_OK) {
				PictureUtil.galleryAddPic(this, mCurrentPhotoPath);
			} else {
				PictureUtil.deleteTempFile(mCurrentPhotoPath);
				return ;
			}
			Intent intent = new Intent(mContext,ClipPictureActivity.class);
			intent.putExtra("filePath", mCurrentPhotoPath);
			intent.putExtra("action", 0);
			startActivityForResult(intent,Constants.REQUEST_GET_MEDIA);
		}else if(requestCode == Constants.REQUEST_GET_MEDIA){
			mCurrentFilePath = data.getStringExtra("filePath");
			initUserPhotoInfo("file://"+mCurrentFilePath,mIvAddUser);	
		}
	}
	
	private void initUserPhotoInfo(String url,RoundImageView iv){
		ImageLoader.getInstance().displayImage(url, iv,mOptionsStyle, 
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						
					}
	
					@Override
					public void onLoadingFailed(String imageUri, View view,FailReason failReason) {
					}
	
					@Override
					public void onLoadingComplete(String imageUri, View view,Bitmap loadedImage) {
						
					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,int current, int total) {
						
					}
				});
	}
	
	private int mType ;
	
	private int mUserId ;
	
	private void getIntentData(){
		Intent intent = getIntent();
		mType = intent.getIntExtra("type", 0);
		mUserId = intent.getIntExtra("user_id", -1);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		mContext = RegisterActivity.this ;
		mDatabaseManager = DatabaseManager.getInstance(mContext);
		mLayoutInflater = LayoutInflater.from(mContext);
		Resources res = getResources();
		sexs = res.getStringArray(R.array.sex);
		
		initView();
		getSystemDate();
		getIntentData();
		initData();
	}
	
	private LinearLayout mLLDelete ;
	
	private void initData(){
		if(mType == 1){
			mLLDelete.setVisibility(View.VISIBLE);
			mUser = mDatabaseManager.selectUserInfo(mUserId);
			mEtAge.setText(String.valueOf(mUser.getAge()));
			DecimalFormat decimalFormat = new DecimalFormat("0.0");
			String weight = decimalFormat.format(mUser.getWeight());
			String height = decimalFormat.format(mUser.getHeight());
			mEtNickName.setText(mUser.getNickname());
			mEtWeight.setText(weight);
			mEtHeight.setText(height);
			if(mUser.getGender() == 0){ // 0 nan
				mEtSex.setText(sexs[0]);
			}else{
				mEtSex.setText(sexs[1]);
			}
			initUserPhotoInfo("file://"+mUser.getPhoto(), mIvAddUser);
			mCurrentFilePath = mUser.getPhoto() ;
		}else{
			mLLDelete.setVisibility(View.GONE);
		}
	}
	
	private String mDate ;
	
	private void getSystemDate(){
		mDate = DateUtils.getFormatDate(System.currentTimeMillis());
	}
	
	private TextView mEtAge ;
	
	private TextView mEtSex ;
	
	private TextView mEtHeight ;
	
	private TextView mEtWeight ;

	private TextView mTvBack ;
	
	private TextView mTvComplete ;
	
	private void initView(){
		mTvSuggestWeight = (TextView)findViewById(R.id.tv_suggest_weight);
		mIvAddUser = (RoundImageView)findViewById(R.id.iv_add_user);
		mTvBack = (TextView)findViewById(R.id.tv_back);
		mTvComplete = (TextView)findViewById(R.id.tv_ok);
		mTvDelete = (TextView)findViewById(R.id.tv_delete);
		mEtNickName = (EditText)findViewById(R.id.et_nick_name); 
		mEtAge = (TextView)findViewById(R.id.et_age);
		mEtSex = (TextView)findViewById(R.id.et_sex);
		mEtHeight = (TextView)findViewById(R.id.et_height);
		mEtWeight = (TextView)findViewById(R.id.et_weight);
		mLLDelete = (LinearLayout)findViewById(R.id.ll_delete);
		mLLDelete.setOnClickListener(this);
		mEtAge.setOnClickListener(this);
		mEtSex.setOnClickListener(this);
		mEtHeight.setOnClickListener(this);
		mEtWeight.setOnClickListener(this);
		mTvBack.setOnClickListener(this);
		mTvComplete.setOnClickListener(this);
		mIvAddUser.setOnClickListener(this);
		mTvDelete.setOnClickListener(this);
		
		mEtSex.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!mEtHeight.getText().toString().trim().equals("")){
					if(mEtSex.getText().toString().equals(sexs[0])){
						
						mTvSuggestWeight.setText(mContext.getString(R.string.suggest_weight)+(int)(Float.valueOf(mEtHeight.getText().toString().trim()) - 102)+"~"+
								(int)(Float.valueOf(mEtHeight.getText().toString().trim()) - 96)+"KG");
					}else{
						mTvSuggestWeight.setText((mContext.getString(R.string.suggest_weight)+(int)(Float.valueOf(mEtHeight.getText().toString().trim()) - 115)+"~"+
								(int)(Float.valueOf(mEtHeight.getText().toString().trim()) - 100)+"KG"));
					}
				}
			}
		});
		
		mEtHeight.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(!mEtSex.getText().toString().trim().equals("")){
					if(mEtSex.getText().toString().equals(sexs[0])){
						mTvSuggestWeight.setText(mContext.getString(R.string.suggest_weight)+(int)(Float.valueOf(mEtHeight.getText().toString().trim()) - 102)+"~"+
												(int)(Integer.valueOf(mEtHeight.getText().toString().trim()) - 96)+"KG");
					}else{
						mTvSuggestWeight.setText(mContext.getString(R.string.suggest_weight)+(int)(Float.valueOf(mEtHeight.getText().toString().trim()) - 115)+"~"+
								(int)(Float.valueOf(mEtHeight.getText().toString().trim()) - 100)+"KG");
					}
				}
			}
		});
		
	}
	
	private LinearLayout mLLAddViewContainer ;
	
	private View parent  ;
	
	private void showMenuPopWindows(View view){
		
		View contentView = getLayoutInflater().inflate(R.layout.popwindow, null);
		mLLAddViewContainer = (LinearLayout)contentView.findViewById(R.id.ll);
		mPopupWindow = new PopupWindow(contentView,
										ViewGroup.LayoutParams.MATCH_PARENT,
										ViewGroup.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mPopupWindow.setAnimationStyle(R.style.animation);
		mLLAddViewContainer.removeAllViews();
		mLLAddViewContainer.addView(view);
		
		parent = this.findViewById(R.id.main);
		
		mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
	}
	
	private View view ;
	
	private View getAgeView() {
		
		view = mLayoutInflater.inflate(R.layout.layout_age, null);
		
		final NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 200, "%02d"); 
		numericWheelAdapter.setLabel("");
		final WheelView wvage = (WheelView) view.findViewById(R.id.age);
		
		wvage.setViewAdapter(numericWheelAdapter);
		
		wvage.setVisibleItems(7);
		wvage.setCurrentItem(19);
		wvage.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				int currentItem = wvage.getCurrentItem();
				mEtAge.setText(numericWheelAdapter.getItemText(currentItem));
			}
		});
		
		return view;
	}
	
	private View getHeightView() {
		
		view = mLayoutInflater.inflate(R.layout.layout_height, null);
		
		Resources res = getResources();
		String[] heightsDanwei = res.getStringArray(R.array.height_danwei);
		
		final NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 250, "%02d");
		final TextWheelAdapter textWheelAdapter = new TextWheelAdapter(mContext,heightsDanwei);
		
		numericWheelAdapter.setLabel("");
		
		final WheelView wvHeight = (WheelView) view.findViewById(R.id.height);
		final WheelView wvDanWei = (WheelView)view.findViewById(R.id.danwei);
		wvDanWei.setViewAdapter(textWheelAdapter);
		wvHeight.setViewAdapter(numericWheelAdapter);
		
		wvHeight.setVisibleItems(7);
		wvDanWei.setVisibleItems(7);
		wvHeight.setCurrentItem(169);
		wvHeight.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				int currentItem = wvHeight.getCurrentItem();
				mEtHeight.setText(numericWheelAdapter.getItemText(currentItem));
			}
		});
		
		wvDanWei.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				int currentItem = wvDanWei.getCurrentItem();
				//mEtHeight.setText(textWheelAdapter.getItemText(currentItem));
			}
		});
		
		return view;
	}
	
	private String[]  sexs ;
	
	private View getSexView() {
		
		view = mLayoutInflater.inflate(R.layout.layout_sex, null);
		
		final TextWheelAdapter numericWheelAdapter = new TextWheelAdapter(this,sexs); 
		
		final WheelView wvSex = (WheelView) view.findViewById(R.id.sex);
		
		wvSex.setVisibleItems(7);
		wvSex.setCurrentItem(0);
		wvSex.setViewAdapter(numericWheelAdapter);
		
		wvSex.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				int currentItem = wvSex.getCurrentItem();
				mEtSex.setText(numericWheelAdapter.getItemText(currentItem));
			}
		});
		
		return view;
	}
	
	private View getWeightView() {
		
		view = mLayoutInflater.inflate(R.layout.layout_weight, null);
		
		final NumericWheelAdapter numericWheelAdapter = new NumericWheelAdapter(this,1, 300, "%02d"); 
		numericWheelAdapter.setLabel("");
		final WheelView wvWeight = (WheelView) view.findViewById(R.id.weight);
		
		wvWeight.setViewAdapter(numericWheelAdapter);
		
		wvWeight.setVisibleItems(7);
		wvWeight.setCurrentItem(49);
		wvWeight.addScrollingListener(new OnWheelScrollListener() {
			
			@Override
			public void onScrollingStarted(WheelView wheel) {
				
			}
			
			@Override
			public void onScrollingFinished(WheelView wheel) {
				int currentItem = wvWeight.getCurrentItem();
				mEtWeight.setText(numericWheelAdapter.getItemText(currentItem));
			}
		});
		
		return view;
	}
	
	private User mUser ;
	
	private boolean checkDataValide(){
		boolean isRet = true ;
		if(mEtAge.getText().toString().equals("")){
			isRet = false ;
		}else if(mEtNickName.getText().toString().equals("")){
			isRet = false ;
		}else if(mEtHeight.getText().toString().equals("")){
			isRet = false ;
		}else if(mEtWeight.getText().toString().equals("")){
			isRet = false ;
		}else if(mCurrentFilePath.equals("")){
			isRet = false ;
		}
		return isRet;
	}
	
	private void getUserInfo(){
		if(mUser == null){
			mUser = new User();	
		}
		
		
		mUser.setAge(Integer.valueOf(mEtAge.getText().toString()));
		mUser.setNickname(mEtNickName.getText().toString());
		mUser.setHeight(Float.valueOf(mEtHeight.getText().toString()));
		mUser.setWeight(Float.valueOf(mEtWeight.getText().toString()));
		if(mEtSex.getText().toString().trim().equals(sexs[0])){
			mUser.setGender(0);
		}else{
			mUser.setGender(1);
		}
		mUser.setPhoto(mCurrentFilePath);
	}
	
	private SelectPicPopupWindow menuWindow;
	
	private String mCurrentPhotoPath ;
	
	private File createImageFile() throws IOException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = "sheqing_" + timeStamp + ".jpg";

		File image = new File(PictureUtil.getAlbumDir(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
		return image;
	}
	
	
	public void takePhoto() {
		
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {
			File f = createImageFile();
			takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			startActivityForResult(takePictureIntent,Constants.CAMERA_CAPTURE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	private OnClickListener itemsOnClick = new OnClickListener() {

		public void onClick(View v) {
			Intent intent;
			menuWindow.dismiss();
			switch (v.getId()) {
			case R.id.ll_capture:
				takePhoto();
				break;
			case R.id.ll_gallery:
				intent = new Intent(mContext, GalleryActivity.class);
				startActivityForResult(intent, Constants.REQUEST_GET_MEDIA);
				break;
			default:
				break;
			}
		}
	};
	
	private TextView mTvDelete ;

	
	private void showWindows() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE); 
        imm.hideSoftInputFromWindow(mEtNickName.getWindowToken(),0);
		menuWindow = new SelectPicPopupWindow(RegisterActivity.this,
				itemsOnClick);
		menuWindow.showAtLocation(this.findViewById(R.id.main), Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	private void initWeightData(){
		mWeightReuslt = new WeightResult();
		mWeightReuslt.setUid(mUser.getUid());
		mWeightReuslt.setWeight(Float.valueOf(mEtWeight.getText().toString().trim()));
		mWeightReuslt.setBmi(0);
		mWeightReuslt.setCalorie(0);
		mWeightReuslt.setFatContent(0);
		mWeightReuslt.setBoneContent(0);
		mWeightReuslt.setMuscleContent(0);
		mWeightReuslt.setWaterContent(0);
		mWeightReuslt.setVisceralFatContent(0);
		mWeightReuslt.setOrg(1);
		String[] dateArray = mDate.split("-");
		
		mWeightReuslt.setYear(Integer.valueOf(dateArray[0]));
		mWeightReuslt.setMonth(Integer.valueOf(dateArray[1]));
		mWeightReuslt.setDay(Integer.valueOf(dateArray[2]));
		
		mWeightReuslt.setRecordDate(mDate);
	}
	
	private WeightResult mWeightReuslt  = new WeightResult();
	
	@Override
	public void onClick(View v) {
		
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
		imm.showSoftInput(mEtNickName,InputMethodManager.SHOW_FORCED); 
		imm.hideSoftInputFromWindow(mEtNickName.getWindowToken(), 0);
		
		switch (v.getId()) {
		case R.id.iv_add_user:
			showWindows();
			break ;
		case R.id.et_age:
			showMenuPopWindows(getAgeView());
			break;
		case R.id.et_weight:
			showMenuPopWindows(getWeightView());
			break ;
		case R.id.et_sex:
			showMenuPopWindows(getSexView());
			break ;
		case R.id.et_height:
			showMenuPopWindows(getHeightView());
			break ;
		case R.id.tv_back :
			finish();
			break ;
		case R.id.tv_ok:
			
			boolean flag = checkDataValide();
			if(!flag){
				Toast.makeText(mContext, mContext.getString(R.string.info_not_non), 1).show();
				return ;
			}
			
			getUserInfo();
			if(mType == 1){
				mDatabaseManager.updateUser(mUser);
			}else{
				initWeightData();
				if(!mDatabaseManager.isUserExist(mUser)){
					mDatabaseManager.insertUser(mUser);
				}
				int uid = mDatabaseManager.selectMaxUserId();
				mDatabaseManager.insertWeightResult(mWeightReuslt,uid);
			}
			finish();
			break ;
		case R.id.tv_delete:
			CommonDialog.getInstance(mContext).showDialog(mContext,1,new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					mDatabaseManager.deleteUser(mUser.getUid());
					setResult(54);
					finish();
				}
			});
		
			break ;
		default:
			break;
		}
	}
}
