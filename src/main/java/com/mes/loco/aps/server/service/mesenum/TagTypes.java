package com.mes.loco.aps.server.service.mesenum;

public enum TagTypes {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 接收
	 */
	Dispatcher(1, "接收"),
	/**
	 * 发起
	 */
	Applicant(2, "发起"),
	/**
	 * 确认
	 */
	Confirmer(3, "确认"),
	/**
	 * 审批
	 */
	Approver(4, "审批");

	private int value;
	private String lable;

	private TagTypes(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static TagTypes getEnumType(int val) {
		for (TagTypes type : TagTypes.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return TagTypes.Default;
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
