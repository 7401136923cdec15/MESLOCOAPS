package com.mes.loco.aps.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.cfg.CFGItem;

/**
 * 基础数据检验类型
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-7-5 15:03:03
 * @LastEditTime 2020-7-5 15:03:07
 *
 */
public enum OMSCheckType {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 工艺BOP
	 */
	BOP(1, "工艺BOP"),
	/**
	 * 标准BOM
	 */
	BOM(2, "标准BOM"),
	/**
	 * 物料主数据
	 */
	Material(3, "物料主数据"),
	/**
	 * 检验模板
	 */
	Standard(4, "检验模板");

	private int value;
	private String lable;

	private OMSCheckType(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static OMSCheckType getEnumType(int val) {
		for (OMSCheckType type : OMSCheckType.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (OMSCheckType type : OMSCheckType.values()) {
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
