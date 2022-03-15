package com.mes.loco.aps.server.service.mesenum;

public enum APSReplaceType {
	/**
	 * 默认
	 */
	Default(0, ""),
	/**
	 * 正常关闭
	 */
	MustChange(1, "必换"),
	/**
	 * 异常关闭
	 */
	OuChange(2, "偶换");

	private int value;
	private String lable;

	private APSReplaceType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static APSReplaceType getEnumType(int val) {
		for (APSReplaceType type : APSReplaceType.values()) {
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
