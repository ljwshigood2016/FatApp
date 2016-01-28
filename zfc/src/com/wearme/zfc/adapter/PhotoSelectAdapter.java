/**
 * 项目名称：PublcNumber
 * 文件名：ReleaseMediaSelectAdapter.java 
 * 2015-3-3-下午5:35:33
 * 2015 万家恒通公司-版权所有
 * @version 1.0.0
 */
package com.wearme.zfc.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.wearme.zfc.R;
import com.wearme.zfc.bean.MediaInfo;
import com.wearme.zfc.impl.IOnItemClickListener;
import com.wearme.zfc.ui.ClipPictureActivity;
import com.wearme.zfc.widget.Constants;

/**
 * 
 * @description:
 * 
 * author : liujw 
 * modify : 2015-3-3 下午5:35:33
 * 
 */
public class PhotoSelectAdapter extends BaseAdapter implements OnItemClickListener {

	private Context mContext;

	private LayoutInflater mLayoutInflater;

	private List<MediaInfo> mMediaInfoList;

	private DisplayImageOptions mOptions;

	final int VIEW_TYPE = 2;

	private IOnItemClickListener mOnItemClickListener ;
	
	private int mType ; // 0 : 图片截取   1：图片浏览
	
	private Activity mAcitivity ;
	
	public PhotoSelectAdapter(Context context,List<MediaInfo> mediaList, DisplayImageOptions options,IOnItemClickListener onItemClickListener,int type) {
		this.mContext = context;
		mLayoutInflater = LayoutInflater.from(context);
		this.mMediaInfoList = mediaList;
		this.mOptions = options;
		this.mType = type ;
		this.mOnItemClickListener = onItemClickListener;
	}

	@Override
	public int getCount() {
		return mMediaInfoList == null ? 0 : mMediaInfoList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public int getViewTypeCount() { 
		return VIEW_TYPE;
	}

	@Override
	public int getItemViewType(int position) {
		int type = 0;
		if (position == 0) {
			type = 1;
		} 
		return type;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		String filePath = null;
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.item_release_media,null);
			viewHolder = new ViewHolder();
			viewHolder.mIvSelect = (ImageView) convertView.findViewById(R.id.iv_select);
			viewHolder.mRelativityLayout = (RelativeLayout) convertView.findViewById(R.id.rl_media);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		filePath = "file://" + mMediaInfoList.get(position).getFilePath();
		ImageLoader.getInstance().displayImage(filePath, viewHolder.mIvSelect,mOptions, 
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
		return convertView;
	}
	
	private IGetPhoto mIGetPhoto ;
	
	
	public IGetPhoto getmIGetPhoto() {
		return mIGetPhoto;
	}

	public void setmIGetPhoto(IGetPhoto mIGetPhoto) {
		this.mIGetPhoto = mIGetPhoto;
	}

	public interface IGetPhoto{
		
		public void getPhoto();
		
	}

	class ViewHolder {
		ImageView mIvSelect;
		RelativeLayout mRelativityLayout;
	}
	
	class CamearHolder{
		LinearLayout mLlinearLayout ;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		MediaInfo info = mMediaInfoList.get(position);
		Intent intent = new Intent(mContext,ClipPictureActivity.class);
		intent.putExtra("filePath", info.getFilePath());
		intent.putExtra("flag", 0);
		((Activity) mContext).startActivityForResult(intent, Constants.MODIFY_PHOTO);
	}

}
