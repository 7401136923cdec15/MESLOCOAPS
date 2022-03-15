package com.mes.loco.aps.server.service.mesenum;

/**
 * 物料需求计划状态
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-3-14 09:19:37
 */
public enum MRPMaterialPlanStatus {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 保存
	 */
	Save(1, "保存"),
	/**
	 * 系统生成
	 */
	SystemSubmit(2, "系统生成"),
	/**
	 * 手动生成
	 */
	HandSubmit(3, "手动生成");

	private int value;
	private String lable;

	private MRPMaterialPlanStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static MRPMaterialPlanStatus getEnumType(int val) {
		for (MRPMaterialPlanStatus type : MRPMaterialPlanStatus.values()) {
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
