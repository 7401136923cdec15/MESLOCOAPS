package com.mes.loco.aps.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.cfg.CFGItem;

/**
 * 评估类型
 */
public enum SAPType {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 常规新件
	 */
	CGXJ(1, "常规新件"),
	/**
	 * 修复旧件
	 */
	XFJJ(2, "修复旧件"),
	/**
	 * 可用旧件
	 */
	KYJJ(3, "可用旧件"),
	/**
	 * 高价互换件
	 */
	GJHHJ(4, "高价互换件");

	private int value;
	private String lable;

	private SAPType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static SAPType getEnumType(int val) {
		for (SAPType type : SAPType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (SAPType type : SAPType.values()) {
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
