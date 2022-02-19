package com.mes.loco.aps.server.service;

import java.util.Calendar;
import java.util.List;

import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonConfig;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanAreaCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanClassCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayPlanPartCashingRate;
import com.mes.loco.aps.server.service.po.andon.AndonDayReport01;
import com.mes.loco.aps.server.service.po.andon.AndonJournal;
import com.mes.loco.aps.server.service.po.andon.AndonLocomotiveProductionStatus;
import com.mes.loco.aps.server.service.po.andon.AndonOrder;
import com.mes.loco.aps.server.service.po.andon.AndonScreen;
import com.mes.loco.aps.server.service.po.andon.AndonTaskStep;
import com.mes.loco.aps.server.service.po.andon.OrderTimeInfo;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2019年12月27日16:16:33
 * @LastEditTime 2019年12月27日20:12:20
 *
 */
public interface AndonService {

	/**
	 * 查询台车订单实时列表
	 * 
	 * @param wLoginUser
	 * @return
	 */
	ServiceResult<List<OrderTimeInfo>> Andon_QueryTrainOrderInfoList(BMSEmployee wLoginUser);

	/**
	 * 查询台车订单实时列表(优化版)
	 * 
	 * @param wLoginUser
	 * @return
	 */
	ServiceResult<List<OrderTimeInfo>> Andon_QueryTrainOrderInfoListNew(BMSEmployee wLoginUser);

	/**
	 * 获取安灯数据
	 */
	ServiceResult<AndonScreen> Andon_QueryAndonScreen(BMSEmployee wLoginUser);

	/**
	 * 获取车辆实时信息
	 */
	ServiceResult<AndonOrder> Andon_QueryPositionInfo(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 创建报表日志
	 */
	ServiceResult<AndonJournal> Andon_CreateJournal(BMSEmployee wLoginUser);

	/**
	 * 提交报表日志
	 */
	ServiceResult<Integer> Andon_SubmitJournal(BMSEmployee wLoginUser, AndonJournal wAndonJournal);

	/**
	 * 查询单条报表日志记录
	 */
	ServiceResult<AndonJournal> Andon_QueryJournalInfo(BMSEmployee wLoginUser, int wID);

	/**
	 * 查询自己提交的报表日志
	 */
	ServiceResult<List<AndonJournal>> Andon_QueryJournalEmployeeAll(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime);

	ServiceResult<Integer> Andon_UpdateConfig(BMSEmployee wLoginUser, AndonConfig wAndonConfig);

	ServiceResult<List<AndonConfig>> Andon_QueryConfigList(BMSEmployee wLoginUser, int wID);

	ServiceResult<AndonConfig> Andon_QueryConfig(BMSEmployee wLoginUser, int wID);

	ServiceResult<AndonLocomotiveProductionStatus> Andon_QueryProductionStatus(BMSEmployee wLoginUser);

	ServiceResult<List<AndonDayPlanPartCashingRate>> Andon_QueryPartDayPlanRate(BMSEmployee wLoginUser, Calendar wSTime,
			Calendar wETime, String wPartIDs, String wOrderIDs);

	ServiceResult<List<AndonDayPlanClassCashingRate>> Andon_QueryClassDayPlanRate(BMSEmployee wLoginUser,
			Calendar wSTime, Calendar wETime, String wClassIDs, String wOrderIDs);

	ServiceResult<List<AndonDayPlanAreaCashingRate>> Andon_QueryAreaDayPlanRate(BMSEmployee wLoginUser, Calendar wSDate,
			Calendar wETime, String wAreaIDs, String wOrderIDs);

	ServiceResult<List<AndonDayPlanAreaCashingRate>> Andon_QueryAreaMonthPlanRate(BMSEmployee wLoginUser,
			Calendar wSDate, Calendar wEDate, String wAreaIDs, String wOrderIDs);

	ServiceResult<List<AndonDayPlanClassCashingRate>> Andon_QueryClassMonthPlanRate(BMSEmployee wLoginUser,
			Calendar wSDate, Calendar wEDate, String wClassIDs, String wOrderIDs);

	ServiceResult<List<AndonDayPlanPartCashingRate>> Andon_QueryPartMonthPlanRate(BMSEmployee wLoginUser,
			Calendar wSDate, Calendar wEDate, String wPartIDs, String wOrderIDs);

	/**
	 * 导出生产看板数据
	 */
	ServiceResult<String> Andon_ExportTrainStatus(BMSEmployee wLoginUser);

	/**
	 * 导出工区兑现率
	 */
	ServiceResult<String> Andon_ExportAreaRate(BMSEmployee wLoginUser, List<AndonDayPlanAreaCashingRate> wList);

	/**
	 * 导出班组兑现率
	 */
	ServiceResult<String> Andon_ExportClassRate(BMSEmployee wLoginUser, List<AndonDayPlanClassCashingRate> wList);

	/**
	 * 导出工位兑现率
	 */
	ServiceResult<String> Andon_ExportPartRate(BMSEmployee wLoginUser, List<AndonDayPlanPartCashingRate> wList);

	/**
	 * 通过工区、班组筛选工位
	 */
	ServiceResult<List<FPCPart>> APS_QueryPartList(BMSEmployee wLoginUser, String wAreaID, String wClassID);

	/**
	 * 生产日报第2页
	 */
	ServiceResult<Integer> Andon_ProductionDaily2(BMSEmployee wLoginUser, int year);

	/**
	 * 生产日报第3页
	 */
	ServiceResult<Integer> Andon_ProductionDaily3(BMSEmployee wLoginUser, int year);

	/**
	 * 生产日报第1页
	 */
	ServiceResult<AndonDayReport01> Andon_ProductionDaily1(BMSEmployee wLoginUser, int wYear, int wMonth);

	/**
	 * 导出生产计划
	 */
	ServiceResult<String> Andon_ExportProductionPlan(BMSEmployee wLoginUser);

	/**
	 * 通过工区查看未完成、已完成的工序任务详情
	 */
	ServiceResult<List<AndonTaskStep>> Andon_QueryAreaStepDetails(BMSEmployee wLoginUser, int wStatus, int wWorkAreaID,
			Calendar wStartTime, Calendar wEndTime, int wOrderID, int wPartID, int wStepID);
}
