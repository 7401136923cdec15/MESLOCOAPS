package com.mes.loco.aps.server.service;

import java.util.Calendar;
import java.util.List;

import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.utils.Configuration;

public interface WDWService {
	public static final String ServerUrl = Configuration.readConfigString("wdw.server.url", "config/config");
	public static final String ServerName = Configuration.readConfigString("wdw.server.project.name", "config/config");

	APIResult WDW_SpecialItemAll_Repair(BMSEmployee wAdminUser, int taskID);

	APIResult RRO_QueryItemTaskList(BMSEmployee wLoginUser, int wCarType, int wLineID, int wPartID, int wLimit);

	/**
	 * 将车绑定到厂线上
	 */
	APIResult WDW_BindCarToPlant(BMSEmployee wLoginUser, String wPartNo);

	/**
	 * 根据订单查询未完成的返修项
	 */
	APIResult RRO_QueryNotFinishItemList(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 返修项列表
	 */
	APIResult RRO_QueryItemTaskList(BMSEmployee wLoginUser, List<Integer> wIDList);

	/**
	 * 车节数信息
	 */
	APIResult MTC_QuerySectionInfoList(BMSEmployee wLoginUser, int wProductID);
	
	/**
	 * 查询移车记录
	 */
	APIResult MTC_QueryEmployeeAllWeb(BMSEmployee wLoginUser,Calendar wStartTime,Calendar wEndTime,int wProductID,int wStatus);
}