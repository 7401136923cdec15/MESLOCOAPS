package com.mes.loco.aps.server.service;

import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.excel.ExcelData;
import com.mes.loco.aps.server.service.po.oms.OMSCheckMsg;
import com.mes.loco.aps.server.service.po.oms.OMSCommand;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.po.oms.OMSPurchaseOrder;
import java.util.Calendar;
import java.util.List;

public interface OMSService {
	ServiceResult<OMSOrder> OMS_QueryOMSOrder(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<OMSOrder>> OMS_QueryOMSOrderList(BMSEmployee wLoginUser, int wID, int wCommandID,
			String wOrderNo, int wLineID, int wProductID, int wBureauSectionID, String wPartNo, String wBOMNo,
			int wActive, List<Integer> wStateIDList);

	ServiceResult<Long> OMS_UpdateOMSOrder(BMSEmployee wLoginUser, OMSOrder wOMSOrder);

	ServiceResult<Integer> OMS_ActiveOMSOrderList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	ServiceResult<OMSPurchaseOrder> OMS_QueryOMSPurchaseOrder(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<OMSPurchaseOrder>> OMS_QueryOMSPurchaseOrderList(BMSEmployee wLoginUser, long wID, int wPlaceID,
			String wPartNo, int wMaterialID);

	ServiceResult<Long> OMS_UpdateOMSPurchaseOrder(BMSEmployee wLoginUser, OMSPurchaseOrder wOMSPurchaseOrder);

	ServiceResult<OMSOutsourceOrder> OMS_QueryOMSOutsourceOrder(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<OMSOutsourceOrder>> OMS_QueryOMSOutsourceOrderList(BMSEmployee wLoginUser, long wID,
			int wStationID, int wOrderID, String wPartNo, int wMaterialID);

	ServiceResult<List<OMSOutsourceOrder>> OMS_QueryOMSOutsourceOrderList(BMSEmployee wLoginUser,
			List<Integer> wOrderIDList);

	ServiceResult<Long> OMS_UpdateOMSOutsourceOrder(BMSEmployee wLoginUser, OMSOutsourceOrder wOMSOutsourceOrder);

	ServiceResult<OMSCommand> OMS_QueryCommand(BMSEmployee wLoginUser, int wID);

	ServiceResult<List<OMSCommand>> OMS_QueryCommandList(BMSEmployee wLoginUser, int wID, int wActive, int wFactoryID,
			int wBusinessUnitID);

	ServiceResult<Long> OMS_UpdateCommand(BMSEmployee wLoginUser, OMSCommand wOMSCommand);

	ServiceResult<Integer> OMS_ActiveCommandList(BMSEmployee wLoginUser, List<Integer> wIDList, int wActive);

	ServiceResult<List<OMSOrder>> OMS_QueryHistoryOrderList(BMSEmployee wLoginUser, Calendar wStartTime,
			Calendar wEndTime, int wAPSShiftPeriod);

	ServiceResult<List<OMSOrder>> OMS_QueryRFOrderList(BMSEmployee wLoginUser, int wCustomerID, int wLineID,
			int wProductID, String wPartNo, Calendar wStartTime, Calendar wEndTime, int wActive);

	ServiceResult<Integer> OMS_ChangeRoute(BMSEmployee wLoginUser, OMSOrder wOrder, Integer wRouteID);

	ServiceResult<List<OMSOrder>> OMS_QueryConditionAll(BMSEmployee wLoginUser, int wProductID, int wLine,
			int wCustomerID, String wWBSNo, Calendar wStartTime, Calendar wEndTime, int wStatus);

	ServiceResult<Integer> OMS_SaveList(BMSEmployee wLoginUser, List<OMSOrder> wOrderList);

	ServiceResult<List<OMSOrder>> OMS_QueryOrderListByIDList(BMSEmployee wLoginUser, List<Integer> wOrderIDList);

	/**
	 * 已收电报
	 * 
	 * @param wLoginUser
	 * @param wOrder
	 * @return
	 */
	ServiceResult<Integer> OMS_ReceivedOrder(BMSEmployee wLoginUser, List<OMSOrder> wOrderList);

	/**
	 * 订单基础数据检查
	 * 
	 * @param wLoginUser
	 * @param wOrderID
	 * @return
	 */
	ServiceResult<List<OMSCheckMsg>> OMS_Check(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 导出检查报告
	 * 
	 * @param wLoginUser
	 * @param wOrderID
	 * @return
	 */
	ServiceResult<String> ExportCheck(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 检查订单工艺路径Route的BOM
	 * 
	 * @param wLoginUser
	 * @param wOrderList
	 * @return
	 */
	ServiceResult<Integer> OMS_CheckRouteBOM(BMSEmployee wLoginUser, List<OMSOrder> wOrderList);

	/**
	 * 清空车辆
	 */
	ServiceResult<Integer> OMS_ClearCar(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 条件查询所有订单列表
	 */
	ServiceResult<List<OMSOrder>> OMS_QueryAll(BMSEmployee wLoginUser, List<Integer> wCustomerList,
			List<Integer> wLineList, Calendar wStartTime, Calendar wEndTime, List<Integer> wStatusList,
			List<Integer> wProductList, String wPartNo, List<Integer> wActiveList);

	/**
	 * 获取厂线上的所有状态为“已进厂”的车辆列表
	 */
	ServiceResult<List<OMSOrder>> OMS_QueryInPlantCarList(BMSEmployee wLoginUser);

	/**
	 * 剔除厂线上的车辆
	 */
	ServiceResult<Integer> OMS_ClearCarPro(BMSEmployee wLoginUser, List<Integer> wOrderIDList);

	/**
	 * 处理重复的生产命令票
	 */
	ServiceResult<Integer> OMS_HandleRepeatCommand(BMSEmployee wLoginUser);

	/**
	 * 手动推送订单给SAP
	 */
	ServiceResult<String> OMS_SendToSAP(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 根据Excel删除订单
	 */
	ServiceResult<Integer> OMS_DeleteByExcel(BMSEmployee wLoginUser, ExcelData result);

	/**
	 * 获取订单数据检查优化版
	 */
	ServiceResult<List<OMSCheckMsg>> OMS_Check_V2(BMSEmployee wLoginUser, int wOrderID);

	/**
	 * 禁用相关计划
	 */
	ServiceResult<Integer> OMS_DisablePlan(BMSEmployee wLoginUser, String wOrders);

	ServiceResult<List<OMSOrder>> OMS_QuerySelfOrderList(BMSEmployee wLoginUser, int wOrderType);

	ServiceResult<Integer> OMS_ReparingOrders(BMSEmployee wLoginUser, List<Integer> wOrderIDList);

	/**
	 * 保存顺序
	 */
	ServiceResult<Integer> OMS_SaveOrder(BMSEmployee wLoginUser, List<OMSOrder> wList);
}

/*
 * Location: C:\Users\Shris\Desktop\新建文件夹
 * (5)\MESLOCOAPS.zip!\WEB-INF\classes\com\mes\loco\aps\server\service\
 * OMSService.class Java compiler version: 8 (52.0) JD-Core Version: 1.1.2
 */