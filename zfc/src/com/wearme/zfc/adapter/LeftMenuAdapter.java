package com.wearme.zfc.adapter;

import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.wearme.zfc.R;
import com.wearme.zfc.ui.InfomationActivity;
import com.wearme.zfc.ui.VersionActivity;

public class LeftMenuAdapter extends BaseAdapter implements OnItemClickListener {

	private int left_text[] = { R.string.home, R.string.information,R.string.product_info,
			R.string.about_me };

	private int left_image[] = { R.drawable.ic_launcher,
			R.drawable.left_infomation, R.drawable.left_infomation,R.drawable.aboutus};

	private Context mContext;

	private LayoutInflater inflater;
	
	private InvisiableMenu mInvisiableMenu ;

	public LeftMenuAdapter(Context context,InvisiableMenu invisiableMenu) {
		this.mContext = context;
		this.mInvisiableMenu = invisiableMenu ;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return left_image.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	class ViewHolder {

		ImageView mIvIcon;

		TextView mTvIcon;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_left_menu, null);
			viewHolder.mIvIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
			viewHolder.mTvIcon = (TextView) convertView.findViewById(R.id.tv_icon);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.mIvIcon.setImageResource(left_image[position]);
		viewHolder.mTvIcon.setText(left_text[position]);

		return convertView;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		Intent intent  ;
		switch (position) {
		case 0:
			if(mInvisiableMenu != null){
				mInvisiableMenu.visiableMene();
			}
			break;
		case 1:
			intent = new Intent(mContext,InfomationActivity.class);
			mContext.startActivity(intent);
			break;
		case 2:

			break;
		case 3:
			intent = new Intent(mContext,VersionActivity.class);
			mContext.startActivity(intent);			
			break ;
		
		default:
			break;
		}
	}

	public interface InvisiableMenu{
		
		public void invisiableMenu();
		
		public void visiableMene();
		
	}
	
}
