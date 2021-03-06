package com.mes.loco.aps.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.cfg.CFGItem;

public enum BMSEmployeeType {
	Default(0, "默认"),
	/**
	 * 正式
	 */
	Regular(1, "正式"),
	/**
	 * 试用
	 */
	Probationary(2, "试用"),
	/**
	 * 实习
	 */
	Interns(3, "实习"),
	/**
	 * 劳务派遣
	 */
	OutSource(4, "劳务派遣"),
	/**
	 * 返聘
	 */
	RaRetirement(5, "返聘"),
	/**
	 * 退休
	 */
	Retirement(6, "退休");

	private int value;
	private String lable;

	private BMSEmployeeType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static BMSEmployeeType getEnumType(int val) {
		for (BMSEmployeeType type : BMSEmployeeType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return null;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (BMSEmployeeType type : BMSEmployeeType.values()) {
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
