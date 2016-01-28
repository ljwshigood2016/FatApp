package com.wearme.zfc.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;

import com.wearme.zfc.R;

public class CommonDialog {
	
	private Dialog mDialog ;
	
	private ProgressBar mProgressBar ;

	private Context mContext ;
	
	private static CommonDialog mInstance ;
	
	private CommonDialog(Context context){
		this.mContext = context ;
	}
	
	
	public static CommonDialog getInstance(Context context){
		if(mInstance == null){
			mInstance = new CommonDialog(context);
		}
		return mInstance;
	}
	
	private LinearLayout mLLTip ;
	
	private LinearLayout mLLCancel ;
	
	private LinearLayout mLLOk ;
	
	private LinearLayout mLLBackground ;
	
	public void showDialog(Context context,int type ,final Handler handler){
		boolean isFinish = ((Activity)context).isFinishing() ;
		if(isFinish){
			return ;
		}
		
		if(mDialog == null){
		
			mDialog = new Dialog(context, R.style.dialog_style);
			mDialog.setCancelable(true);
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.global_dialog, null);
			
			mLLCancel = (LinearLayout)view.findViewById(R.id.ll_cancel);
			mLLOk = (LinearLayout)view.findViewById(R.id.ll_ok);
			mLLTip = (LinearLayout)view.findViewById(R.id.ll_tip);
			mProgressBar = (ProgressBar)view.findViewById(R.id.pb_progress);
			mLLBackground = (LinearLayout)view.findViewById(R.id.ll_global);
			Window dialogWindow = mDialog.getWindow();
			WindowManager.LayoutParams lp = dialogWindow.getAttributes();
			lp.width = (int) (context.getResources().getDisplayMetrics().density*288);
			dialogWindow.setAttributes(lp);
			
			mLLCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mDialog.dismiss();
				}
			});
			
			mLLOk.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(handler != null){
						Message msg = handler.obtainMessage();
						msg.what = 1;
						handler.sendMessage(msg);
					}
					mDialog.dismiss();
				}
			});
			
			if(type == 0){
				mProgressBar.setVisibility(View.VISIBLE);
				mLLBackground.setBackground(null);
				mLLTip.setVisibility(View.GONE);
			}else{
				mLLBackground.setBackgroundResource(R.drawable.bg_dialog);
				mProgressBar.setVisibility(View.GONE);
				mLLTip.setVisibility(View.VISIBLE);
			}
			
			mDialog.addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			mDialog.setCancelable(true);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.show();
		}else{
			mDialog.setCancelable(true);
			mDialog.setCanceledOnTouchOutside(false);
			mDialog.show();
		}
	}
	
	public void dismissDialog(){
		if(mDialog != null){
			mDialog.dismiss();
			mDialog = null ;
		}
	}
	
}
