package com.mes.loco.aps.server.service.mesenum;

public enum SFCRepairItemResult {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 未完成
	 */
	NotFinish(1, "未完成"),
	/**
	 * 提交
	 */
	Submited(2, "提交"),
	/**
	 * 同意试运
	 */
	Agreen(3, "同意试运"),
	/**
	 * 不同意试运
	 */
	NotAgreen(4, "不同意试运"),
	/**
	 * 已完成
	 */
	Finished(5, "已完成"),
	/**
	 * 已取消
	 */
	Canceled(6, "已取消");

	private int value;
	private String lable;

	private SFCRepairItemResult(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SFCRepairItemResult getEnumType(int val) {
		for (SFCRepairItemResult type : SFCRepairItemResult.values()) {
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
