package com.wearme.zfc.wheelview.adapter;

import android.content.Context;

public class TextWheelAdapter extends AbstractWheelTextAdapter {

	public TextWheelAdapter(Context context, int itemResource,int itemTextResource) {
		super(context, itemResource, itemTextResource);
	}

	public TextWheelAdapter(Context context, int itemResource) {
		super(context, itemResource);
	}

	public TextWheelAdapter(Context context) {
		super(context);
		
	}
	
	private String[] mSex ;
	
	public TextWheelAdapter(Context context,String[] sexs){
		super(context);
		this.mSex = sexs ;
	}


	@Override
	public int getItemsCount() {
		return mSex == null ? 0 : mSex.length;
	}

	@Override
	public CharSequence getItemText(int index) {
		return mSex[index];
	}

}
