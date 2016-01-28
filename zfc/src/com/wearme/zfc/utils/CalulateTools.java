package com.wearme.zfc.utils;

import android.content.Context;

import com.wearme.zfc.R;
import com.wearme.zfc.bean.User;
import com.wearme.zfc.bean.WeightResult;

public class CalulateTools {
	
	public static String setUserStatusInfo(Context context,User user,WeightResult weight,int type){
		 String nomalData = "" ;
		 switch (type) {
	        case 0:
	            //脂肪含量
	            if (weight.getFatContent()< 3.0) {
	            	nomalData = context.getString(R.string.srgddfz);
	            }else if (3.0 <= user.getFatContent() && user.getFatContent() <= 19.0){
	            	nomalData = context.getString(R.string.suggest);
	            }else{
	            	nomalData = context.getString(R.string.jqdl) ;
	            }
	            break;
	        case 1:
	            //卡路里
	            
	            if (weight.getCalorie() < 1400) {
	            	nomalData = context.getString(R.string.srgddfz) ;
	            }else if (1400.0 <= user.getCalorie() && user.getCalorie() <= 1600){
	            	nomalData = context.getString(R.string.suggest) ;
	            }else{
	            	nomalData = context.getString(R.string.jqdl) ;
	            }
	            
	            break;
	        case 2:
	            //水分含量
	            if (user.getGender() == 0) {
	                //男
	                
	                if (weight.getWaterContent() < 52) {
	                	nomalData = context.getString(R.string.srgddfz) ;
	                }else if (52.0 <= weight.getWaterContent() && weight.getWaterContent() <= 60.0){
	                	nomalData = context.getString(R.string.suggest) ;
	                }else{
	                	nomalData = context.getString(R.string.jqdl) ;
	                }
	                
	            }else{
	                
	                if (weight.getWaterContent() < 56) {
	                	nomalData = context.getString(R.string.srgddfz)  ;
	                }else if (56.0 <= weight.getWaterContent() && weight.getWaterContent() <= 62.0){
	                	nomalData = context.getString(R.string.suggest) ;
	                }else{
	                	nomalData = context.getString(R.string.jqdl) ;
	                }
	            }
	            break;
	        case 3:
	            //肌肉含量
	            if (user.getGender()  == 0) {
	                //男
	                if (weight.getMuscleContent() < 31) {
	                	nomalData = context.getString(R.string.srgddfz); 
	                }else if (31.0 <= weight.getMuscleContent() && weight.getMuscleContent() <= 34.9){
	                	nomalData = context.getString(R.string.suggest) ;
	                }else{
	                	nomalData = context.getString(R.string.jqdl);
	                }
	            }else{
	                if (weight.getMuscleContent() < 26) {
	                	nomalData =  context.getString(R.string.srgddfz) ;
	                }else if (26.0 <= weight.getMuscleContent() && weight.getMuscleContent() <= 27.9){
	                	nomalData = context.getString(R.string.suggest) ;
	                }else{
	                	nomalData= context.getString(R.string.jqdl);
	                }
	            }
	            break;
	        case 4:
	            //内脏脂肪含量
	            
	            if (weight.getVisceralFatContent() < 1) {
	            	nomalData = context.getString(R.string.srgddfz) ;
	            }else if (1.0 <= weight.getVisceralFatContent() && weight.getVisceralFatContent() <= 9.0){
	            	nomalData = context.getString(R.string.suggest);
	            }else{
	            	nomalData = context.getString(R.string.jqdl);
	            }
	            break;
	        case 5:
	            //骨骼
	            if (user.getGender() == 0) {
	                
	                if (weight.getBoneContent() < 2.6) {
	                	nomalData = context.getString(R.string.srgddfz) ;
	                }else if (2.6 <= weight.getBoneContent() && weight.getBoneContent() <= 3.1){
	                	nomalData = context.getString(R.string.suggest);
	                }else{
	                	nomalData = context.getString(R.string.jqdl);
	                }
	                
	            }else{
	                if (weight.getBoneContent() < 1.9) {
	                	nomalData = context.getString(R.string.srgddfz);
	                }else if (1.9 <= weight.getBoneContent() && weight.getBoneContent() <= 2.4){
	                	nomalData = context.getString(R.string.suggest);
	                }else{
	                	nomalData = context.getString(R.string.jqdl) ;
	                }
	            }
	            break;
	        case 6:
	            //体重
	            if (user.getGender() == 0) {
	                
	                if (weight.getWeight() < (user.getHeight() - 102)) {
	                	nomalData = context.getString(R.string.srgddfz) ;
	                }else if ((user.getHeight()  - 102) <= weight.getBoneContent() && weight.getBoneContent() <= (user.getHeight()  - 96)){
	                	nomalData = context.getString(R.string.suggest) ;
	                }else{
	                	nomalData = context.getString(R.string.jqdl);
	                }
	                
	            }else{
	                
	                if (weight.getWeight() < (user.getHeight()  - 115)) {
	                	nomalData = context.getString(R.string.srgddfz);
	                }else if ((user.getHeight() - 115) <= weight.getBoneContent() && weight.getBoneContent() <= (user.getHeight() - 100)){
	                	nomalData = context.getString(R.string.suggest);
	                }else{
	                	nomalData = context.getString(R.string.jqdl) ;
	                }
	                
	            }
	            break;
	        case 7:
	            if (weight.getBmi() < 18.5) {
	            	nomalData = context.getString(R.string.srgddfz);
	            }else if (18.5 <= weight.getBmi() && weight.getBmi() <= 25.0){
	            	nomalData = context.getString(R.string.suggest);
	            }else{
	            	nomalData = context.getString(R.string.jqdl);
	            }
	            break;
	        default:
	            break;
	    }
		 
		 return nomalData ;
		
	}
	
	
	public static String setNomalData(User user,int type){
		
		String nomalData = "" ;
		switch (type) {
	        case 0:
	            //脂肪含量
	        	nomalData = "3.0 ~ 19.0";
	            break;
	        case 1:
	            //卡路里
	        	nomalData = "1400.0 ~ 1600.0";
	            break;
	        case 2:
	            //水分含量
	            if (user.getGender() == 0) {
	            	nomalData = "52.0 ~ 60.0";
	            }else{
	            	nomalData = "56.0 ~ 62.0";
	            }
	            break;
	        case 3:
	            //肌肉含量
	            if (user.getGender() == 0) {
	            	nomalData = "31.0 ~ 34.9";
	            }else{
	            	nomalData = "26.0 ~ 27.9";
	            }
	            break;
	        case 4:
	            //内脏脂肪含量
	        	nomalData = "1.0 ~ 9.0";
	            break;
	        case 5:
	            //骨骼
	            if (user.getGender() == 0) {
	            	nomalData = "2.6 ~ 3.1";
	            }else{
	                nomalData = "1.9 ~ 2.4";
	            }
	            break;
	        case 6:
	            //体重
	            if (user.getGender() == 0) {
	            	nomalData  = (int)(user.getHeight() - 102)+"~"+(int)(user.getHeight() - 96);
	            }else{
	            	nomalData  = (int)(user.getHeight() - 115)+"~"+(int)(user.getHeight() - 100);
	            }
	            break;
	        case 7:
	            //BMI
	        	nomalData = "18.5 ~ 25.0";
	            break;
	        default:
	            break;
	    }
		return nomalData;
	}
	
