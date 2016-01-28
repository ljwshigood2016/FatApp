package com.publicnumber.satellite.util;


import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class ImageTools {

	public static Bitmap getBitmapFromFile(String filePath,int size) {
		BitmapFactory.Options opts = null;
		opts = new BitmapFactory.Options();
		opts.inSampleSize = size ;
		return BitmapFactory.decodeFile(filePath, opts);
	}

	/**
	 * 将圆形图片,返回Bitmap
	 * 
	 * @param bitmap
	 *            源Bitmap
	 * @return
	 */

	public static Bitmap toRoundBitmap(Bitmap bitmap, int zoomSize) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);

		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);

		output = zoomBitmap(output, zoomSize, zoomSize);

		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		return output;
	}

	/**
	 * 缩放图片
	 * 
	 * @param bitmap
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		matrix.postScale((float) width / w, (float) height / h);
		Bitmap retBitmap = Bitmap
				.createBitmap(bitmap, 0, 0, w, h, matrix, true);
		if (bitmap != null) {
			bitmap.recycle();
			bitmap = null;
		}
		return retBitmap;
	}

}
