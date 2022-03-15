package com.mes.loco.aps.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.cfg.CFGItem;

public enum MSSMaterialType {
	/**
	 * 默认
	 */
	Default(0, ""),
	/**
	 * 定额工时调整
	 */
	MainMaterial(1, "主料"),
	/**
	 * 计划提交
	 */
	Accessories(2, "辅料");

	private int value;
	private String lable;

	private MSSMaterialType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static MSSMaterialType getEnumType(int val) {
		for (MSSMaterialType type : MSSMaterialType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return null;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (MSSMaterialType type : MSSMaterialType.values()) {
			CFGItem wItem = new CFGItem();
			wItem.ID = type.getValue();
			wItem.ItemName = type.getLable();
			wItem.ItemText = type.getLable();
			wItemList.add(wItem);
		}
		return wItemList;
	}

	public int getValue() {
		return value;
	}

	public String getLable() {
		return lable;
	}
}
