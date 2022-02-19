package com.mes.loco.aps.server.service.mesenum;

public enum SFCBOMTaskStatus {
	/**
	 * SCH
	 */
	Default(0, "默认"),
	/**
	 * 正常关闭
	 */
	NomalClose(20, "已完成"),
	/**
	 * 异常关闭
	 */
	ExceptionClose(21, "已驳回"),
	/**
	 * 已撤销
	 */
	Canceled(22, "已撤销");

	private int value;
	private String lable;

	private SFCBOMTaskStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCBOMTaskStatus getEnumType(int val) {
		for (SFCBOMTaskStatus type : SFCBOMTaskStatus.values()) {
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
