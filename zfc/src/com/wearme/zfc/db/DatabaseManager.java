/**
 * 项目名称：PublcNumber
 * 文件名：DatabaseManager.java 
 * 2015-1-6-上午10:26:00
 * 2015 万家恒通公司-版权所有
 * @version 1.0.0
 */
package com.wearme.zfc.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wearme.zfc.bean.User;
import com.wearme.zfc.bean.WeightResult;

/**
 * 
 * @description: 数据库管理类，sqlite采用单例模式进行数据管理
 *
 * author : liujw
 * modify : 
 * 2015-1-6 上午10:41:47
 */


public class DatabaseManager {

	private Context mContext;

	private static DatabaseManager mInstance;

	private DBHelper mDBHelp = null;

	private SQLiteDatabase mSQLiteDatabase;

	private DatabaseManager(Context context) {
		this.mContext = context;
		mDBHelp = DBHelper.getInstance(context);
		mSQLiteDatabase = mDBHelp.getSQLiteDatabaseObject();
	}

	public synchronized static DatabaseManager getInstance(Context context){
		if(mInstance == null){
			mInstance = new DatabaseManager(context);
		}
		return mInstance ;
	}
	
	public synchronized void recyleCursor(Cursor cursor){
		if (cursor != null) {
			cursor.close();
			cursor = null;
		}
	}

	// 按年检索
	
	
	
	// 按月检索
	
	public List<User> selectUserList(){
		List<User> userList = new ArrayList<User>() ;
		String sql = "select * from User" ;
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while(cursor != null && cursor.moveToNext()){
			User user = new User();
			user.setUid(cursor.getInt(cursor.getColumnIndex("id")));
			user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
			user.setWeight(cursor.getInt(cursor.getColumnIndex("weight"))) ;
			user.setNickname(cursor.getString(cursor.getColumnIndex("nickname"))) ;
			user.setHeight(cursor.getInt(cursor.getColumnIndex("height"))) ;
			user.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
			user.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
			userList.add(user);
		}
		return userList;
	}
	
	public boolean isUserExist(User user){
		boolean isExist =  false ;
		String sql = "select * from User where id = %d";
		sql = String.format(sql, user.getUid());
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while(cursor != null && cursor.moveToNext()){
			isExist = true ;
		}
		return isExist ;
	}
	
	public void updateUser(User user){
		String upDateSQL  = "update User set nickname = \"%s\",height = %.2f ,weight = %.2f ,age = %d,gender =  %d,photo = \"%s\" where id = %d" ;
		upDateSQL = String.format(upDateSQL, user.getNickname(),user.getHeight(),user.getWeight(),user.getAge(),user.getGender(),user.getPhoto(),user.getUid());
		mSQLiteDatabase.execSQL(upDateSQL);
	}
	
	public void insertUser(User user){
		if(isUserExist(user)){
			updateUser(user);
		}else{
			String insertSQL  = "insert into User values(null,\"%s\",%.2f,%.2f,%d,%d,\"%s\")" ;
			insertSQL = String.format(insertSQL, user.getNickname(),user.getHeight(),user.getWeight(),user.getAge(),user.getGender(),user.getPhoto());
			mSQLiteDatabase.execSQL(insertSQL);
		}
	}
	
	public int selectMaxUserId(){
		int uid = 0 ;
		String sql  = "select max(id) as id from User" ;
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while(cursor != null && cursor.moveToNext()){
			uid = cursor.getInt(cursor.getColumnIndex("id"));
		}
		return uid ;
	}
	
	public void deleteUser(int uid){
		String deleteSQL = "delete from User where id = %d" ;
		deleteSQL  =  String.format(deleteSQL,uid);
		mSQLiteDatabase.execSQL(deleteSQL);
	}
	
	public boolean isInsertRecordWeight(WeightResult result,User user){
		boolean ret = true ;
		String sql = "select * from WeightResultsetTable where recorddate = \"%s\"  and uid = %d" ;
		sql = String.format(sql, result.getRecordDate(),user.getUid());
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		
		while(cursor != null && cursor.moveToNext()){
			ret = false ;
		}
		return ret ;
	}
	
	public List<WeightResult> selectWeightListByYear(int year,int uid){
		List<WeightResult> weightList = new ArrayList<WeightResult>();
		String sql = "select " +
					"uid ,"+
					"avg(weight) as weight," +
					"avg(bmi) as bmi," +
					"avg(calorie) as calorie," +
					"avg(fatContent) as fatContent," +
					"avg(boneContent) as boneContent," +
					"avg(muscleContent) as muscleContent," +
					"avg(waterContent) as waterContent," +
					"avg(visceralFatContent) as visceralFatContent," +
					"month ," +
					"day " +
					"from WeightResultsetTable " +
					"where year = %d and uid = %d group by month" ;
		
		sql = String.format(sql, year,uid);
		
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while(cursor != null && cursor.moveToNext()){
			WeightResult weightResult = new WeightResult();
			weightResult.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
			weightResult.setWeight(cursor.getFloat(cursor.getColumnIndex("weight"))) ;
			weightResult.setBmi(cursor.getFloat(cursor.getColumnIndex("bmi")));
			weightResult.setCalorie(cursor.getInt(cursor.getColumnIndex("calorie")));
			weightResult.setFatContent(cursor.getFloat(cursor.getColumnIndex("fatContent")));
			weightResult.setBoneContent(cursor.getFloat(cursor.getColumnIndex("boneContent")));
			weightResult.setMuscleContent(cursor.getFloat(cursor.getColumnIndex("muscleContent")));
			weightResult.setWaterContent(cursor.getFloat(cursor.getColumnIndex("waterContent")));
			weightResult.setVisceralFatContent(cursor.getFloat(cursor.getColumnIndex("visceralFatContent")));
			weightResult.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			weightResult.setDay(cursor.getInt(cursor.getColumnIndex("day")));
			weightList.add(weightResult);
		}
		
		return weightList ;
	}
	
