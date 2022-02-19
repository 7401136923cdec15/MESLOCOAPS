package com.mes.loco.aps.server.service.mesenum;

public enum SCHSecondmentApplyStatus {
	Default(0, "默认"),
	/**
	 * 已确认
	 */
	NomalClose(20, "已确认"),
	/**
	 * 撤销
	 */
	Canceled(21, "撤销"),
	/**
	 * 已驳回
	 */
	ExceptionClose(22, "已驳回");

	private int value;
	private String lable;

	private SCHSecondmentApplyStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SCHSecondmentApplyStatus getEnumType(int val) {
		for (SCHSecondmentApplyStatus type : SCHSecondmentApplyStatus.values()) {
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
