package com.guozha.buy.entry.market;

import java.util.List;
//单个物品的属性类
public class ItemSaleInfo {

	private String itemId;  //单品ID
	private String skuId;
	private String itemImage;  //单品图片
	private String itemName;   //单品名称
	private float skuPrice;	   
	private String itemUnit;   //单品单位
	private int defaultWeight; //默认重量
	private List<WeightOption> weightOption;  //选择的重量

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getItemImage() {
		return itemImage;
	}

	public void setItemImage(String itemImage) {
		this.itemImage = itemImage;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public float getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(float skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getItemUnit() {
		return itemUnit;
	}

	public void setItemUnit(String itemUnit) {
		this.itemUnit = itemUnit;
	}

	public int getDefaultWeight() {
		return defaultWeight;
	}

	public void setDefaultWeight(int defaultWeight) {
		this.defaultWeight = defaultWeight;
	}

	public List<WeightOption> getWeightOption() {
		return weightOption;
	}

	public void setWeightOption(List<WeightOption> weightOption) {
		this.weightOption = weightOption;
	}

	public static class WeightOption {
		private int quantity;
		private float amount;
		private String unit;
		private String moneyUnit;

		public String getUnit() {
			return unit;
		}

		public void setUnit(String unit) {
			this.unit = unit;
		}

		public String getMoneyUnit() {
			return moneyUnit;
		}

		public void setMoneyUnit(String moneyUnit) {
			this.moneyUnit = moneyUnit;
		}

		public int getQuantity() {
			return quantity;
		}

		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}

		public float getAmount() {
			return amount;
		}

		public void setAmount(float amount) {
			this.amount = amount;
		}

	}

}
