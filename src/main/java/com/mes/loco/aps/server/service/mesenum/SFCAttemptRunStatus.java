package com.mes.loco.aps.server.service.mesenum;

/**
 * 试运申请
 */
public enum SFCAttemptRunStatus {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 已确认
	 */
	NomalClose(20, "已确认"),
	/**
	 * 异常关闭
	 */
	ExceptionClose(21, "异常关闭"),
	/**
	 * 撤销
	 */
	Canceled(22, "撤销");

	private int value;
	private String lable;

	private SFCAttemptRunStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCAttemptRunStatus getEnumType(int val) {
		for (SFCAttemptRunStatus type : SFCAttemptRunStatus.values()) {
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
