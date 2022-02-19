package com.mes.loco.aps.server.service.mesenum;

import com.mes.loco.aps.server.service.mesenum.SFCTaskType;
import com.mes.loco.aps.server.service.po.cfg.CFGItem;
import java.util.ArrayList;
import java.util.List;

public enum SFCTaskType {
	Default(0, "默认"), StationSpotCheck(

			1, "操作点检"),
	StationBeforeShift(

			2, "班前"),
	StationAfterShift(

			3, "班后"),
	DeviceSpotCheck(

			4, "设备点检"),
	ProduceSpotCheck(

			5, "生产巡检"),
	SCRecheck(

			11, "生产复测"),
	QualityCheck(

			7, "QualityCheck"),
	QualityOnSiteCheck(

			8, "质量巡检"),
	TechOnSiteCheck(

			9, "工艺巡检"),
	ToolOnSiteCheck(

			10, "计量检查"),
	SelfCheck(

			6, "自检"),
	MutualCheck(

			12, "互检"),
	SpecialCheck(

			13, "专检"),
	PreCheck(

			14, "预检"),
	Final(

			15, "终检"),
	OutPlant(

			16, "出厂检");

	private int value;
	private String lable;

	SFCTaskType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	public static SFCTaskType getEnumType(int val) {
		byte b;
		int i;
		SFCTaskType[] arrayOfSFCTaskType;
		for (i = (arrayOfSFCTaskType = values()).length, b = 0; b < i;) {
			SFCTaskType type = arrayOfSFCTaskType[b];
			if (type.getValue() == val)
				return type;
			b++;
		}

		return Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<>();
		byte b;
		int i;
		SFCTaskType[] arrayOfSFCTaskType;
		for (i = (arrayOfSFCTaskType = values()).length, b = 0; b < i;) {
			SFCTaskType type = arrayOfSFCTaskType[b];
			CFGItem wItem = new CFGItem();
			wItem.ID = type.getValue();
			wItem.ItemName = type.getLable();
			wItem.ItemText = type.getLable();
			wItemList.add(wItem);
			b++;
		}

		return wItemList;
	}

	public int getValue() {
		return this.value;
	}

	public String getLable() {
		return this.lable;
	}
}
