/**
 * 项目名称：PublcNumber
 * 文件名：MediaInfo.java 
 * 2015-3-2-下午4:35:46
 * 2015 万家恒通公司-版权所有
 * @version 1.0.0
 */
package com.wearme.zfc.bean;

import java.io.Serializable;

/**
 * 
 * @description:
 *
 * author : liujw
 * modify : 
 * 2015-3-2 下午4:35:46
 *
 * 
 */
public class MediaInfo implements Serializable {

	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;
	
	private String filePath ;
	
	private String fileName ;
	
	private String fileThumb ;
	
	private int type ; // 0:picture 1:video
	
	private boolean isSelect ;
	
	public String getFileThumb() {
		return fileThumb;
	}

	public void setFileThumb(String fileThumb) {
		this.fileThumb = fileThumb;
	}
	
	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	/**
	 * type
	 *
	 * @return  the type
	 * @since   1.0.0
	 */
	
	public int getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * filePath
	 *
	 * @return  the filePath
	 * @since   1.0.0
	 */
	
	public String getFilePath() {
		return filePath;
	}

	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * fileName
	 *
	 * @return  the fileName
	 * @since   1.0.0
	 */
	
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
}
