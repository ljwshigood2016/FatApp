/**
 * 项目名称：PublcNumber
 * 文件名：DBHelper.java 
 * 2015-1-6-上午10:26:00
 * 2015 万家恒通公司-版权所有
 * @version 1.0.0
 */
package com.wearme.zfc.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.wearme.zfc.R;

/**
 * 
 * @description:数据库创建对象
 *
 * author : liujw
 * modify : 
 * 2015-1-6 上午10:45:22
 * 
 */
public class DBHelper{
	
	private static DBHelper mDBHelper = null;
	
	private Context mContext ;
	
	private DBHelper(Context context) {
		this.mContext = context ;
		loadDatabaseFile(mContext);
	}

	public static synchronized DBHelper getInstance(Context context) {
		if (null == mDBHelper) {
			mDBHelper = new DBHelper(context);
		}
		return mDBHelper;
	}
	
	public SQLiteDatabase getSQLiteDatabaseObject(){
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        return database ;
	}
	
	private String DB_PATH ;
	
	private String DB_NAME = "vscalebalance.db";
	
	
	private void loadDatabaseFile(Context context){
		  DB_PATH = "/data/data/com.wearme.zfc/databases/" ;
	      if ((new File(DB_PATH + DB_NAME)).exists() == false) {
            File file = new File(DB_PATH);
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                InputStream is = context.getResources().openRawResource(R.raw.vscalebalance);
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}

}
