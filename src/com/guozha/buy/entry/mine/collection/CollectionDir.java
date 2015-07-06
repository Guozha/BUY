package com.guozha.buy.entry.mine.collection;

import java.io.Serializable;
import java.util.List;

/**
 * 菜谱收藏的文件夹
 * @author PeggyTong
 *
 */
public class CollectionDir implements Serializable{
	
	private static final long serialVersionUID = -1511298635740569775L;
	
	private int myDirId;				//文件夹id
	private String defaultFlag;			//文件夹标识（1代表默认文件夹)
	private String dirName;				//文件夹名称
	private List<CollectionMenu> menuInfoList;
	
	public int getMyDirId() {
		return myDirId;
	}
	public void setMyDirId(int myDirId) {
		this.myDirId = myDirId;
	}
	public String getDefaultFlag() {
		return defaultFlag;
	}
	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
	public String getDirName() {
		return dirName;
	}
	public void setDirName(String dirName) {
		this.dirName = dirName;
	}
	public List<CollectionMenu> getMenuInfoList() {
		return menuInfoList;
	}
	public void setMenuInfoList(List<CollectionMenu> menuInfoList) {
		this.menuInfoList = menuInfoList;
	}
}
