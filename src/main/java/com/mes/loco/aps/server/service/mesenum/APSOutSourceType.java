package com.mes.loco.aps.server.service.mesenum;

public enum APSOutSourceType {
	/**
	 * 默认
	 */
	Default(0, ""),
	/**
	 * 委外必修
	 */
	WWBX(1, "委外必修"),
	/**
	 * 委外偶修
	 */
	WWOX(2, "委外偶修"),
	/**
	 * 自修必修
	 */
	ZXBX(3, "自修必修"),
	/**
	 * 自修偶修
	 */
	ZXOX(3, "自修偶修"),
	/**
	 * 其他
	 */
	Other(3, "其他");

	private int value;
	private String lable;

	private APSOutSourceType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static APSOutSourceType getEnumType(int val) {
		for (APSOutSourceType type : APSOutSourceType.values()) {
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