	public static float changeKGtoLBWithKG(float data_kg){
	    float data_lb = (float) (data_kg * 2.2046);
	    return data_lb;
	}
	
	public static float changeLBtoKGWithLB(float data_lb){
	    // 1kg = 2.205 lb
	    float data_kg = (float) (data_lb * 0.4536);
	    return data_kg;
	}
	
	//计算肌肉含量
	public static String calculateMuscleContentWithUser(Context context,User user,WeightResult weightResult){
		String muscleContent_status = "" ;
	    if (user.getGender() == 0) {
	        //男
	        if (weightResult.getMuscleContent() <= 30.9) {
	           // int offset  = (int)(31 - weightResult.getMuscleContent());
	        	muscleContent_status = context.getString(R.string.diyu);
	        }else if(weightResult.getMuscleContent() >= 31.0 && weightResult.getMuscleContent() <= 34.9){
	        	muscleContent_status = context.getString(R.string.biaozhun);
	        }else if(weightResult.getMuscleContent() >= 35.0 && weightResult.getMuscleContent() <= 38.9){
	        	muscleContent_status = context.getString(R.string.piangao);
	        }else if(weightResult.getMuscleContent() >= 39.0){
	            //int offset  = (int)(weightResult.getMuscleContent() - 39);
	        	muscleContent_status = context.getString(R.string.gaoyu);
	        }
	    }else if (user.getGender() == 1){
	        //女
	        if (weightResult.getMuscleContent() <= 25.9) {
	           // int offset  = (int)(26 - weightResult.getMuscleContent());
	        	muscleContent_status = context.getString(R.string.diyu);
	        }else if(weightResult.getMuscleContent() >= 26.0 && weightResult.getMuscleContent() <= 27.9){
	        	muscleContent_status = context.getString(R.string.biaozhun) ;
	        }else if(weightResult.getMuscleContent() >= 28.0 && weightResult.getMuscleContent() <= 29.9){
	        	muscleContent_status = context.getString(R.string.piangao) ;
	        }else if(weightResult.getMuscleContent() >= 30.0){
	           // int offset  = (int)(weightResult.getMuscleContent() - 30);
	        	muscleContent_status = context.getString(R.string.gaoyu);
	        }
	    }
	    return muscleContent_status;
	}
	
