package com.mes.loco.aps.server.service.po.aps;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.mes.loco.aps.server.service.utils.StringUtils;

/**
 * 日计划审批表子项
 * 
 * @author YouWang·Peng
 * @CreateTime 2020-6-3 15:05:03
 * @LastEditTime 2020-6-3 15:05:08
 *
 */
public class APSDayPlanAuditItemBPM implements Serializable {
	/**
	 * 序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 工序任务ID
	 */
	public int APSTaskStepID = 0;
	/**
	 * 状态
	 */
	public int Status = 0;
	/**
	 * 备注
	 */
	public String Remark = "";

	public APSDayPlanAuditItemBPM() {
	}

	public String toString() {
		return StringUtils.Format("{0}+|:|+{1}+|:|+{2}", String.valueOf(APSTaskStepID), String.valueOf(Status), Remark);
	}

	public APSDayPlanAuditItemBPM(String wValue) {
		try {
			if (StringUtils.isEmpty(wValue))
				return;
			List<String> wArray = StringUtils.splitList(wValue, "+|:|+");
			if (wArray == null || wArray.size() <= 0) {
				return;
			}
			if (wArray.size() > 0) {
				this.APSTaskStepID = StringUtils.parseInt(wArray.get(0)).intValue();
			}
			if (wArray.size() > 1) {
				this.Status = StringUtils.parseInt(wArray.get(1)).intValue();
			}
			if (wArray.size() > 2) {
				this.Remark = StringUtils.parseString(wArray.get(2));
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}

	public static String ListToString(List<APSDayPlanAuditItemBPM> wItems) {
		String wResult = "";
		
		if (wItems == null || wItems.size() <= 0)
			return wResult;
		List<String> wResultList = new ArrayList<>();
		for (APSDayPlanAuditItemBPM wAPSDayPlanAuditItemBPM : wItems) {
			if (wAPSDayPlanAuditItemBPM == null)
				continue;
			wResultList.add(wAPSDayPlanAuditItemBPM.toString());
		}
		wResult = StringUtils.Join("+|;|+", wResultList);

		return wResult;
	}

	public static List<APSDayPlanAuditItemBPM> StringToList(String wStringValues) {
		List<APSDayPlanAuditItemBPM> wResult = new ArrayList<>();

		if (StringUtils.isEmpty(wStringValues)) {
			return wResult;
		}

		List<String> wResultList = StringUtils.splitList(wStringValues, "+|;|+");
		for (String wValue : wResultList) {
			if (StringUtils.isEmpty(wValue))
				continue;
			wResult.add(new APSDayPlanAuditItemBPM(wValue));
		}
		return wResult;
	}

	public int getAPSTaskStepID() {
		return APSTaskStepID;
	}

	public void setAPSTaskStepID(int aPSTaskStepID) {
		APSTaskStepID = aPSTaskStepID;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}
}
