package com.mes.loco.aps.server.service.mesenum;

public enum SCHSecondmentBPMStatus {
	Default(0, "默认"),
	/**
	 * 正常关闭
	 */
	NomalClose(20, "正常关闭"),
	/**
	 * 异常关闭
	 */
	ExceptionClose(21, "异常关闭");

	private int value;
	private String lable;

	private SCHSecondmentBPMStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SCHSecondmentBPMStatus getEnumType(int val) {
		for (SCHSecondmentBPMStatus type : SCHSecondmentBPMStatus.values()) {
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