	//计算内脏脂肪含量
	public static String calculateVisceralFatContentWithUser(Context context,User user,WeightResult weightResult){
		String visceralFatContent_status ="";
	    if(weightResult.getVisceralFatContent()< 10){
	    	visceralFatContent_status = context.getString(R.string.biaozhun) ;
	    }
	    else if(weightResult.getVisceralFatContent() >= 10 && weightResult.getVisceralFatContent() < 15){
	    	visceralFatContent_status = context.getString(R.string.piangao) ;
	    }else if(weightResult.getVisceralFatContent() >= 15){
	    	visceralFatContent_status = context.getString(R.string.gaoyu) ;
	    }
	    return visceralFatContent_status;
	}
	//计算骨骼含量
	public static String calculateBoneContentWithUser(Context context,User user,WeightResult weightResult){
		String boneContent_status = "" ;
	    if (user.getGender() == 0) {
	        //男
	        if (weightResult.getBoneContent() < 2.6) {
	            boneContent_status = context.getString(R.string.diyu) ;
	        }else if(weightResult.getBoneContent() >= 2.6 && weightResult.getBoneContent() <= 3.1){
	        	boneContent_status = context.getString(R.string.biaozhun);
	        }else if(weightResult.getBoneContent() > 3.1){
	            boneContent_status = context.getString(R.string.gaoyu) ;
	        }
	    }else if (user.getGender() == 1){
	        //女
	        if (weightResult.getBoneContent() < 1.9) {
	           // int offset  = (int)(26 - weightResult.getBoneContent());
	        	boneContent_status = context.getString(R.string.diyu) ;
	        }else if(weightResult.getBoneContent() >= 1.9 && weightResult.getBoneContent() <= 2.4){
	        	boneContent_status = context.getString(R.string.biaozhun) ;
	        }else if(weightResult.getBoneContent() > 2.4){
	            int offset  = (int)(weightResult.getBoneContent() - 30);
	            boneContent_status = context.getString(R.string.gaoyu) ;
	        }
	    }
	    return boneContent_status;
	}
	
