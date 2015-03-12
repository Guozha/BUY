package com.guozha.buy.entry.cart;

/**
 * 菜谱的材料
 * @author PeggyTong
 *
 */
public class CartCookMaterial{

	private String materialId;   
	private String materialName;  //材料名称
	private String materialCount;  //材料数量
	private String materialUnit;   //材料单位
	
	public CartCookMaterial(String materialId, String materialName,
			String materialCount, String materialUnit) {
		super();
		this.materialId = materialId;
		this.materialName = materialName;
		this.materialCount = materialCount;
		this.materialUnit = materialUnit;
	}
	
	public String getMaterialId() {
		return materialId;
	}
	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	public String getMaterialCount() {
		return materialCount;
	}
	public void setMaterialCount(String materialCount) {
		this.materialCount = materialCount;
	}
	public String getMaterialUnit() {
		return materialUnit;
	}
	public void setMaterialUnit(String materialUnit) {
		this.materialUnit = materialUnit;
	}
}
