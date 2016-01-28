package com.wearme.zfc.bean;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int uid ;
	
	private String nickname ;
	
	private float height ;
	
	private float weight ;
	
	private int age ;
	
	private int gender ;
	
	private String photo ;
	
	private float bmi ;
	
	private int calorie ;
	
	private float fatContent;
	
	private float boneContent ;
	
	private float muscleContent ;
	
	private float waterContent ;
	
	private float visceralFatContent ;
	
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

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	

}
