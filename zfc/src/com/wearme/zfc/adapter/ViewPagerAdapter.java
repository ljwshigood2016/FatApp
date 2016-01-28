package com.wearme.zfc.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wearme.zfc.R;
import com.wearme.zfc.adapter.LeftMenuAdapter.InvisiableMenu;
import com.wearme.zfc.bean.User;
import com.wearme.zfc.bean.WeightResult;
import com.wearme.zfc.db.DatabaseManager;
import com.wearme.zfc.ui.ChartActivity;
import com.wearme.zfc.ui.RegisterActivity;
import com.wearme.zfc.utils.CalulateTools;
import com.wearme.zfc.widget.CircleProgressBar;
import com.wearme.zfc.widget.RoundImageView;

/**
 * 
 */
public class ViewPagerAdapter extends PagerAdapter {
	
    private ArrayList<View> views;
    
    private InvisiableMenu mInvisiableMenu ;

    private Context mContext ;
    
    private List<User> mUserList ;
    
    private DisplayImageOptions mOptionsStyle ;
    
    public DisplayImageOptions getmOptionsStyle() {
		return mOptionsStyle;
	}

	public void setmOptionsStyle(DisplayImageOptions mOptionsStyle) {
		this.mOptionsStyle = mOptionsStyle;
	}

	public List<User> getmUserList() {
		return mUserList;
	}

	public void setmUserList(List<User> mUserList) {
		this.mUserList = mUserList;
	}

	public InvisiableMenu getmInvisiableMenu() {
		return mInvisiableMenu;
	}

	public void setmInvisiableMenu(InvisiableMenu mInvisiableMenu) {
		this.mInvisiableMenu = mInvisiableMenu;
	}
	
	private boolean isConnect ;
	
	public void notifyViewPaperConnect(boolean isConnect){
		this.isConnect = isConnect ;
		notifyDataSetChanged();
	}
	

	private DatabaseManager mDatabaseManager ;
	
	public ViewPagerAdapter (Context context,ArrayList<View> views,List<User> userList){
		this.mContext = context ;
        this.views = views;
        this.mUserList = userList ;
        mDatabaseManager = DatabaseManager.getInstance(context);
    }
       
    @Override
	public int getItemPosition(Object object) {

		return POSITION_NONE;
	}
    
	@Override
	public int getCount() {
        return views == null ? 0 : views.size();
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
	

    @Override
    public Object instantiateItem(View container, final int position) {
    	
    	((ViewPager) container).addView(views.get(position), 0);
    	
    	if(mUserList.size() > position){
    		User user = mUserList.get(position);
        	final WeightResult weightResult = mDatabaseManager.selectWeightByUserId(user.getUid());
            ImageView iv = (ImageView)views.get(position).findViewById(R.id.iv_menu) ;
            RoundImageView ivPhoto = (RoundImageView)views.get(position).findViewById(R.id.iv_photo);
            TextView tv = (TextView)views.get(position).findViewById(R.id.tv_name);
            final TextView tvWeight = (TextView)views.get(position).findViewById(R.id.tv_total_weight);
            TextView tvBMI = (TextView)views.get(position).findViewById(R.id.tv_bmi);
            TextView mTvBMIStatus = (TextView)views.get(position).findViewById(R.id.tv_status);
            ImageView mIvConnect = (ImageView)views.get(position).findViewById(R.id.iv_connect_info);
            TextView mTvConnect = (TextView)views.get(position).findViewById(R.id.tv_connect_info);
            final CheckBox cbWeightDanwei = (CheckBox)views.get(position).findViewById(R.id.iv_danwei);
            ImageView mIvInfo = (ImageView)views.get(position).findViewById(R.id.iv_info);
            
            if(mIvInfo != null){
            	mIvInfo.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent intent = new Intent(mContext,RegisterActivity.class);
    					intent.putExtra("user_id", mUserList.get(position).getUid());
    					intent.putExtra("type", 1);
    					( (FragmentActivity) mContext).startActivityForResult(intent,54);
    				}
    			});
            }
            if(cbWeightDanwei != null){
            	 cbWeightDanwei.setOnClickListener(new OnClickListener() {
         			
         			@Override
         			public void onClick(View arg0) {
         				
         				if(cbWeightDanwei.isChecked()){
         					DecimalFormat decimalFormat = new DecimalFormat("0.0");
     						String weight = decimalFormat.format(CalulateTools.changeKGtoLBWithKG(Float.valueOf(tvWeight.getText().toString().trim())));
         					tvWeight.setText(weight);	
         					
         				}else{
         					DecimalFormat decimalFormat = new DecimalFormat("0.0");
     						String weight = decimalFormat.format(CalulateTools.changeLBtoKGWithLB(Float.valueOf(tvWeight.getText().toString().trim())));
         					tvWeight.setText(weight);
         				}
         			}
         		});
            }
            
