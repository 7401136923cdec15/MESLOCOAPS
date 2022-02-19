package com.mes.loco.aps.server.service.mesenum;

import com.mes.loco.aps.server.service.mesenum.SFCSequentialInfoType;

/**
 * 电子履历类型枚举
 */
public enum SFCSequentialInfoType {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 机车进厂
	 */
	Inplant(1, "机车进厂"),
	/**
	 * 工位任务
	 */
	StationTask(2, "工位任务"),
	/**
	 * 工序任务
	 */
	StepTask(3, "工序任务"),
	/**
	 * 开工打卡
	 */
	ClockIn(4, "开工打卡"),
	/**
	 * 预检
	 */
	PreCheck(5, "预检"),
	/**
	 * 自检
	 */
	SelfCheck(6, "自检"),
	/**
	 * 互检
	 */
	MutualCheck(7, "互检"),
	/**
	 * 专检
	 */
	SpecialCheck(8, "专检"),
	/**
	 * 不合格评审
	 */
	NCR(9, "不合格评审"),
	/**
	 * 返修
	 */
	Repair(10, "返修"),
	/**
	 * 终检
	 */
	FinalCheck(11, "终检"),
	/**
	 * 竣工确认
	 */
	CompleteConfirm(12, "竣工确认"),
	/**
	 * 出厂检
	 */
	OutCheck(13, "出厂检"),
	/**
	 * 机车出厂
	 */
	OutPlant(14, "机车出厂"),
	/**
	 * 异常
	 */
	Exception(15, "异常"),
	/**
	 * 收到电报
	 */
	Telegraph(16, "收到电报"),
	/**
	 * 移车
	 */
	MoveCar(17, "移车"),
	/**
	 * 预检问题项
	 */
	ProblemCheck(18, "预检问题项"),
	/**
	 * 完工打卡
	 */
	ClockOut(19, "完工打卡"),
	/**
	 * 出厂申请
	 */
	OutApply(20, "出厂申请");

	private int value;
	private String lable;

	SFCSequentialInfoType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	public static SFCSequentialInfoType getEnumType(int val) {
		for (SFCSequentialInfoType type : SFCSequentialInfoType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public int getValue() {
		return this.value;
	}

	public String getLable() {
		return this.lable;
	}
}