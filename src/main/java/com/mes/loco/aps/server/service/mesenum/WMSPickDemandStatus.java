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
public enum WMSPickDemandStatus {
	/**
	 * 默认
	 */
	Default(0, "默认"),
	/**
	 * 保存
	 */
	Saved(1, "保存"),
	/**
	 * 已发送
	 */
	Sended(2, "已发送"),
	/**
	 * 已拣货
	 */
	Picked(3, "已分拣"),
	/**
	 * 已交接
	 */
	Checked(4, "已交接"),
	/**
	 * 已配送
	 */
	Received(5, "已配送"),
	/**
	 * 已取消
	 */
	Canceld(6, "已取消");

	private int value;
	private String lable;

	private WMSPickDemandStatus(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static WMSPickDemandStatus getEnumType(int val) {
		for (WMSPickDemandStatus type : WMSPickDemandStatus.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return WMSPickDemandStatus.Default;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (WMSPickDemandStatus type : WMSPickDemandStatus.values()) {
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
