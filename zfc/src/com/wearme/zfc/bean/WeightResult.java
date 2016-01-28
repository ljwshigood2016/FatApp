package com.wearme.zfc.bean;

import java.io.Serializable;

public class WeightResult implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int uid ;
	
	private float weight ;
	
	private float bmi ;
	
	private int calorie ;
	
	private float fatContent ;
	
	private float boneContent ;
	
	private float muscleContent ;
	
	private float waterContent ;
	
	private float visceralFatContent ;
	
	private String recordDate ;
	
	private int org ;

	private int year ;
	
	private int month ;
	
	private int day ;
	
	public int getOrg() {
		return org;
	}

	public void setOrg(int org) {
		this.org = org;
	}
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getBmi() {
		return bmi;
	}

	public void setBmi(float bmi) {
		this.bmi = bmi;
	}

	public int getCalorie() {
		return calorie;
	}

	public void setCalorie(int calorie) {
		this.calorie = calorie;
	}

	public float getFatContent() {
		return fatContent;
	}

	public void setFatContent(float fatContent) {
		this.fatContent = fatContent;
	}

	public float getBoneContent() {
		return boneContent;
	}

	public void setBoneContent(float boneContent) {
		this.boneContent = boneContent;
	}

	public float getMuscleContent() {
		return muscleContent;
	}

	public void setMuscleContent(float muscleContent) {
		this.muscleContent = muscleContent;
	}

	public float getWaterContent() {
		return waterContent;
	}

	public void setWaterContent(float waterContent) {
		this.waterContent = waterContent;
	}

	public float getVisceralFatContent() {
		return visceralFatContent;
	}

	public void setVisceralFatContent(float visceralFatContent) {
		this.visceralFatContent = visceralFatContent;
	}

	public String getRecordDate() {
		return recordDate;
	}

	public void setRecordDate(String recordDate) {
		this.recordDate = recordDate;
	}
	
	


}
