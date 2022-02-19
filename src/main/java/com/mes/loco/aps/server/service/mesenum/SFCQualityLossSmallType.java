package com.mes.loco.aps.server.service.mesenum;

public enum SFCQualityLossSmallType {
	/**
	 * 默认
	 */
	Default(0, ""),
	/**
	 * 设计差错
	 */
	DesignError(1, "01"),
	/**
	 * 工艺差错
	 */
	TechError(2, "02"),
	/**
	 * 制造差错
	 */
	ProductError(3, "03"),
	/**
	 * 供方原因
	 */
	ProviderReason(4, "04"),
	/**
	 * 其他原因
	 */
	OtherReason(5, "05");

	private int value;
	private String lable;

	private SFCQualityLossSmallType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCQualityLossSmallType getEnumType(int val) {
		for (SFCQualityLossSmallType type : SFCQualityLossSmallType.values()) {
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
