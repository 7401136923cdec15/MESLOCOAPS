package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 日计划审批表
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-6-3 15:05:03
 * @LastEditTime 2020-6-3 15:05:08
 *
 */
public class APSDayPlanAudit implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 主键
	 */
	public int ID = 0;
	/**
	 * 工区ID
	 */
	public int AreaID = 0;
	/**
	 * 工区名称
	 */
	public String AreaName = "";
	/**
	 * 班次
	 */
	public int ShiftID = 0;
	/**
	 * 创建人ID
	 */
	public int CreateID = 0;
	/**
	 * 创建人名称
	 */
	public String Creator = "";
	/**
	 * 创建时刻
	 */
	public Calendar CreateTime = Calendar.getInstance();
	/**
	 * 审批人ID
	 */
	public int AuditID = 0;
	/**
	 * 审批人名称
	 */
	public String Auditor = "";
	/**
	 * 审批时刻
	 */
	public Calendar AuditTime = Calendar.getInstance();
	/**
	 * 审批状态
	 */
	public int Status = 0;

	public APSDayPlanAudit() {
	}
	
	public APSDayPlanAudit(int iD, int areaID, String areaName, int shiftID, int createID, String creator,
			Calendar createTime, int auditID, String auditor, Calendar auditTime, int status) {
		super();
		ID = iD;
		AreaID = areaID;
		AreaName = areaName;
		ShiftID = shiftID;
		CreateID = createID;
		Creator = creator;
		CreateTime = createTime;
		AuditID = auditID;
		Auditor = auditor;
		AuditTime = auditTime;
		Status = status;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}
}
