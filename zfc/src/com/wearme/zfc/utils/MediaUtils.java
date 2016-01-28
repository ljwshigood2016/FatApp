package com.wearme.zfc.utils;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.wearme.zfc.bean.MediaInfo;

public class MediaUtils {
	
	private static Cursor getImageCursor(Context context){
		String[] projection = new String[] { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.BUCKET_ID, // 直接包含该图片文件的文件夹ID，防止在不同下的文件夹重名
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // 直接包含该图片文件的文件夹名
				MediaStore.Images.Media.DISPLAY_NAME, // 图片文件名
				MediaStore.Images.Media.DATA };
		String selection = " 0==0) ORDER BY (bucket_display_name";
		ContentResolver cr = context.getContentResolver();
		Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				projection, selection, null, "");
		return cursor ;
	}
	public static ArrayList<MediaInfo> getImageFolderList(Context context,ArrayList<MediaInfo> mediaInfoList) {

		ContentResolver cr = context.getContentResolver();
		Cursor cursor = getImageCursor(context);
		
		while (cursor.moveToNext()) {
			String finaName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			MediaInfo mediaInfo = new MediaInfo();
			mediaInfo.setFileName(finaName);
			mediaInfo.setFilePath(path);
			mediaInfo.setType(0);
			mediaInfoList.add(mediaInfo);
		}
		
		if (null != cursor && !cursor.isClosed()) {
			cursor.close();
		}
		return mediaInfoList;
	}
	
	private static Cursor getVideoCursor(Context context) {
		String[] projection = new String[] { MediaStore.Video.Media._ID,
				MediaStore.Video.Media.BUCKET_ID, 
				MediaStore.Video.Media.BUCKET_DISPLAY_NAME, 
				MediaStore.Video.Media.DISPLAY_NAME, 
				MediaStore.Video.Media.DATA };
		String selection = " 0==0) ORDER BY (bucket_display_name";
		ContentResolver cr = context.getContentResolver();

		Cursor cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
				projection, selection, null, "");
		return cursor;
	}

	public static ArrayList<MediaInfo> getVideoFolderList(Context context,ArrayList<MediaInfo> mediaInfoList) {

		if (context != null) {
			Cursor cursor = getVideoCursor(context);
			while (cursor.moveToNext()) {
				String displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME));
				String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
				MediaInfo mediaInfo = new MediaInfo();
				mediaInfo.setFileName(displayName);
				mediaInfo.setFilePath(path);
				mediaInfo.setType(1);
				mediaInfoList.add(mediaInfo);
			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return mediaInfoList;
	}

}
