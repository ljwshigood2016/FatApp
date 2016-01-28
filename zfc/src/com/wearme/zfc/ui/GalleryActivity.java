/**
- * 项目名称：PublcNumber
 * 文件名：CaptureActivity.java 
 * 2015-2-24-上午11:52:03
 * 2015 万家恒通公司-版权所有
 * @version 1.0.0
 */
package com.wearme.zfc.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.wearme.zfc.R;
import com.wearme.zfc.adapter.PhotoSelectAdapter;
import com.wearme.zfc.adapter.PhotoSelectAdapter.IGetPhoto;
import com.wearme.zfc.bean.MediaInfo;
import com.wearme.zfc.impl.IOnItemClickListener;
import com.wearme.zfc.utils.MediaUtils;
import com.wearme.zfc.widget.Constants;

/**
 * 
 * @description:
 *
 * author : liujw
 * modify : 
 * 2015-2-24 上午11:52:03
 *
 * 
 */
public class GalleryActivity extends BaseActivity implements OnClickListener,IOnItemClickListener,IGetPhoto{

	private GridView mGvPhoto ;
	
	private PhotoSelectAdapter mReleasePictureAdapter;
	
	private ArrayList<MediaInfo> mPictureInfoList = new ArrayList<MediaInfo>();

	private TextView mTvMainInfo;

	private TextView mTvLeft;

	private TextView mTvRight;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		getIntentData();
		initView();
		initData();
	}
	
	private void initView(){
		
		mTvLeft = (TextView)findViewById(R.id.tv_back);
		mTvRight = (TextView)findViewById(R.id.tv_ok);
		mTvMainInfo = (TextView)findViewById(R.id.tv_main_info);
		mTvLeft.setOnClickListener(this);
		mTvRight.setOnClickListener(this);
		mTvMainInfo.setText(mContext.getString(R.string.gallery));
		mGvPhoto = (GridView)findViewById(R.id.gridView);
	}
	
	private int mFlag;
	
	private void getIntentData(){
		Intent intent = getIntent();
		mFlag = intent.getIntExtra("flag", -1);
	}
	
	private void initData(){
		MediaUtils.getImageFolderList(mContext, mPictureInfoList);
		mReleasePictureAdapter = new PhotoSelectAdapter(mContext,mPictureInfoList, mOptionsStyle,this,mFlag);
		mGvPhoto.setAdapter(mReleasePictureAdapter);
		mReleasePictureAdapter.setmIGetPhoto(this);
		mGvPhoto.setOnItemClickListener(mReleasePictureAdapter);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			break ;
		case R.id.tv_back:
			setResult(Constants.UNDO);
			finish();
			break ;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == Constants.UNDO){
			return ;
		}
		
		if(requestCode == Constants.MODIFY_PHOTO){
			setResult(resultCode, data);
			finish();
		}
	}
	
	@Override
	public void onItemCheckListener(List<MediaInfo> list) {
		
	}
	
	private static final String PATH = Environment.getExternalStorageDirectory() + "/DCIM";
	
	public String name;
	

	@Override
	public void getPhoto() {
		
	}
	
	
}
