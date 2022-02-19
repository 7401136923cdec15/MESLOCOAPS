package com.mes.loco.aps.server.service.mesenum;

import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.po.cfg.CFGItem;

public enum MBSRoleTree {
	/**
	 * 默认
	 */
	Default(0, ""),
	/**
	 * 定额工时调整
	 */
	AdjustHour(220000, "定额工时调整"),
	/**
	 * 计划提交
	 */
	PlanSubmit(300100, "计划提交"),
	/**
	 * 月计划审批一
	 */
	MonthAuditOne(300101, "月计划审批一"),
	/**
	 * 月计划审批二
	 */
	MonthAuditTwo(300102, "月计划审批二"),
	/**
	 * 月计划审批三
	 */
	MonthAuditThree(300103, "月计划审批三"),
	/**
	 * 周计划审批一
	 */
	WeekAuditOne(300104, "周计划审批一"),
	/**
	 * 周计划审批二
	 */
	WeekAuditTwo(300105, "周计划审批二"),
	/**
	 * 周计划审批三
	 */
	WeekAuditThree(300106, "周计划审批三"),
	/**
	 * 日计划提交
	 */
	DayCreate(300108, "日计划提交"),
	/**
	 * 日计划审批
	 */
	DayAudit(300107, "日计划审批");

	private int value;
	private String lable;

	private MBSRoleTree(int value, String lable) {
		this.value = value;
		this.lable = lable;
	}

	/**
	 * 通过 value 的数值获取枚举实例
	 *
	 * @param val
	 * @return
	 */
	public static MBSRoleTree getEnumType(int val) {
		for (MBSRoleTree type : MBSRoleTree.values()) {
			if (type.getValue() == val) {
				return type;
			}
		}
		return null;
	}

	public static List<CFGItem> getEnumList() {
		List<CFGItem> wItemList = new ArrayList<CFGItem>();

		for (MBSRoleTree type : MBSRoleTree.values()) {
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
