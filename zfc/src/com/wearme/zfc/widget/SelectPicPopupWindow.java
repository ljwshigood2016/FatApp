package com.wearme.zfc.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.wearme.zfc.R;

public class SelectPicPopupWindow extends PopupWindow {

	private LinearLayout mLlCapture ;
	
	private LinearLayout mLlCancel ;
	
	private View mMenuView;
	
	private LinearLayout mLLModifyPhoto ;
	
	private int mAction ;
	
	
	public int getmAction() {
		return mAction;
	}
	

	public void setmAction(int mAction) {
		this.mAction = mAction;
	}
	
	public SelectPicPopupWindow(Activity context,OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.item_select_media, null);
		mLLModifyPhoto  = (LinearLayout)mMenuView.findViewById(R.id.ll_gallery);
		mLlCapture = (LinearLayout) mMenuView.findViewById(R.id.ll_capture);
		mLlCancel = (LinearLayout) mMenuView.findViewById(R.id.ll_cancel);
		mLlCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				dismiss();
			}
		});
		mLlCapture.setOnClickListener(itemsOnClick);
		mLLModifyPhoto.setOnClickListener(itemsOnClick);
		
		this.setContentView(mMenuView);
		this.setWidth(LayoutParams.MATCH_PARENT);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setAnimationStyle(R.style.AnimBottom);
		ColorDrawable dw = new ColorDrawable(0xb0000000);
		this.setBackgroundDrawable(dw);
		mMenuView.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				
				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
				int y=(int) event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}				
				return true;
			}
		});

	}

}
