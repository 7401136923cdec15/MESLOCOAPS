package com.mes.loco.aps.server.service.mesenum;

public enum SCHSecondmentApplyType {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 本工区借调
	 */
	TAreaLoan(1, "本工区借调"),
	/**
	 * 跨工区借调
	 */
	KAreaLoan(2, "跨工区借调"),
	/**
	 * 班组内借调
	 */
	ClassLoan(3, "班组内借调");

	private int value;
	private String lable;

	private SCHSecondmentApplyType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SCHSecondmentApplyType getEnumType(int val) {
		for (SCHSecondmentApplyType type : SCHSecondmentApplyType.values()) {
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
