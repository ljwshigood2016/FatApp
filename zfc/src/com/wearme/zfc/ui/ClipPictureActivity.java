package com.wearme.zfc.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wearme.zfc.R;
import com.wearme.zfc.utils.PictureUtil;
import com.wearme.zfc.widget.ClipImageLayout;
import com.wearme.zfc.widget.ClipView;
import com.wearme.zfc.widget.Constants;

public class ClipPictureActivity extends BaseActivity implements OnTouchListener, OnClickListener{

	private ClipView mClipview;

	private Matrix matrix = new Matrix();
	private Matrix savedMatrix = new Matrix();

	private static final int NONE = 0;
	private static final int DRAG = 1;
	private static final int ZOOM = 2;
	private int mode = NONE;

	private PointF start = new PointF();
	private PointF mid = new PointF();
	private float oldDist = 1f;

	private Bitmap bitmap;

	private LinearLayout mLLBack ;
	
	private TextView mTvMainInfo;

	private TextView mTvLeft;

	private ImageView mIvLeft;

	private ImageView mIvRight;

	private TextView mTvRight;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clip_picture);
		getIntentData();
		initView();
	}

	private void initView() {
		mTvLeft = (TextView)findViewById(R.id.tv_back);
		mTvRight = (TextView)findViewById(R.id.tv_ok);
		mTvLeft.setOnClickListener(this);
		mTvRight.setOnClickListener(this);
		mClipImageLayout = (ClipImageLayout)findViewById(R.id.src_pic);
		mClipImageLayout.setmBitmapFile(mBitmapFile);
	}

	String mBitmapFile ;
	
	private Bitmap decodeBitmapFile(){
		Bitmap bitmap = null ;
		if(mBitmapFile != null){
			File file = new File(mBitmapFile);
			if(file.exists()){
				bitmap = PictureUtil.getSmallBitmap(mBitmapFile);
			}	
		}
		return bitmap ;
	} 
	
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		case MotionEvent.ACTION_DOWN:
			savedMatrix.set(matrix);
			start.set(event.getX(), event.getY());
			mode = DRAG;
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			oldDist = spacing(event);
			if (oldDist > 10f) {
				savedMatrix.set(matrix);
				midPoint(mid, event);
				mode = ZOOM;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			mode = NONE;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mode == DRAG) {
				matrix.set(savedMatrix);
				matrix.postTranslate(event.getX() - start.x, event.getY()
						- start.y);
			} else if (mode == ZOOM) {
				float newDist = spacing(event);
				if (newDist > 10f) {
					matrix.set(savedMatrix);
					float scale = newDist / oldDist;
					matrix.postScale(scale, scale, mid.x, mid.y);
				}
			}
			break;
		}
		view.setImageMatrix(matrix);
		return true;
	}

	/**
	 * 
	 * @param event
	 * @return
	 */
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return (float) Math.sqrt(x * x + y * y);
	}

	/**
	 * 
	 * @param point
	 * @param event
	 */
	private void midPoint(PointF point, MotionEvent event) {
		float x = event.getX(0) + event.getX(1);
		float y = event.getY(0) + event.getY(1);
		point.set(x / 2, y / 2);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_ok:
			Bitmap clipBitmap = mClipImageLayout.clip();
			saveBitmap2Native(clipBitmap);
			Intent intent = new Intent();
			intent.putExtra("filePath", mCurrentPhotoPath);
			setResult(Constants.MODIFY_PHOTO, intent);
			finish();
			break;
		case R.id.tv_back:
			setResult(Constants.UNDO);
			finish();
			break;
		default:
			break;
		}

	}

	private void saveBitmap2Native(Bitmap bitmap) {
		try {
			createImageFile();
			saveBitmap2file(bitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private ClipImageLayout mClipImageLayout;
	
	private  int mAction ;
	
	private void getIntentData() {
		Intent intent = getIntent();
		mBitmapFile = intent.getStringExtra("filePath") ;
		mAction = intent.getIntExtra("action", -1);
	}

	private String mCurrentPhotoPath;

	private File createImageFile() throws IOException {

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = format.format(new Date());
		String imageFileName = timeStamp + ".jpg";

		File image = new File(Environment.getExternalStorageDirectory(), imageFileName);
		mCurrentPhotoPath = image.getAbsolutePath();
	
		return image;
	}

	public void saveBitmap2file(Bitmap bmp) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(mCurrentPhotoPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bmp.compress(format, quality, stream);
		try {
			stream.flush();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @return
	 */
	private Bitmap getBitmap() {
		View view = this.getWindow().getDecorView();
		view.setDrawingCacheEnabled(true);
		view.buildDrawingCache();

		Rect frame = new Rect();
		this.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;

		Bitmap finalBitmap = Bitmap.createBitmap(view.getDrawingCache(),
				mClipview.getClipLeftMargin(), mClipview.getClipTopMargin()
						+ statusBarHeight, mClipview.getClipWidth(),
				mClipview.getClipHeight());
		view.destroyDrawingCache();
		return finalBitmap;
	}

}