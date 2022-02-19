package com.mes.loco.aps.server.service.mesenum;

public enum SFCQualityLossBigType {
	/**
	 * 默认
	 */
	Default(0, ""),
	/**
	 * 报废
	 */
	Scrap(1, "001"),
	/**
	 * 返工返修
	 */
	Repair(2, "002"),
	/**
	 * 停产
	 */
	StopProduct(3, "003"),
	/**
	 * 内部质量收入
	 */
	InnerQualityIn(4, "004");

	private int value;
	private String lable;

	private SFCQualityLossBigType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCQualityLossBigType getEnumType(int val) {
		for (SFCQualityLossBigType type : SFCQualityLossBigType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
