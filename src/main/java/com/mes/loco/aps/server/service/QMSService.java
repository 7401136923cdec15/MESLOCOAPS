package com.mes.loco.aps.server.service;

import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.ipt.IPTPreCheckProblem;
import com.mes.loco.aps.server.utils.Configuration;
import java.util.List;

public interface QMSService {
	public static final String ServerUrl = Configuration.readConfigString("qms.server.url", "config/config");

	public static final String ServerName = Configuration.readConfigString("qms.server.project.name", "config/config");

	APIResult RSM_QueryTurnOrderTaskList(BMSEmployee wLoginUser, int wOrderID, int wApplyStationID,
			int wTargetStationID, List<Integer> wStateIDList);

	APIResult IPT_QueryPreCheckProblemByID(BMSEmployee wLoginUser, int wProblemID);

	APIResult IPT_UpdatePreCheckProblem(BMSEmployee wLoginUser, IPTPreCheckProblem wIPTPreCheckProblem);

	APIResult IPT_QueryPreCheckReportList(BMSEmployee wLoginUser, int wOrderID);

	APIResult SFC_QueryTaskIPTList(BMSEmployee wLoginUser, int wOrderID);

	APIResult IPT_QueryValueList(BMSEmployee wLoginUser, int wSFCTaskIPTID);

	APIResult IPT_QueryStandardCurrent(BMSEmployee wLoginUser, int wITPMode, int wLineID, int wPartID, int wPartPointID,
			String wProductNo, int wCustomID);

	APIResult IPT_QueryItemList(BMSEmployee wLoginUser, int wStandardID);

	APIResult RSM_QueryBOPDoneList(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 查询转向架互换的订单ID
	 */
	APIResult SFC_QueryChangeOrderID(BMSEmployee wLoginUser, int wOrderID);
}
