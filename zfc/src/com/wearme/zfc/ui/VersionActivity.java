package com.wearme.zfc.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.wearme.zfc.R;

public class VersionActivity extends BaseActivity implements OnClickListener {

	private TextView mTvBack;

	private void initView() {
		mTvBack = (TextView) findViewById(R.id.tv_back);
		mTvBack.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version);
		initView();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;

		default:
			break;
		}
	}

}
