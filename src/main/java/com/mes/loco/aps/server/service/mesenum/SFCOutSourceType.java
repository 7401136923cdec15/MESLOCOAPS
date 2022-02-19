package com.mes.loco.aps.server.service.mesenum;

public enum SFCOutSourceType {
	/**
	 * 
	 */
	Default(0, ""),
	/**
	 * 必修
	 */
	WWBX(1, "委外必修"),
	/**
	 * 偶修
	 */
	WWOX(2, "委外偶修"),
	/**
	 * 委外必修
	 */
	ZXBX(3, "自修必修"),
	/**
	 * 委外偶修
	 */
	ZXOX(4, "自修偶修");

	private int value;
	private String lable;

	private SFCOutSourceType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCOutSourceType getEnumType(int val) {
		for (SFCOutSourceType type : SFCOutSourceType.values()) {
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