            if(isConnect){
            	if(mIvConnect != null){
            		mIvConnect.setImageResource(R.drawable.ble_connected);
            	}
            	if(mTvConnect != null){
            		mTvConnect.setText(mContext.getString(R.string.connect_success));  
            	}
            }else{
            	if(mIvConnect != null){
            		mIvConnect.setImageResource(R.drawable.ble_disconnect);
            	}
            	if(mTvConnect != null){
            		mTvConnect.setText(mContext.getString(R.string.shake_cheng));  
            	}
            }
            if(tvBMI != null){
            	DecimalFormat decimalFormat = new DecimalFormat("0.0");
            	String bmi = decimalFormat.format(weightResult.getBmi());
            	tvBMI.setText("BMI: "+bmi);
            	mTvBMIStatus.setText(CalulateTools.calculateBMIWithUser(mContext,mUserList.get(position), weightResult));
            	tvBMI.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent intent = new Intent(mContext,ChartActivity.class);
         				intent.putExtra("type", 7);
         				intent.putExtra("userId", mUserList.get(position).getUid());
         				intent.putExtra("data", String.valueOf(weightResult.getBmi()));
         				mContext.startActivity(intent);
    				}
    			});
            }
            if(ivPhoto != null){
            	initUserPhotoInfo("file://"+mUserList.get(position).getPhoto(),ivPhoto);
            }
            if(tvWeight != null){
            	DecimalFormat decimalFormat = new DecimalFormat("0.0");
            	if(weightResult.getOrg() == 1){
                	tvWeight.setText(0.0+"");
            	}else{
            		String weight = decimalFormat.format(weightResult.getWeight());
                	tvWeight.setText(weight);
            	}
            
            	tvWeight.setOnClickListener(new OnClickListener() {
    				
    				@Override
    				public void onClick(View v) {
    					Intent intent = new Intent(mContext,ChartActivity.class);
         				intent.putExtra("type", 6);
         				intent.putExtra("userId", mUserList.get(position).getUid());
         				intent.putExtra("data", String.valueOf(weightResult.getWeight()));
         				mContext.startActivity(intent);
    				}
    			});
            }
            if(tv != null && position <= mUserList.size() - 1){
            	tv.setText(mUserList.get(position).getNickname());
            }
            if(iv != null){
            	iv.setOnClickListener(new OnClickListener() {
        			
        			@Override
        			public void onClick(View v) {
        				if(mInvisiableMenu != null){
        					mInvisiableMenu.invisiableMenu();	
        				}
        			}
        		});	
            }
            
            CircleProgressBar dpFat = (CircleProgressBar)views.get(position).findViewById(R.id.dp_fat);
            CircleProgressBar dpCalorie = (CircleProgressBar)views.get(position).findViewById(R.id.db_calorie);
            CircleProgressBar dpWater = (CircleProgressBar)views.get(position).findViewById(R.id.dp_water);
            CircleProgressBar dpMuscle = (CircleProgressBar)views.get(position).findViewById(R.id.dp_muscle);
            CircleProgressBar dpVisceralFat = (CircleProgressBar)views.get(position).findViewById(R.id.dp_visceralfat);
            CircleProgressBar dpBone = (CircleProgressBar)views.get(position).findViewById(R.id.dp_bone);
            
            if(dpFat != null){
                dpFat.setMax(100);
            }
            if(dpCalorie != null){
            	 dpCalorie.setMax(100);
            }
            if(dpWater != null){
            	 dpWater.setMax(100);
            }
            if(dpMuscle != null){
            	dpMuscle.setMax(100);
            }
            
            if(dpVisceralFat != null){
            	dpVisceralFat.setMax(100); 
            }
           
            if(dpBone != null){
            	dpBone.setMax(100);
            }
            
            TextView mTvFat = (TextView)views.get(position).findViewById(R.id.tv_fat_value);
            TextView mTvFatDetail = (TextView)views.get(position).findViewById(R.id.tv_fat_detail);
            TextView mTvCalorie = (TextView)views.get(position).findViewById(R.id.tv_calorie_value);
            TextView mTvCalorieDetail = (TextView)views.get(position).findViewById(R.id.tv_calorie_detail);
            TextView mTvWater = (TextView)views.get(position).findViewById(R.id.tv_water_value);
            TextView mTvWaterDetail = (TextView)views.get(position).findViewById(R.id.tv_water_detail);
            TextView mTvMuscle = (TextView)views.get(position).findViewById(R.id.tv_muscle_value);
            TextView mTvMuscleDetail = (TextView)views.get(position).findViewById(R.id.tv_muscle_detail);
            TextView mTvVisceralFat = (TextView)views.get(position).findViewById(R.id.tv_visceralfat_value);
            TextView mTvVisceralFatDetail = (TextView)views.get(position).findViewById(R.id.tv_visceralfat_detail);
            TextView mTvBone = (TextView)views.get(position).findViewById(R.id.tv_bone_value);
            TextView mTvBoneDetail = (TextView)views.get(position).findViewById(R.id.tv_bone_detail);
            
            
            if(weightResult != null){
            	 if(dpFat != null){
            		DecimalFormat decimalFormat = new DecimalFormat("0.0");
                 	String fat = decimalFormat.format(weightResult.getFatContent());
                 	String detail = CalulateTools.calculateFatContentWithUser(mContext,mUserList.get(position), weightResult);
                 	mTvFat.setText(fat);
                 	mTvFatDetail.setText(detail);
                 	
                 	if(detail.equals(mContext.getString(R.string.piandi))){
                 		dpFat.setProgress(30);
                 	}else if(detail.equals(mContext.getString(R.string.biaozhun))){
                 		dpFat.setProgress(60);
                 	}else if(detail.equals(mContext.getString(R.string.piangao))){
                 		dpFat.setProgress(90);
                 	}else if(detail.equals(mContext.getString(R.string.feipang))){
                 		dpFat.setProgress(90);
                 	}
                 	
                 	dpFat.setOnClickListener(new OnClickListener() {
             			
             			@Override
             			public void onClick(View v) {
             				Intent intent = new Intent(mContext,ChartActivity.class);
             				intent.putExtra("type", 0);
             				intent.putExtra("userId", mUserList.get(position).getUid());
             				intent.putExtra("data", String.valueOf(weightResult.getFatContent()));
             				mContext.startActivity(intent);
             			}
             		}) ;	
                 }
                 
                 if(dpCalorie != null){
                	DecimalFormat decimalFormat = new DecimalFormat("0.0");
                  	String calorie = decimalFormat.format(weightResult.getCalorie());
                 	mTvCalorie.setText(calorie);
                 	String detail = CalulateTools.calculateCalorieWithUser(mContext,mUserList.get(position), weightResult);
                 	mTvCalorieDetail.setText(detail);
                 	
                 	if(detail.equals(mContext.getString(R.string.piandi))){
                 		dpCalorie.setProgress(30);
                 	}else if(detail.equals(mContext.getString(R.string.biaozhun))){
                 		dpCalorie.setProgress(60);
                 	}else if(detail.equals(mContext.getString(R.string.piangao))){
                 		dpCalorie.setProgress(90);
                 	}
                 	
                 	dpCalorie.setOnClickListener(new OnClickListener() {
             			
             			@Override
             			public void onClick(View v) {
             				Intent intent = new Intent(mContext,ChartActivity.class);
             				intent.putExtra("type", 1);
             				intent.putExtra("userId", mUserList.get(position).getUid());
             				intent.putExtra("data", String.valueOf(weightResult.getCalorie()));
             				mContext.startActivity(intent);
             			}
             		}) ;	
                 }
                 
                 if(dpWater != null){
                	DecimalFormat decimalFormat = new DecimalFormat("0.0");
                   	String WaterContent = decimalFormat.format(weightResult.getWaterContent());
                   	mTvWater.setText(WaterContent);
                   	String detail = CalulateTools.calculateWaterContentWithUser(mContext,mUserList.get(position), weightResult) ;
                   	mTvWaterDetail.setText(detail);
                   	
                	if(detail.equals(mContext.getString(R.string.piandi))){
                		dpWater.setProgress(30);
                 	}else if(detail.equals(mContext.getString(R.string.biaozhun))){
                 		dpWater.setProgress(60);
                 	}else if(detail.equals(mContext.getString(R.string.piangao))){
                 		dpWater.setProgress(90);
                 	}
                   	
                 	dpWater.setOnClickListener(new OnClickListener() {
             			
             			@Override
             			public void onClick(View v) {
             				Intent intent = new Intent(mContext,ChartActivity.class);
             				intent.putExtra("type", 2);
             				intent.putExtra("userId", mUserList.get(position).getUid());
             				intent.putExtra("data", String.valueOf(weightResult.getWaterContent()));
             				mContext.startActivity(intent);
             			}
             		}) ;	
                 }
                 
                 if(dpMuscle != null){
                	DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    String MuscleContent = decimalFormat.format(weightResult.getMuscleContent());
                 	mTvMuscle.setText(String.valueOf(MuscleContent));
                 	String detail = CalulateTools.calculateMuscleContentWithUser(mContext,mUserList.get(position), weightResult) ;
                 	mTvMuscleDetail.setText(detail);
                 	
                 	if(detail.equals(mContext.getString(R.string.diyu))){
                 		dpMuscle.setProgress(30);
                 	}else if(detail.equals(mContext.getString(R.string.biaozhun))){
                 		dpMuscle.setProgress(60);
                 	}else if(detail.equals(mContext.getString(R.string.piangao))){
                 		dpMuscle.setProgress(90);
                 	}
                 	
                 	dpMuscle.setOnClickListener(new OnClickListener() {
             			
             			@Override
             			public void onClick(View v) {
             				Intent intent = new Intent(mContext,ChartActivity.class);
             				intent.putExtra("type", 3);
             				intent.putExtra("userId", mUserList.get(position).getUid());
             				intent.putExtra("data", String.valueOf(weightResult.getMuscleContent()));
             				mContext.startActivity(intent);
             			}
             		}) ;	
                 }
                 
                 if(dpVisceralFat != null){
                	DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    String VisceralFatContent = decimalFormat.format(weightResult.getVisceralFatContent());
                 	mTvVisceralFat.setText(VisceralFatContent);
                 	String detail = CalulateTools.calculateVisceralFatContentWithUser(mContext,mUserList.get(position), weightResult) ;
                 	mTvVisceralFatDetail.setText(detail);
                 	
                	if(detail.equals(mContext.getString(R.string.biaozhun))){
                		dpVisceralFat.setProgress(30);
                 	}else if(detail.equals(mContext.getString(R.string.piangao))){
                 		dpVisceralFat.setProgress(60);
                 	}else if(detail.equals(mContext.getString(R.string.gaoyu))){
                 		dpVisceralFat.setProgress(90);
                 	}
                	
                 	dpVisceralFat.setOnClickListener(new OnClickListener() {
             			
             			@Override
             			public void onClick(View v) {
             				Intent intent = new Intent(mContext,ChartActivity.class);
             				intent.putExtra("type", 4);
             				intent.putExtra("userId", mUserList.get(position).getUid());
             				intent.putExtra("data", String.valueOf(weightResult.getVisceralFatContent()));
             				mContext.startActivity(intent);
             			}
             		}) ;	
                 }
                 
                 if(dpBone != null){
                	DecimalFormat decimalFormat = new DecimalFormat("0.0");
                    String BoneContent = decimalFormat.format(weightResult.getBoneContent());
                 	mTvBone.setText(BoneContent);
                 	String detail = CalulateTools.calculateBoneContentWithUser(mContext,mUserList.get(position), weightResult) ;
                 	mTvBoneDetail.setText(detail);

                	if(detail.equals(mContext.getString(R.string.diyu))){
                		dpBone.setProgress(30);
                 	}else if(detail.equals(mContext.getString(R.string.biaozhun))){
                 		dpBone.setProgress(60);
                 	}else if(detail.equals(mContext.getString(R.string.piangao))){
                 		dpBone.setProgress(90);
                 	}else if(detail.equals(mContext.getString(R.string.gaoyu))){
                 		dpBone.setProgress(90);
                 	}
                 	
                 	dpBone.setOnClickListener(new OnClickListener() {
             			
             			@Override
             			public void onClick(View v) {
             				Intent intent = new Intent(mContext,ChartActivity.class);
             				intent.putExtra("type", 5);
             				intent.putExtra("userId", mUserList.get(position).getUid());
             				intent.putExtra("data", String.valueOf(weightResult.getBoneContent()));
             				mContext.startActivity(intent);
             			}
             		}) ;	
                 }
            }
    	}else{
    		
    		 ImageView ivAddUser = (ImageView)views.get(position).findViewById(R.id.iv_add_user);
             if(ivAddUser != null){
             	ivAddUser.setOnClickListener(new OnClickListener() {
         			
         			@Override
         			public void onClick(View v) {
     	    			Intent intent = new Intent(mContext,RegisterActivity.class);
     	    			((FragmentActivity) mContext).startActivityForResult(intent,54);
         			}
         		});	
             }
             
    	}
        return views.get(position);
    }
    
    /**
	 */
	@Override
	public boolean isViewFromObject(View view, Object arg1) {
		return (view == arg1);
	}

	/**
	 */
    @Override
    public void destroyItem(View view, int position, Object arg2) {
        ((ViewPager) view).removeView(views.get(position));       
    }
}
