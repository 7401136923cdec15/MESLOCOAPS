package com.mes.loco.aps.server.service;

import java.util.List;
import java.util.Map;

import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.FocasLGL;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fmc.FMCLine;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkCharge;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCPartPoint;
import com.mes.loco.aps.server.service.po.fpc.FPCProduct;
import com.mes.loco.aps.server.service.po.wms.WMSLinePartLLs;
import com.mes.loco.aps.server.service.po.wms.WMSReturn;

public interface MyHelperService {

	/**
	 * 获取修程Name映射
	 * 
	 * @param wLoginUser 登陆者
	 * @return Key：修程Name Value：修程
	 */
	public ServiceResult<Map<String, FMCLine>> FMC_QueryLineNameMap(BMSEmployee wLoginUser);

	/**
	 * 获取工位Name映射
	 * 
	 * @param wLoginUser
	 * @return
	 */
	public ServiceResult<Map<String, FPCPart>> FPC_QueryPartNameMap(BMSEmployee wLoginUser);

	/**
	 * 获取工序Name映射
	 * 
	 * @param wLoginUser
	 * @return
	 */
	public ServiceResult<Map<String, FPCPartPoint>> FPC_QueryPartPointNameMap(BMSEmployee wLoginUser);

	/**
	 * 查询工位 班组映射
	 * 
	 * @param wLoginUser
	 * @return
	 */
	public ServiceResult<Map<Integer, FMCWorkCharge>> FMC_QueryWorkChargeMap(BMSEmployee wLoginUser);

	/**
	 * 获取产品型号映射
	 * 
	 * @param wLoginUser 登陆者
	 * @return Key:产品型号名称 Value:产品型号
	 */
	public ServiceResult<Map<String, FPCProduct>> FPC_QueryProductNameMap(BMSEmployee wLoginUser);

	/**
	 * 查询工区检验员映射
	 * 
	 * @param wLoginUser
	 * @return
	 */
	public ServiceResult<Map<Integer, List<Integer>>> LFS_QueryWorkAreaCheckListMap(BMSEmployee wLoginUser);

	/**
	 * 根据订单、工位获取上工位ID集合
	 * 
	 * @param wLoginUser 登录人
	 * @param wOrderID   订单ID
	 * @param wStationID 工位ID
	 * @return
	 */
	public ServiceResult<List<Integer>> FPC_QueryPreStationIDList(BMSEmployee wLoginUser, int wOrderID, int wStationID);

	/**
	 * 判断某个工序任务是否能进行派工
	 * 
	 * @param wLoginUser     登录者
	 * @param wAPSTaskPartID 工序任务ID
	 * @return 判断结果
	 */
	public ServiceResult<Boolean> SFC_JudgeTaskStepIsCanDo(BMSEmployee wLoginUser, APSTaskStep wAPSTaskStep);

	/**
	 * 判断是否发起返修
	 * 
	 * @param wAdminUser
	 * @param taskID
	 * @param iPTItemID
	 * @return
	 */
	public ServiceResult<Boolean> WDW_IsSendRepair(BMSEmployee wAdminUser, int taskID, int iPTItemID);

	public List<FocasLGL> Focas_LGLMES(BMSEmployee wLoginUser, int wYear);

	public WMSReturn WMS_PostLL(WMSLinePartLLs wWMSLinePartLLs);

	void WMS_TestSend();
}