	//计算脂肪含量
	public static String calculateFatContentWithUser(Context context,User user,WeightResult weightResult){
		
	    String  fatContent_status = "";
	    if (user.getGender() == 0) {
	        //男
	        if (user.getAge() <= 17) {
	            if (weightResult.getFatContent() <= 15.0) {
	            	fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 15.0 && weightResult.getFatContent() <= 22.0){
	            	fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 22.0 && weightResult.getFatContent() <= 26.4){
	            	fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 26.4){
	            	fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 17 && user.getAge() <= 30) {
	            if (weightResult.getFatContent() <= 15.4) {
	            	fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 15.4 && weightResult.getFatContent() <= 23.0){
	            	fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 23.0 && weightResult.getFatContent() <= 27.0){
	            	fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 27.0){
	            	fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 30 && user.getAge() <= 40) {
	            if (weightResult.getFatContent() <= 16.0) {
	            	fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 16.0 && weightResult.getFatContent() <= 23.4){
	            	fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 23.4 && weightResult.getFatContent() <= 27.4){
	            	fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 27.4){
	            	fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 40 && user.getAge()<= 60) {
	            if (weightResult.getFatContent() <= 16.4) {
	            	fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 16.4 && weightResult.getFatContent() <= 24.0){
	            	fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 24.0 && weightResult.getFatContent() <= 28.0){
	            	fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 28.0){
	            	fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 60 && user.getAge() <= 99) {
	            if (weightResult.getFatContent() <= 17.0) {
	            	fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 17.0 && weightResult.getFatContent() <= 24.4){
	            	fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 24.4 && weightResult.getFatContent() <= 28.4){
	            	fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 28.4){
	            	fatContent_status = context.getString(R.string.feipang);
	            }
	        }
	        
	    }else if (user.getGender() == 1){
	       //nv
	        if (user.getGender() <= 17) {
	            if (weightResult.getFatContent() <= 12.0) {
	                fatContent_status = context.getString(R.string.piandi) ;
	            }else if(weightResult.getFatContent() > 12.0 && weightResult.getFatContent() <= 17.0){
	                fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 17.0 && weightResult.getFatContent() <= 22.0){
	                fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 22.0){
	                fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 17 && user.getAge() <= 30) {
	            if (weightResult.getFatContent() <= 12.4) {
	                fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 12.4 && weightResult.getFatContent() <= 18.0){
	                fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 18.0 && weightResult.getFatContent() <= 23.0){
	                fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 23.0){
	                fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 30 && user.getAge() <= 40) {
	            if (weightResult.getFatContent() <= 13.0) {
	                fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 13.0 && weightResult.getFatContent() <= 18.4){
	                fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 18.4 && weightResult.getFatContent() <= 23.0){
	                fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 23.0){
	                fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 40 && user.getAge() <= 60) {
	            if (weightResult.getFatContent() <= 13.4) {
	                fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 13.4 && weightResult.getFatContent() <= 19.0){
	                fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 19.0 && weightResult.getFatContent() <= 23.4){
	                fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 23.4){
	                fatContent_status = context.getString(R.string.feipang);
	            }
	        }else if (user.getAge() > 60 && user.getAge() <= 99) {
	            if (weightResult.getFatContent() <= 14.0) {
	                fatContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getFatContent() > 14.0 && weightResult.getFatContent() <= 19.4){
	                fatContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getFatContent() > 19.4 && weightResult.getFatContent() <= 24.0){
	                fatContent_status = context.getString(R.string.piangao);
	            }else if(weightResult.getFatContent() > 24.0){
	                fatContent_status = context.getString(R.string.feipang);
	            }
	        }
	    }
	    
	    return fatContent_status;
	}

	 //计算水分含量
	public static String calculateWaterContentWithUser(Context context,User user,WeightResult weightResult){
	    String  waterContent_status = "";
	    if (user.getGender() == 0) {
	        //男
	        if (user.getAge() <= 17) {
	            if (weightResult.getWaterContent() < 54.0) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 54.0 && weightResult.getWaterContent() <= 60.0){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 60.0){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() > 17 && user.getAge() <= 30) {
	            if (weightResult.getWaterContent() < 53.5) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 53.5 && weightResult.getWaterContent() <= 59.5){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 59.5){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() > 30 && user.getAge() <= 40) {
	            if (weightResult.getWaterContent() < 53.0) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 53.0 && weightResult.getWaterContent() <= 59.0){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 59.0){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() > 40 && user.getAge() <= 60) {
	            if (weightResult.getWaterContent() < 52.5) {
	                waterContent_status =  context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 52.5 && weightResult.getWaterContent() <= 58.5){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 58.5){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() > 60 && user.getAge() <= 99) {
	            if (weightResult.getWaterContent() < 52.0) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 52.0 && weightResult.getWaterContent() <= 58.0){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 58.0){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }
	        
	    }else if (user.getGender() == 1){
	        //nv
	        if (user.getAge() <= 17) {
	            if (weightResult.getWaterContent() < 57.0) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 57.0 && weightResult.getWaterContent() <= 62.0){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 62.0){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() > 17 && user.getAge() <= 30) {
	            if (weightResult.getWaterContent() < 56.5) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 56.5 && weightResult.getWaterContent() <= 61.5){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 61.5){
	                waterContent_status = context.getString(R.string.piangao) ;
	            }
	        }else if (user.getAge() > 30 && user.getAge() <= 40) {
	            if (weightResult.getWaterContent() < 56.0) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 56.0 && weightResult.getWaterContent() <= 61.0){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 61.0){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() > 40 && user.getAge() <= 60) {
	            if (weightResult.getWaterContent() < 55.5) {
	                waterContent_status = context.getString(R.string.piandi) ;
	            }else if(weightResult.getWaterContent() >= 55.5 && weightResult.getWaterContent() <= 60.5){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 60.5){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() > 60 && user.getAge() <= 99) {
	            if (weightResult.getWaterContent() < 55.0) {
	                waterContent_status = context.getString(R.string.piandi);
	            }else if(weightResult.getWaterContent() >= 55.0 && weightResult.getWaterContent() <= 60.0){
	                waterContent_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getWaterContent() > 60.0){
	                waterContent_status = context.getString(R.string.piangao);
	            }
	        }
	    }
	    return waterContent_status;
	}
	
	//计算BMI
	public static String calculateBMIWithUser(Context context,User user,WeightResult weightResult){
	    String  BMI_status = "";
	    if (user.getGender() == 0) {
	        if(weightResult.getBmi() < 18.5){
	            BMI_status = context.getString(R.string.pianshou);
	        }else if(weightResult.getBmi()>= 18.5 && weightResult.getBmi()< 25){
	            BMI_status = context.getString(R.string.biaozhun);
	        }else if(weightResult.getBmi()  >= 25 && weightResult.getBmi() < 30){
	            BMI_status = context.getString(R.string.pianpang);
	        }else if(weightResult.getBmi() >= 30 && weightResult.getBmi() < 35){
	            BMI_status = context.getString(R.string.feipang);
	        }else if(weightResult.getBmi() >= 35){
	            BMI_status = context.getString(R.string.jidu_feipang);
	        }
	    }else if (user.getGender() == 1){
	        if(weightResult.getBmi() < 18.5){
	            BMI_status = context.getString(R.string.pianshou);
	        }else if(weightResult.getBmi() >= 18.5 && weightResult.getBmi() < 25){
	            BMI_status = context.getString(R.string.biaozhun);
	        }else if(weightResult.getBmi() >= 25 && weightResult.getBmi() < 30){
	            BMI_status = context.getString(R.string.pianpang);
	        }else if(weightResult.getBmi() >= 30 && weightResult.getBmi() < 35){
	            BMI_status = context.getString(R.string.feipang);
	        }else if(weightResult.getBmi() >= 35){
	            BMI_status = context.getString(R.string.jidu_feipang);
	        }
	    }
	    return BMI_status;
	}
	 //计算卡路里
	public static String calculateCalorieWithUser(Context context,User user,WeightResult weightResult){
		
	    String Calorie_status = "";
	    if (user.getGender() == 0) {
	        if (user.getAge() <2) {
	            if (weightResult.getCalorie() < 650) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 650 && weightResult.getCalorie() <= 750){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 750){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 3 && user.getAge() < 6){
	            if (weightResult.getCalorie() < 810) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 810 && weightResult.getCalorie() <= 910){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 910){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 6 && user.getAge() < 9){
	            if (weightResult.getCalorie() < 950) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 950 && weightResult.getCalorie() <= 1050){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1050){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 9 && user.getAge() < 12){
	            if (weightResult.getCalorie() < 1130) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1130 && weightResult.getCalorie() <= 1230){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1230){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 12 && user.getAge() < 15){
	            if (weightResult.getCalorie() < 1290) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1290 && weightResult.getCalorie() <= 1390){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1390){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 15 && user.getAge() < 18){
	            if (weightResult.getCalorie() < 1250) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1250 && weightResult.getCalorie() <= 1350){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1350){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 18 && user.getAge() < 30){
	            if (weightResult.getCalorie() < 1160) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1160 && weightResult.getCalorie() <= 1260){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1260){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 30 && user.getAge() < 50){
	            if (weightResult.getCalorie() < 1120) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1120 && weightResult.getCalorie() <= 1220){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1220){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 50 && user.getAge() <70){
	            if (weightResult.getCalorie() < 1060) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1060 && weightResult.getCalorie() <= 1160){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1160){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 70){
	            if (weightResult.getCalorie() < 960) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 960 && weightResult.getCalorie() <= 1060){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1060){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }
	    }else if (user.getGender() ==1){
	        if (user.getAge() <2) {
	            if (weightResult.getCalorie() < 650) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 650 && weightResult.getCalorie() <= 750){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 750){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 3 && user.getAge() < 6){
	            if (weightResult.getCalorie() < 850) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 850 && weightResult.getCalorie() <= 950){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 950){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 6 && user.getAge() < 9){
	            if (weightResult.getCalorie() < 1040) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1040 && weightResult.getCalorie() <= 1140){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1140){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 9 && user.getAge() < 12){
	            if (weightResult.getCalorie() < 1240) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1240 && weightResult.getCalorie() <= 1340){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1340){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 12 && user.getAge() < 15){
	            if (weightResult.getCalorie() < 1430) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1430 && weightResult.getCalorie() <= 1530){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1530){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 15 && user.getAge() < 18){
	            if (weightResult.getCalorie() < 1560) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1560 && weightResult.getCalorie() <= 1660){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1660){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 18 && user.getAge() < 30){
	            if (weightResult.getCalorie() < 1500) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1500 && weightResult.getCalorie() <= 1600){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1600){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 30 && user.getAge() < 50){
	            if (weightResult.getCalorie() < 1450) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1450 && weightResult.getCalorie() <= 1550){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1550){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 50 && user.getAge() <70){
	            if (weightResult.getCalorie() < 1300) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1300 && weightResult.getCalorie() <= 1400){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1400){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }else if (user.getAge() >= 70){
	            if (weightResult.getCalorie() < 1270) {
	                Calorie_status = context.getString(R.string.piandi);
	            }else if(weightResult.getCalorie() >= 1270 && weightResult.getCalorie() <= 1370){
	                Calorie_status = context.getString(R.string.biaozhun);
	            }else if(weightResult.getCalorie() > 1370){
	                Calorie_status = context.getString(R.string.piangao);
	            }
	        }
	    }
	    
	    return Calorie_status;
	}

}