	public List<WeightResult> selectWeightListByMonth(int uid){
		List<WeightResult> weightList = new ArrayList<WeightResult>();
		String sql = "select * from WeightResultsetTable where uid = %d";
		sql = String.format(sql,uid);
		
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while(cursor != null && cursor.moveToNext()){
			WeightResult weightResult = new WeightResult();
			weightResult.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
			weightResult.setWeight(cursor.getFloat(cursor.getColumnIndex("weight"))) ;
			weightResult.setBmi(cursor.getFloat(cursor.getColumnIndex("bmi")));
			weightResult.setCalorie(cursor.getInt(cursor.getColumnIndex("calorie")));
			weightResult.setFatContent(cursor.getFloat(cursor.getColumnIndex("fatContent")));
			weightResult.setBoneContent(cursor.getFloat(cursor.getColumnIndex("boneContent")));
			weightResult.setMuscleContent(cursor.getFloat(cursor.getColumnIndex("muscleContent")));
			weightResult.setWaterContent(cursor.getFloat(cursor.getColumnIndex("waterContent")));
			weightResult.setVisceralFatContent(cursor.getFloat(cursor.getColumnIndex("visceralFatContent")));
			weightResult.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			weightResult.setDay(cursor.getInt(cursor.getColumnIndex("day")));
			weightList.add(weightResult);
		}
		return weightList ;
	}
	
	public List<WeightResult> selectWeightListByMonth(int year,int month,int uid){
		List<WeightResult> weightList = new ArrayList<WeightResult>();
		String sql = "select " +
				"uid,"+
				"weight," +
				"bmi," +
				"calorie," +
				"fatContent," +
				"boneContent," +
				"muscleContent," +
				"waterContent," +
				"visceralFatContent," +
				"month ," +
				"day " +
				"from WeightResultsetTable " +
				"where year = %d and month = %d and uid = %d " ;
		
		sql = String.format(sql, year,month,uid);
		
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while(cursor != null && cursor.moveToNext()){
			WeightResult weightResult = new WeightResult();
			weightResult.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
			weightResult.setWeight(cursor.getFloat(cursor.getColumnIndex("weight"))) ;
			weightResult.setBmi(cursor.getFloat(cursor.getColumnIndex("bmi")));
			weightResult.setCalorie(cursor.getInt(cursor.getColumnIndex("calorie")));
			weightResult.setFatContent(cursor.getFloat(cursor.getColumnIndex("fatContent")));
			weightResult.setBoneContent(cursor.getFloat(cursor.getColumnIndex("boneContent")));
			weightResult.setMuscleContent(cursor.getFloat(cursor.getColumnIndex("muscleContent")));
			weightResult.setWaterContent(cursor.getFloat(cursor.getColumnIndex("waterContent")));
			weightResult.setVisceralFatContent(cursor.getFloat(cursor.getColumnIndex("visceralFatContent")));
			weightResult.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			weightResult.setDay(cursor.getInt(cursor.getColumnIndex("day")));
			weightList.add(weightResult);
		}
		
		return weightList ;
	}
	
	public User selectUserInfo(int uid){
		User user = null ;
		String sql = "select * from User where id = %d" ;
		sql = String.format(sql, uid);
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while(cursor != null && cursor.moveToNext()){
			user = new User();
			user.setUid(cursor.getInt(cursor.getColumnIndex("id")));
			user.setAge(cursor.getInt(cursor.getColumnIndex("age")));
			user.setWeight(cursor.getInt(cursor.getColumnIndex("weight"))) ;
			user.setNickname(cursor.getString(cursor.getColumnIndex("nickname"))) ;
			user.setHeight(cursor.getInt(cursor.getColumnIndex("height"))) ;
			user.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
			user.setGender(cursor.getInt(cursor.getColumnIndex("gender")));
		}
		return user;
		
	}
	
