package com.mes.loco.aps.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.cfg.CFGItem;

/**
 * 领料需求订单类型
 * 
 * @author YouWang·Peng
 * @CreateTime 2022-1-6 10:13:30
 *
 */
public enum WMSOrderType {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 产线领料订单
	 */
	LineOrder(100, "产线领料订单"),
	/**
	 * 换料领料订单
	 */
	ChangeOrder(101, "换料领料订单");

	private int value;
	private String lable;

	private WMSOrderType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static WMSOrderType getEnumType(int val) {
		for (WMSOrderType type : WMSOrderType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return WMSOrderType.Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (WMSOrderType type : WMSOrderType.values()) {
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
