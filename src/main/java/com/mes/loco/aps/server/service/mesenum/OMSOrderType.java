package com.mes.loco.aps.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.cfg.CFGItem;

/**
 * 订单类型
 */
public enum OMSOrderType {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 车体
	 */
	CheTi(1, "车体"),
	/**
	 * 标准BOM
	 */
	GuanLu(2, "管路"),
	/**
	 * 变压器
	 */
	BianYaQi(3, "变压器");

	private int value;
	private String lable;

	private OMSOrderType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static OMSOrderType getEnumType(int val) {
		for (OMSOrderType type : OMSOrderType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (OMSOrderType type : OMSOrderType.values()) {
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
