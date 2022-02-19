package com.mes.loco.aps.server.service;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.rpt.RPTCustomerShift;
import com.mes.loco.aps.server.service.po.rpt.RPTDataDetails;
import com.mes.loco.aps.server.service.po.rpt.RPTMonthData;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPart;
import com.mes.loco.aps.server.service.po.rpt.RPTOrderPartTJ;
import com.mes.loco.aps.server.service.po.rpt.RPTProductShift;
import com.mes.loco.aps.server.service.po.rpt.RPTQuarterData;
import com.mes.loco.aps.server.service.po.rpt.RPTYearNumberTrend;
import com.mes.loco.aps.server.service.po.rpt.RPTYearProductShift;
import com.mes.loco.aps.server.service.po.rpt.RPTYearStopTrend;

public interface RPTService {
	ServiceResult<Integer> RPT_SetProductShifts(BMSEmployee wLoginUser);

	ServiceResult<Integer> RPT_SetCustomerShifts(BMSEmployee wLoginUser);

	ServiceResult<List<RPTProductShift>> RPT_QueryProductShiftList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	ServiceResult<List<RPTProductShift>> RPT_QueryProductShiftInfo(BMSEmployee wLoginUser, int wLineID,
			Calendar wShiftDate, APSShiftPeriod wAPSShiftPeriod);

	ServiceResult<List<RPTOrderPart>> RPT_QueryRPTOrderPartList(BMSEmployee wLoginUser, int wLineID,
			Calendar wStartTime, Calendar wEndTime);

	ServiceResult<List<RPTCustomerShift>> RPT_QueryCustomerShiftList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	ServiceResult<RPTCustomerShift> RPT_QueryCustomerShiftInfo(BMSEmployee wLoginUser, int wCustomerID,
			Calendar wShiftDate, APSShiftPeriod wAPSShiftPeriod);

	ServiceResult<Map<Integer, List<RPTOrderPartTJ>>> RPT_QueryRPTOrderPartTJList(BMSEmployee wLoginUser, int wYear);

	/**
	 * 获取年统计数据
	 * 
	 * @param wLoginUser
	 * @param wStartTime
	 * @param wEndTime
	 * @return
	 */
	ServiceResult<List<RPTYearProductShift>> RPT_QueryYearProductShiftList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	/**
	 * 获取季度完成数
	 * 
	 * @param wLoginUser 登录信息
	 * @param wYear      年份
	 * @return 季度统计数据
	 */
	ServiceResult<List<RPTQuarterData>> RPT_QueryQuarterData(BMSEmployee wLoginUser, int wYear);

	/**
	 * 获取月份完成数
	 * 
	 * @param wLoginUser
	 * @param wYear
	 * @return
	 */
	ServiceResult<List<RPTMonthData>> RPT_QueryMonthData(BMSEmployee wLoginUser, int wYear);

	/**
	 * 显示统计详情
	 * 
	 * @param wLoginUser     登录信息
	 * @param wYear          年份
	 * @param wQID           季度或月份
	 * @param wQType         季度或月份类型
	 * @param wShowZero是否统计0
	 * @return 统计数据详情
	 */
	ServiceResult<List<RPTDataDetails>> RPT_QueryDataDetails(BMSEmployee wLoginUser, int wYear, int wQID, int wQType,
			boolean wShowZero);

	/**
	 * 年度机型统计数据
	 * 
	 * @param wLoginUser
	 * @param wYear
	 * @return
	 */
	ServiceResult<List<RPTDataDetails>> RPT_QueryYearProduct(BMSEmployee wLoginUser, int wYear);

	/**
	 * 机车进出厂数量趋势图
	 */
	ServiceResult<RPTYearNumberTrend> RPT_QueryYearNumberTrend(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	/**
	 * 机车平均停时趋势图
	 */
	ServiceResult<RPTYearStopTrend> RPT_QueryYearStopTrend(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);
}
