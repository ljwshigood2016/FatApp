package com.wearme.zfc.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.wearme.zfc.R;

public class InfomationActivity extends BaseActivity implements OnClickListener {

	private CheckBox mIvSelectSex;

	private TextView mTvComplete;

	private ScrollView mScrollViewMan;

	private ScrollView mScrollViewWonman;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_infomation);
		initView();

	}

	private void initView() {
		mScrollViewMan = (ScrollView) findViewById(R.id.sv_man);
		mScrollViewWonman = (ScrollView) findViewById(R.id.sv_women);
		mIvSelectSex = (CheckBox) findViewById(R.id.iv_select_sex);
		mTvComplete = (TextView) findViewById(R.id.tv_complete);
		mTvComplete.setOnClickListener(this);
		mIvSelectSex.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_select_sex:
			if (mIvSelectSex.isChecked()) {
				mScrollViewMan.setVisibility(View.VISIBLE);
				mScrollViewWonman.setVisibility(View.GONE);
			} else {
				mScrollViewMan.setVisibility(View.GONE);
				mScrollViewWonman.setVisibility(View.VISIBLE);
				
			}
			break;
		case R.id.tv_complete:
			finish();
			break;
		default:
			break;
		}
	}

}