	public void updateWeightResult(WeightResult result){
		String sqlUpdate = "update WeightResultsetTable set " +
				"weight = %.2f,bmi = %.2f,calorie = %d,fatContent = %.2f,boneContent = %.2f," +
				"muscleContent = %.2f,waterContent = %.2f,visceralFatContent = %.2f," +
				"year = %d,month = %d,day = %d,org = %d where recorddate = \"%s\" and uid = %d" ;
		
		sqlUpdate = String.format(sqlUpdate, result.getWeight(),result.getBmi(),result.getCalorie(),
				result.getFatContent(),result.getBoneContent(),result.getMuscleContent(),result.getWaterContent(),
				result.getVisceralFatContent(),result.getYear(),result.getMonth(),result.getDay(),
				result.getOrg(),result.getRecordDate(),result.getUid());
		
		mSQLiteDatabase.execSQL(sqlUpdate);
	}
	
	public void insertWeightResult(WeightResult result,int uid){
			
		String insertSQL = "insert into WeightResultsetTable values(null,%d,%.2f,%.2f,%d,%.2f,%.2f,%.2f,%.2f,%.2f,%d,%d,%d,\"%s\",%d)" ;
		insertSQL = String.format(insertSQL,uid, result.getWeight(),
				result.getBmi(),result.getCalorie(),result.getFatContent(),
				result.getBoneContent(),result.getMuscleContent(),
				result.getWaterContent(),result.getVisceralFatContent(),
				result.getYear(),result.getMonth(),result.getDay(),result.getRecordDate(),result.getOrg());
		mSQLiteDatabase.execSQL(insertSQL);
	}
	
	/*public List<WeightResult> selectWeightByUserId(){
		List<WeightResult> listWeightList = new ArrayList<WeightResult>();
		String sql = "select * from WeightResultsetTable where uid = %d" ;
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while (cursor != null && cursor.moveToNext()) {
			WeightResult weightResult = new WeightResult();
			weightResult.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
			weightResult.setWeight(cursor.getFloat(cursor.getColumnIndex("weight"))) ;
			weightResult.setBmi(cursor.getFloat(cursor.getColumnIndex("bmi")));
			weightResult.setCalorie(cursor.getInt(cursor.getColumnIndex("calorie")));
			weightResult.setFatContent(cursor.getFloat(cursor.getColumnIndex("fatContent")));
			weightResult.setBoneContent(cursor.getFloat(cursor.getColumnIndex("boneContent")));
			weightResult.setMuscleContent(cursor.getFloat(cursor.getColumnIndex("muscleContent")));
			weightResult.setWaterContent(cursor.getFloat(cursor.getColumnIndex("waterContent")));
			weightResult.setVisceralFatContent(cursor.getFloat(cursor.getColumnIndex("visceralFatContent")));
			weightResult.setYear(cursor.getInt(cursor.getColumnIndex("year")));
			weightResult.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			weightResult.setDay(cursor.getInt(cursor.getColumnIndex("day")));
			weightResult.setRecordDate(cursor.getString(cursor.getColumnIndex("recorddate")));
			weightResult.setOrg(cursor.getInt(cursor.getColumnIndex("org")));
			listWeightList.add(weightResult);
		}
		
		return listWeightList ;
	}
	*/
	public WeightResult selectWeightByUserId(int uid){
		WeightResult weightResult  = null ;
		String sql = "select * from WeightResultsetTable where uid = %d and recorddate = (select max(recorddate) from WeightResultsetTable)" ;
		sql = String.format(sql, uid);
		Cursor cursor = mSQLiteDatabase.rawQuery(sql, null);
		while (cursor != null && cursor.moveToNext()) {
			weightResult = new WeightResult();
			weightResult.setUid(cursor.getInt(cursor.getColumnIndex("uid")));
			weightResult.setWeight(cursor.getFloat(cursor.getColumnIndex("weight"))) ;
			weightResult.setBmi(cursor.getFloat(cursor.getColumnIndex("bmi")));
			weightResult.setCalorie(cursor.getInt(cursor.getColumnIndex("calorie")));
			weightResult.setFatContent(cursor.getFloat(cursor.getColumnIndex("fatContent")));
			weightResult.setBoneContent(cursor.getFloat(cursor.getColumnIndex("boneContent")));
			weightResult.setMuscleContent(cursor.getFloat(cursor.getColumnIndex("muscleContent")));
			weightResult.setWaterContent(cursor.getFloat(cursor.getColumnIndex("waterContent")));
			weightResult.setVisceralFatContent(cursor.getFloat(cursor.getColumnIndex("visceralFatContent")));
			weightResult.setYear(cursor.getInt(cursor.getColumnIndex("year")));
			weightResult.setMonth(cursor.getInt(cursor.getColumnIndex("month")));
			weightResult.setDay(cursor.getInt(cursor.getColumnIndex("day")));
			weightResult.setRecordDate(cursor.getString(cursor.getColumnIndex("recorddate")));
			weightResult.setOrg(cursor.getInt(cursor.getColumnIndex("org")));
		}
		
		return weightResult ;
	}
	
	public void deleteWeightResult(int uid){
		String deleteSQL = "delete from WeightResultsetTable where uid = %d" ;
		deleteSQL  =  String.format(deleteSQL,uid);
		mSQLiteDatabase.execSQL(deleteSQL);
	}
	
	
}
