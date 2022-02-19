package com.mes.loco.aps.server.controller.aps;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.CoreService;
import com.mes.loco.aps.server.service.FMCService;
import com.mes.loco.aps.server.service.LFSService;
import com.mes.loco.aps.server.service.OMSService;
import com.mes.loco.aps.server.service.mesenum.APSShiftPeriod;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.MBSRoleTree;
import com.mes.loco.aps.server.service.mesenum.OMSOrderStatus;
import com.mes.loco.aps.server.service.po.APIResult;
import com.mes.loco.aps.server.service.po.OutResult;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.andon.AndonTime;
import com.mes.loco.aps.server.service.po.aps.APSDismantling;
import com.mes.loco.aps.server.service.po.aps.APSInstallation;
import com.mes.loco.aps.server.service.po.aps.APSManuCapacity;
import com.mes.loco.aps.server.service.po.aps.APSMessage;
import com.mes.loco.aps.server.service.po.aps.APSSchedulingVersion;
import com.mes.loco.aps.server.service.po.aps.APSStepPlanParam;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSWorkHour;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fmc.FMCLine;
import com.mes.loco.aps.server.service.po.fmc.FMCWorkspace;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCPartPoint;
import com.mes.loco.aps.server.service.po.fpc.FPCProduct;
import com.mes.loco.aps.server.service.po.fpc.FPCRoute;
import com.mes.loco.aps.server.service.po.fpc.FPCRoutePart;
import com.mes.loco.aps.server.service.po.lfs.LFSWorkAreaStation;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.oms.OMSOutsourceOrder;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.serviceimpl.dao.fpc.FPCRouteDAO;
import com.mes.loco.aps.server.serviceimpl.dao.oms.OMSOrderDAO;
import com.mes.loco.aps.server.serviceimpl.utils.aps.APSConstans;
import com.mes.loco.aps.server.shristool.LoggerTool;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2019年12月26日22:46:00
 * @LastEditTime 2021-8-16 10:52:40
 */
@RestController
@RequestMapping("/api/APSTaskPart")
public class APSTaskPartController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(APSTaskPartController.class);

	@Autowired
	APSService wAPSService;

	@Autowired
	LFSService wLFSService;

	@Autowired
	OMSService wOMSService;

	@Autowired
	CoreService wCoreService;

	@Autowired
	FMCService wFMCService;

	/**
	 * 查单条
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/Info")
	public Object Info(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wID = StringUtils.parseInt(request.getParameter("ID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<APSTaskPart> wServiceResult = wAPSService.APS_QueryTaskPart(wLoginUser, wID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/All")
	public Object All(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wShiftPeriod = StringUtils.parseInt(request.getParameter("ShiftPeriod"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryTaskPartList(wLoginUser, wID,
					wOrderID, -1, -1, -1, 1);

			if (wServiceResult.Result.size() > 0) {
				wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.ShiftPeriod == wShiftPeriod)
						.collect(Collectors.toList());
			}

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 工时调整工位任务数据源
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/WorkHourList")
	public Object WorkHourList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			Integer wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryWorkHourList(wLoginUser, wStartTime,
					wEndTime, wOrderID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 条件查询
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/List")
	public Object List(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wShiftID = StringUtils.parseInt(request.getParameter("ShiftID"));
			int wShiftPeriod = StringUtils.parseInt(request.getParameter("ShiftPeriod"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryTaskPartList(wLoginUser, wShiftID,
					wShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 状态查询工位任务
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/StatusList")
	public Object StatusList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wShiftPeriod = StringUtils.parseInt(wParam.get("ShiftPeriod"));
			List<Integer> wStateIDList = CloneTool.CloneArray(wParam.get("data"), Integer.class);

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryTaskPartList(wLoginUser,
					wShiftPeriod, wStateIDList, null);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 通过订单ID查询工位任务列表
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/RFList")
	public Object RFList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryRFTaskPartList(wLoginUser, wOrderID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 查询历史
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/History")
	public Object History(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wAPSShiftPeriod = StringUtils.parseInt(request.getParameter("APSShiftPeriod"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<APSSchedulingVersion>> wServiceResult = wAPSService
					.APS_QueryHistoryVersionList(wLoginUser, wAPSShiftPeriod, wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}

			if (wServiceResult.Result.size() <= 0)
				return wResult;

//			// 获取树形的数据
//			List<APSMessage> wMessageList = new ArrayList<APSMessage>();
//			wMessageList = wAPSService.APS_QueryMessageListBySubmitTime(wLoginUser,
//					wServiceResult.Result.get(0).SubmitTime).Result;
//			// 产线列表
//			List<FMCLine> wLineList = wCoreService.FMC_QueryLineList(wLoginUser).List(FMCLine.class);
//			// 工位列表
//			List<FPCPart> wPartList = wCoreService.FPC_QueryPartList(wLoginUser).List(FPCPart.class);
//
//			int wFlag = 1;
//			int wOrderFlag = 1;
//			List<APSTaskPart> wTreeList = new ArrayList<APSTaskPart>();
//			// 工区工位列表
//			APIResult wApiResult = wLFSService.LFS_QueryWorkAreaStationList(wLoginUser);
//			List<LFSWorkAreaStation> wWorkAreaStationList = wApiResult.List(LFSWorkAreaStation.class);
//			// 排序
//			wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
//			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
//				// 工位
//				APSTaskPart wAPSTaskPart = new APSTaskPart();
//				wAPSTaskPart.PartID = wItem.StationID;
//				wAPSTaskPart.PartOrder = wOrderFlag++;
//				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
//				if (wOption.isPresent()) {
//					wAPSTaskPart.PartName = wOption.get().Name;
//				}
//				wAPSTaskPart.UniqueID = wFlag++;
//				wTreeList.add(wAPSTaskPart);
//
//				for (FMCLine wLine : wLineList) {
//					// 产线
//					APSTaskPart wLinePart = new APSTaskPart();
//					wLinePart.UniqueID = wFlag++;
//					wLinePart.LineID = wLine.ID;
//					wLinePart.LineName = wLine.Name;
//					wAPSTaskPart.TaskPartList.add(wLinePart);
//
//					// 工位任务
//					wLinePart.TaskPartList = wServiceResult.Result.stream()
//							.filter(p -> p.PartID == wItem.StationID && p.LineID == wLine.ID)
//							.collect(Collectors.toList());
//					if (wLinePart.TaskPartList != null && wLinePart.TaskPartList.size() > 0) {
//						for (APSTaskPart wItemPart : wLinePart.TaskPartList) {
//							wItemPart.UniqueID = wFlag++;
//							wItemPart.PartOrder = wAPSTaskPart.PartOrder;
//
//							// 为冲突消息设置唯一标识
//							if (wMessageList == null || wMessageList.size() <= 0)
//								continue;
//							Optional<APSMessage> wMessageOption = wMessageList.stream()
//									.filter(p -> p.LineID == wItemPart.LineID && p.PartID == wItemPart.PartID
//											&& p.OrderID == wItemPart.OrderID)
//									.findFirst();
//							if (wMessageOption.isPresent()) {
//								APSMessage wMessage = wMessageOption.get();
//								wMessage.UniqueID = wItemPart.UniqueID;
//								wItemPart.APSMessage = wMessage.MessageText;
//							}
//						}
//					} else {
//						wLinePart.TaskPartList = new ArrayList<APSTaskPart>();
//						wLinePart.TaskPartList.add(new APSTaskPart());
//					}
//				}
//			}
//
//			SetResult(wResult, "Msg", wMessageList);
//			SetResult(wResult, "TreeList", wTreeList);
//
//			// 表格数据
//			List<OMSOrder> wOrderList = new ArrayList<OMSOrder>();
//			List<Integer> wIDList = wServiceResult.Result.stream().map(p -> p.OrderID).collect(Collectors.toList());
//			for (Integer wItem : wIDList) {
//				ServiceResult<OMSOrder> wTempOrder = wOMSService.OMS_QueryOMSOrder(wLoginUser, wItem);
//				if (wTempOrder != null && wTempOrder.Result != null && wTempOrder.Result.ID > 0) {
//					wOrderList.add(wTempOrder.Result);
//				}
//			}
//
//			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
//			Map<String, Object> wHeadMap = new HashMap<String, Object>();
//			List<FPCPart> wStationList = new ArrayList<FPCPart>();
//			// 排序
//			if (wWorkAreaStationList != null && wWorkAreaStationList.size() > 0) {
//				wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
//			}
//
//			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
//				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
//				if (wOption.isPresent()) {
//					wHeadMap.put("Station_" + String.valueOf(wItem.StationID), wOption.get().Name);
//					wStationList.add(wOption.get());
//				}
//			}
//			wTableMap.add(wHeadMap);
//			wTableMap.addAll(wAPSService.ChangeToTable(wOrderList, wServiceResult.Result, wWorkAreaStationList).Result);
//			SetResult(wResult, "TableInfoList", wTableMap);
//			SetResult(wResult, "OrderColumn", wStationList);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 查询表格数据
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/TableInfoList")
	public Object TableInfoList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wAPSShiftPeriod = StringUtils.parseInt(request.getParameter("APSShiftPeriod"));

			ServiceResult<List<Map<String, Object>>> wServiceResult = wAPSService.APS_QueryTableInfoList(wLoginUser,
					wStartTime, wEndTime, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 新增或更新
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Update")
	public Object Update(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			APSTaskPart wAPSTaskPart = CloneTool.Clone(wParam.get("data"), APSTaskPart.class);
			if (wAPSTaskPart == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<Long> wServiceResult = wAPSService.APS_UpdateTaskPart(wLoginUser, wAPSTaskPart);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 批量激活或禁用
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Active")
	public Object Active(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("Active"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			int wActive = StringUtils.parseInt(wParam.get("Active"));

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			List<APSTaskPart> wList = CloneTool.CloneArray(wParam.get("data"), APSTaskPart.class);
			if (wList == null || wList.size() <= 0)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			List<Integer> wIDList = wList.stream().map(p -> p.getID()).collect(Collectors.toList());

			wAPSService.APS_ActiveTaskPartList(wLoginUser, wIDList, wActive);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 自动排程(工位)
	 */
	@PostMapping("/AutoScheduling")
	public Object AutoScheduling(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			long wStartMillis = System.currentTimeMillis();
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			// 【排程】权限控制
			if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 500902, 0, 0)
					.Info(Boolean.class)) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
			}

			if (!wParam.containsKey("data"))
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			@SuppressWarnings("unchecked")
			Map<String, Object> wDataParam = CloneTool.Clone(wParam.get("data"), Map.class);
			// 排程订单
			List<OMSOrder> wOrderList = CloneTool.CloneArray(wDataParam.get("OrderList"), OMSOrder.class);
			// 排程性质
			APSShiftPeriod wShiftPeriod = APSShiftPeriod
					.getEnumType(StringUtils.parseInt(wDataParam.get("APSShiftPeriod")));
			Calendar wStartTime = StringUtils.parseCalendar(wDataParam.get("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(wDataParam.get("EndTime"));

			// 排程如果订单RouteID<=0,不让排程
			if (wOrderList != null && wOrderList.size() > 0 && wOrderList.stream().anyMatch(p -> p.RouteID <= 0)) {
				String wMsg = StringUtils.Format("提示：【{0}】该订单未设置工艺路线，无法排程!",
						wOrderList.stream().filter(p -> p.RouteID <= 0).findFirst().get().OrderNo);
				return GetResult(RetCode.SERVER_CODE_ERR, wMsg);
			}

			// 检查订单的route有无bom
//			ServiceResult<Integer> wCRBResult = wOMSService.OMS_CheckRouteBOM(wLoginUser, wOrderList);
//			if (StringUtils.isNotEmpty(wCRBResult.FaultCode)) {
//				return GetResult(RetCode.SERVER_CODE_ERR, wCRBResult.FaultCode);
//			}

			// 检查计划是否已排
			ServiceResult<Boolean> wCheckResult = wAPSService.APS_CheckIsExist(wLoginUser, wStartTime, wShiftPeriod);
			if (wCheckResult.Result) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：" + wShiftPeriod.getLable() + "计划已排，请审批下达后再试!");
			}

			// 月计划未排，无法排周计划
			if (wShiftPeriod == APSShiftPeriod.Week) {
				// 提示车辆未进厂，无法排周计划。
				if (wOrderList.stream().anyMatch(p -> p.Status < OMSOrderStatus.ReceivedTelegraph.getValue())) {
					String wTip = StringUtils.Format("提示：【{0}】该订单的车辆未收电报，无法排程周计划!",
							wOrderList.stream().filter(p -> p.Status < OMSOrderStatus.ReceivedTelegraph.getValue())
									.findFirst().get().OrderNo);
					return GetResult(RetCode.SERVER_CODE_ERR, wTip);
				}
			}

			// 作息ID
			int wWorkDay = StringUtils.parseInt(wDataParam.get("WorkDay"));
			// 跨天限制时长(分钟)
			int wLimitMinutes = StringUtils.parseInt(wDataParam.get("LimitMinutes"));
			// 固定工位计划
			List<APSTaskPart> wMutualTaskList = CloneTool.CloneArray(wDataParam.get("MutualTaskList"),
					APSTaskPart.class);

			// 工位任务列表
			List<APSTaskPart> wOrderPartIssuedList = new ArrayList<APSTaskPart>();
			List<Integer> wStatusList = new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Issued.getValue(),
					APSTaskStatus.Confirm.getValue(), APSTaskStatus.Started.getValue(),
					APSTaskStatus.Suspend.getValue(), APSTaskStatus.Done.getValue()));
			List<Integer> wOrderIDList = wOrderList.stream().map(p -> p.ID).collect(Collectors.toList());
			wOrderPartIssuedList = wAPSService.APS_QueryTaskPartList(wLoginUser, wShiftPeriod.getValue(), wStatusList,
					wOrderIDList).Result;
			if (wOrderPartIssuedList != null && wOrderPartIssuedList.size() > 0)
				wOrderPartIssuedList = wOrderPartIssuedList.stream()
						.filter(p -> p.ShiftPeriod == wShiftPeriod.getValue() && p.Active == 1)
						.collect(Collectors.toList());

			// 开始时间计算
			Calendar wMinStartTime = Calendar.getInstance();
			List<Calendar> wTimeList = new ArrayList<Calendar>();
			for (OMSOrder wOMSOrder : wOrderList) {
				if (wOrderPartIssuedList != null && wOrderPartIssuedList.size() > 0
						&& wOrderPartIssuedList.stream().anyMatch(p -> p.OrderID == wOMSOrder.ID)) {
					wTimeList.add(wMinStartTime);
				} else if (wOMSOrder.PlanReceiveDate.compareTo(wMinStartTime) > 0) {
					wTimeList.add(wOMSOrder.PlanReceiveDate);
				}
			}
			if (wTimeList.size() <= 0) {
				wTimeList.add(wMinStartTime);
			}
			wMinStartTime = Collections.min(wTimeList);

			// 委外订单列表
			List<OMSOutsourceOrder> wOutsourceOrderList = wOMSService.OMS_QueryOMSOutsourceOrderList(wLoginUser,
					wOrderList.stream().map(p -> p.ID).collect(Collectors.toList())).Result;

			// 工位工时列表
			List<APSManuCapacity> wManuCapacityList = wAPSService.APS_QueryManuCapacityList(wLoginUser, -1, -1, -1,
					1).Result;

			// 获取工艺路径
			Map<Integer, List<FPCRoutePart>> wRoutePartListMap = new HashMap<Integer, List<FPCRoutePart>>();

			// 产线列表
			List<FMCLine> wLineList = wCoreService.FMC_QueryLineList(wLoginUser).List(FMCLine.class);
			wLineList.removeIf(p -> p.Active == 2);
			wOrderList
					.forEach(p -> p.LineName = wLineList.stream().filter(q -> q.ID == p.LineID).findFirst().get().Name);

			Map<Integer, List<FPCRoutePart>> wRouteMap = new HashMap<Integer, List<FPCRoutePart>>();

			for (OMSOrder wOMSOrder : wOrderList) {
				List<FPCRoutePart> wPartList = null;
				if (wRouteMap.containsKey(wOMSOrder.RouteID)) {
					wPartList = wRouteMap.get(wOMSOrder.RouteID);
				} else {
					wPartList = wCoreService.FPC_QueryRoutePartList(wLoginUser, wOMSOrder.RouteID)
							.List(FPCRoutePart.class);
					wRouteMap.put(wOMSOrder.RouteID, wPartList);
				}

				if (wPartList != null && wPartList.size() > 0) {
					wRoutePartListMap.put(wOMSOrder.ID, wPartList);
				}
			}

			// 获取安装明细列表
			List<APSInstallation> wAPSInstallationList = wAPSService.APS_QueryInstallationList(wLoginUser, -1, -1, -1,
					-1, -1).Result;

			// 获取拆解明细列表
			List<APSDismantling> wAPSDismantlingList = wAPSService.APS_QueryDismantlingList(wLoginUser, -1, -1, -1, -1,
					-1).Result;

			// 冲突消息
			List<APSMessage> wMessageList = new ArrayList<APSMessage>();

			long wEndMillis = System.currentTimeMillis();
			int wCallMS = (int) (wEndMillis - wStartMillis);
			LoggerTool.MonitorFunction("排程", "Condition", wCallMS);

			// 自动排程
			ServiceResult<List<APSTaskPart>> wServerRst = wAPSService.APS_AutoTaskPart(wLoginUser, wOrderList,
					wShiftPeriod, wOrderPartIssuedList, wOutsourceOrderList, wStartTime, wEndTime, wRoutePartListMap,
					wManuCapacityList, wAPSInstallationList, wAPSDismantlingList, wMessageList, wWorkDay, wLimitMinutes,
					wMutualTaskList);

			if (!StringUtils.isEmpty(wServerRst.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_FAILED);
				return wResult;
			}

			// 赋值WBS号
			if (wServerRst.Result.size() > 0) {
				wServerRst.Result.forEach(p -> {
					if (StringUtils.isEmpty(p.PartNo)) {
						p.PartNo = wOrderList.stream().filter(q -> q.ID == p.OrderID).findFirst().get().OrderNo;
					}
				});
			}

			// 赋值工艺路线
			if (wServerRst.Result != null && wServerRst.Result.size() > 0) {
				wServerRst.Result.forEach(
						p -> p.RouteID = wOrderList.stream().filter(q -> q.ID == p.OrderID).findFirst().get().RouteID);
			}

			// 甘特图Tip生成
			for (APSTaskPart wItem : wServerRst.Result) {
				Optional<APSManuCapacity> wOption = wManuCapacityList.stream()
						.filter(p -> p.LineID == wItem.LineID && p.PartID == wItem.PartID).findFirst();
				if (wOption.isPresent()) {
					APSManuCapacity wCapacity = wOption.get();
					String wTaskText = StringUtils.Format("工位容量：{0}\n排程周期(天)：{1}", wCapacity.FQTY, wCapacity.WorkHour);
					wItem.TaskText = wTaskText;
				}
			}

			// 处理月排程结尾堆积问题
			Calendar wMiddleTime = wEndTime;

			Calendar wLastTime = (Calendar) wEndTime.clone();
			wLastTime.add(Calendar.DATE, 1);

			if (wShiftPeriod == APSShiftPeriod.Month && wServerRst.Result != null && wServerRst.Result.size() > 0) {
				List<APSTaskPart> wTempList = wServerRst.Result.stream()
						.filter(p -> p.StartTime.compareTo(wEndTime) > 0).collect(Collectors.toList());
				if (wTempList != null && wTempList.size() > 0) {
					APSTaskPart wMinItem = wTempList.stream().min(Comparator.comparing(APSTaskPart::getEndTime)).get();
					wMiddleTime = wMinItem.EndTime;

					wServerRst.Result.forEach(p -> {
						if (p.StartTime.compareTo(wEndTime) > 0) {
							double wHour = CalIntervalHour(wEndTime, p.StartTime);
							p.StartTime = wEndTime;
							p.EndTime.add(Calendar.HOUR_OF_DAY, (int) wHour * -1);
						}
					});
				}
			}

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServerRst.Result, null);

			// 获取去重后的订单工艺路线

			List<Integer> wRouteIDList = wOrderList.stream().map(p -> p.RouteID).distinct()
					.collect(Collectors.toList());
			List<FPCRoute> wRouteList = wAPSService.ASP_QueryRouteList(wLoginUser, wRouteIDList, wRouteMap).Result;

			this.SetResult(wResult, "StartTime", wMinStartTime);
			this.SetResult(wResult, "MiddleTime", wMiddleTime);
			this.SetResult(wResult, "RouteList", wRouteList);
			// 获取树形的数据
			// 产线列表
			if (wLineList == null || wLineList.size() <= 0)
				return wResult;
			// 工位列表

			List<FPCPart> wPartList = wCoreService.FPC_QueryPartList(wLoginUser).List(FPCPart.class);
			if (wPartList == null || wPartList.size() <= 0)
				return wResult;

			int wFlag = 1;
			int wOrderFlag = 1;
			List<APSTaskPart> wTreeList = new ArrayList<APSTaskPart>();
			// 工区工位列表

//			APIResult wApiResult = wLFSService.LFS_QueryWorkAreaStationList(wLoginUser);
//			List<LFSWorkAreaStation> wWorkAreaStationList = wApiResult.List(LFSWorkAreaStation.class);

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			List<LFSWorkAreaStation> wWorkAreaStationList = FPCRouteDAO.getInstance()
					.SelectLFSWorkAreaStationList(wLoginUser, wErrorCode);

			if (wWorkAreaStationList == null || wWorkAreaStationList.size() <= 0) {
				return wResult;
			}

			// 去除订单以外的工位数据
			List<FPCRoutePart> wRoutePartList = new ArrayList<FPCRoutePart>();
			for (int wOrderID : wRoutePartListMap.keySet()) {
				wRoutePartList.addAll(wRoutePartListMap.get(wOrderID));
			}
			wWorkAreaStationList.removeIf(p -> !wRoutePartList.stream().anyMatch(q -> q.PartID == p.StationID));

			// 排序
			wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				// 工位
				APSTaskPart wAPSTaskPart = new APSTaskPart();
				wAPSTaskPart.PartID = wItem.StationID;
				wAPSTaskPart.PartOrder = wOrderFlag++;
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wAPSTaskPart.PartName = wOption.get().Name;
				}
				wAPSTaskPart.UniqueID = wFlag++;
				wTreeList.add(wAPSTaskPart);

				for (FMCLine wLine : wLineList) {
					// 产线
					APSTaskPart wLinePart = new APSTaskPart();
					wLinePart.UniqueID = wFlag++;
					wLinePart.LineID = wLine.ID;
					wLinePart.LineName = wLine.Name;
					wAPSTaskPart.TaskPartList.add(wLinePart);

					// 工位任务
					wLinePart.TaskPartList = wServerRst.Result.stream()
							.filter(p -> p.PartID == wItem.StationID && p.LineID == wLine.ID)
							.collect(Collectors.toList());
					if (wLinePart.TaskPartList != null && wLinePart.TaskPartList.size() > 0) {
						for (APSTaskPart wItemPart : wLinePart.TaskPartList) {
							wItemPart.UniqueID = wFlag++;
							wItemPart.PartOrder = wAPSTaskPart.PartOrder;

							// 为冲突消息设置唯一标识
							if (wMessageList == null || wMessageList.size() <= 0)
								continue;
							Optional<APSMessage> wMessageOption = wMessageList.stream()
									.filter(p -> p.LineID == wItemPart.LineID && p.PartID == wItemPart.PartID
											&& p.OrderID == wItemPart.OrderID)
									.findFirst();
							if (wMessageOption.isPresent()) {
								APSMessage wMessage = wMessageOption.get();
								wMessage.UniqueID = wItemPart.UniqueID;
								wItemPart.APSMessage = wMessage.MessageText;
							}
						}
					} else {
						wLinePart.TaskPartList = new ArrayList<APSTaskPart>();
						wLinePart.TaskPartList.add(new APSTaskPart());
					}
				}
			}

			// 断点打在这里直接执行到这来
			SetResult(wResult, "Msg", wMessageList);
			SetResult(wResult, "TreeList", wTreeList);

			// 表格数据
			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			List<FPCPart> wStationList = new ArrayList<FPCPart>();
			// 排序
			if (wWorkAreaStationList != null && wWorkAreaStationList.size() > 0) {
				wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			}
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wHeadMap.put("Station_" + String.valueOf(wItem.StationID), wOption.get().Name);
					wStationList.add(wOption.get());
				}
			}
			wTableMap.add(wHeadMap);
			wTableMap.addAll(wAPSService.ChangeToTable(wOrderList, wServerRst.Result, wWorkAreaStationList).Result);
			SetResult(wResult, "TableInfoList", wTableMap);
			SetResult(wResult, "OrderColumn", wStationList);

			if (wServerRst.Result != null && wServerRst.Result.size() > 0) {
				// 最大时间
				Calendar wMaxTime = wServerRst.Result.stream().max(Comparator.comparing(APSTaskPart::getEndTime))
						.get().EndTime;
				this.SetResult(wResult, "MaxTime", wMaxTime);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 工序排程-多工位-多订单
	 */
	@PostMapping("/StepPlanMore")
	public Object StepPlanMore(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<APSStepPlanParam> wList = CloneTool.CloneArray(wParam.get("data"), APSStepPlanParam.class);

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_StepPlanMore(wLoginUser, wList);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "TableInfoListMap", wServiceResult.CustomResult.get("TableInfoListMap"));
				this.SetResult(wResult, "OrderColumnMap", wServiceResult.CustomResult.get("OrderColumnMap"));
				this.SetResult(wResult, "RouteList", wServiceResult.CustomResult.get("RouteList"));
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 自动排程(工序)
	 */
	@PostMapping("/AutoSchedulingStep")
	public Object AutoSchedulingStep(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			// 【排程】权限控制
			if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 500902, 0, 0)
					.Info(Boolean.class)) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
			}

			if (!wParam.containsKey("data"))
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			@SuppressWarnings("unchecked")
			Map<String, Object> wDataParam = CloneTool.Clone(wParam.get("data"), Map.class);
			// 排程订单
			int wOrderID = StringUtils.parseInt(wDataParam.get("OrderID"));
			int wPartID = StringUtils.parseInt(wDataParam.get("PartID"));
			// 排程性质
			APSShiftPeriod wShiftPeriod = APSShiftPeriod
					.getEnumType(StringUtils.parseInt(wDataParam.get("APSShiftPeriod")));
			Calendar wStartTime = StringUtils.parseCalendar(wDataParam.get("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(wDataParam.get("EndTime"));
			// 作息ID
			int wWorkDay = StringUtils.parseInt(wDataParam.get("WorkDay"));
			// 跨天限制时长(分钟)
			int wLimitMinutes = StringUtils.parseInt(wDataParam.get("LimitMinutes"));
			// 固定工位计划
			List<APSTaskPart> wMutualTaskList = CloneTool.CloneArray(wDataParam.get("MutualTaskList"),
					APSTaskPart.class);

			// 需要验证是否已派此工位的工序计划

			ServiceResult<List<APSTaskPart>> wServerRst = wAPSService.APS_AutoSchedulingStep(wLoginUser, wOrderID,
					wShiftPeriod, wStartTime, wEndTime, wWorkDay, wLimitMinutes, wMutualTaskList, wPartID);

			if (StringUtils.isEmpty(wServerRst.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServerRst.Result, null);
				this.SetResult(wResult, "StartTime", wServerRst.CustomResult.get("StartTime"));
				this.SetResult(wResult, "MiddleTime", wServerRst.CustomResult.get("MiddleTime"));
				this.SetResult(wResult, "RouteList", wServerRst.CustomResult.get("RouteList"));
				this.SetResult(wResult, "Msg", wServerRst.CustomResult.get("Msg"));
				this.SetResult(wResult, "TreeList", wServerRst.CustomResult.get("TreeList"));
				this.SetResult(wResult, "TableInfoList", wServerRst.CustomResult.get("TableInfoList"));
				this.SetResult(wResult, "OrderColumn", wServerRst.CustomResult.get("OrderColumn"));
				this.SetResult(wResult, "MaxTime", wServerRst.CustomResult.get("MaxTime"));
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServerRst.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 手动排程
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/ManualScheduling")
	public Object ManualScheduling(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			// 【排程】权限控制
			if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 500902, 0, 0)
					.Info(Boolean.class)) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
			}

			if (!wParam.containsKey("data"))
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			@SuppressWarnings("unchecked")
			Map<String, Object> wDataParam = CloneTool.Clone(wParam.get("data"), Map.class);

			List<APSTaskPart> wCheckTaskList = CloneTool.CloneArray(wDataParam.get("CheckTaskList"), APSTaskPart.class);
			List<OMSOrder> wOrderList = CloneTool.CloneArray(wDataParam.get("OrderList"), OMSOrder.class);
			APSShiftPeriod wShiftPeriod = APSShiftPeriod
					.getEnumType(CloneTool.Clone(wDataParam.get("APSShiftPeriod"), Integer.class));
			Calendar wStartTime = CloneTool.Clone(wDataParam.get("StartTime"), Calendar.class);
			Calendar wEndTime = CloneTool.Clone(wDataParam.get("EndTime"), Calendar.class);
			int wWorkDay = CloneTool.Clone(wDataParam.get("WorkDay"), Integer.class);

			// 工位任务列表
			List<APSTaskPart> wOrderPartIssuedList = new ArrayList<APSTaskPart>();
			List<Integer> wStatusList = new ArrayList<Integer>(Arrays.asList(APSTaskStatus.Issued.getValue(),
					APSTaskStatus.Started.getValue(), APSTaskStatus.Suspend.getValue()));
			List<Integer> wOrderIDList = wOrderList.stream().map(p -> p.ID).collect(Collectors.toList());
			wOrderPartIssuedList = wAPSService.APS_QueryTaskPartList(wLoginUser, wShiftPeriod.getValue(), wStatusList,
					wOrderIDList).Result;
			if (wOrderPartIssuedList != null && wOrderPartIssuedList.size() > 0)
				wOrderPartIssuedList = wOrderPartIssuedList.stream()
						.filter(p -> p.ShiftPeriod == wShiftPeriod.getValue()).collect(Collectors.toList());

			// 委外订单列表
			List<OMSOutsourceOrder> wOutsourceOrderList = new ArrayList<OMSOutsourceOrder>();
			wOutsourceOrderList = wOMSService.OMS_QueryOMSOutsourceOrderList(wLoginUser,
					wOrderList.stream().map(p -> p.ID).collect(Collectors.toList())).Result;

			// 工位工时列表
			List<APSManuCapacity> wManuCapacityList = wAPSService.APS_QueryManuCapacityList(wLoginUser, -1, -1, -1,
					1).Result;

			// 获取工艺路径
			Map<Integer, List<FPCRoutePart>> wRoutePartListMap = new HashMap<Integer, List<FPCRoutePart>>();

			for (OMSOrder wOMSOrder : wOrderList) {
				if (wRoutePartListMap.containsKey(wOMSOrder.ID))
					continue;

				if (wOMSOrder.RouteID > 0) {
					List<FPCRoutePart> wPartList = wCoreService.FPC_QueryRoutePartList(wLoginUser, wOMSOrder.RouteID)
							.List(FPCRoutePart.class);
					if (wPartList != null && wPartList.size() > 0) {
						wRoutePartListMap.put(wOMSOrder.ID, wPartList);
					}
				}
			}

			// 获取安装明细列表
			List<APSInstallation> wAPSInstallationList = wAPSService.APS_QueryInstallationList(wLoginUser, -1, -1, -1,
					-1, -1).Result;

			// 获取拆解明细列表
			List<APSDismantling> wAPSDismantlingList = wAPSService.APS_QueryDismantlingList(wLoginUser, -1, -1, -1, -1,
					-1).Result;

			// 工位赋值
			List<FPCPart> wFPCPartList = wCoreService.FPC_QueryPartList(wLoginUser).List(FPCPart.class);
			for (APSTaskPart wItem : wCheckTaskList) {
				Optional<FPCPart> wOption = wFPCPartList.stream().filter(p -> p.ID == wItem.PartID).findFirst();
				if (wOption.isPresent())
					wItem.PartName = wOption.get().Name;
			}

			// 手动排程
			ServiceResult<List<APSMessage>> wServiceResult = wAPSService.APS_CheckTaskPart(wLoginUser, wOrderList,
					wCheckTaskList, wShiftPeriod, wOrderPartIssuedList, wOutsourceOrderList, wStartTime, wEndTime,
					wRoutePartListMap, wManuCapacityList, wAPSInstallationList, wAPSDismantlingList, wWorkDay);

			// 甘特图Tip生成
			for (APSTaskPart wItem : wCheckTaskList) {
				Optional<APSManuCapacity> wOption = wManuCapacityList.stream()
						.filter(p -> p.Active == 1 && p.LineID == wItem.LineID && p.PartID == wItem.PartID).findFirst();
				if (wOption.isPresent()) {
					APSManuCapacity wCapacity = wOption.get();
					String wTaskText = StringUtils.Format("工位容量：{0}\n排程周期(天)：{1}", wCapacity.FQTY, wCapacity.WorkHour);
					wItem.TaskText = wTaskText;
				}
			}

			// 返回Tree型数据
			// 产线列表
			List<FMCLine> wLineList = wCoreService.FMC_QueryLineList(wLoginUser).List(FMCLine.class);
			wLineList.removeIf(p -> p.Active == 2);
			if (wLineList == null || wLineList.size() <= 0)
				return wResult;

			// 工位列表
			List<FPCPart> wPartList = wCoreService.FPC_QueryPartList(wLoginUser).List(FPCPart.class);
			if (wPartList == null || wPartList.size() <= 0)
				return wResult;

			int wFlag = 1;
			int wOrderFlag = 1;
			List<APSTaskPart> wTreeList = new ArrayList<APSTaskPart>();
			// 工区工位列表
			APIResult wApiResult = wLFSService.LFS_QueryWorkAreaStationList(wLoginUser);
			List<LFSWorkAreaStation> wWorkAreaStationList = wApiResult.List(LFSWorkAreaStation.class);
			if (wWorkAreaStationList == null || wWorkAreaStationList.size() <= 0)
				return wResult;

			// 去除订单以外的工位数据
			List<FPCRoutePart> wRoutePartList = new ArrayList<FPCRoutePart>();
			for (int wOrderID : wRoutePartListMap.keySet()) {
				wRoutePartList.addAll(wRoutePartListMap.get(wOrderID));
			}
			wWorkAreaStationList.removeIf(p -> !wRoutePartList.stream().anyMatch(q -> q.PartID == p.StationID));

			// 排序
			wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));

			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				// 工位
				APSTaskPart wAPSTaskPart = new APSTaskPart();
				wAPSTaskPart.PartID = wItem.StationID;
				wAPSTaskPart.PartOrder = wOrderFlag++;
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wAPSTaskPart.PartName = wOption.get().Name;
				}
				wAPSTaskPart.UniqueID = wFlag++;
				wTreeList.add(wAPSTaskPart);

				for (FMCLine wLine : wLineList) {
					// 产线
					APSTaskPart wLinePart = new APSTaskPart();
					wLinePart.UniqueID = wFlag++;
					wLinePart.LineID = wLine.ID;
					wLinePart.LineName = wLine.Name;
					wAPSTaskPart.TaskPartList.add(wLinePart);

					// 工位任务
					wLinePart.TaskPartList = wCheckTaskList.stream()
							.filter(p -> p.PartID == wItem.StationID && p.LineID == wLine.ID)
							.collect(Collectors.toList());
					if (wLinePart.TaskPartList != null && wLinePart.TaskPartList.size() > 0) {
						for (APSTaskPart wItemPart : wLinePart.TaskPartList) {
							wItemPart.UniqueID = wFlag++;
							wItemPart.PartOrder = wAPSTaskPart.PartOrder;

							// 为冲突消息设置唯一标识
							if (wServiceResult.Result == null || wServiceResult.Result.size() <= 0)
								continue;
							Optional<APSMessage> wMessageOption = wServiceResult.Result.stream()
									.filter(p -> p.LineID == wItemPart.LineID && p.PartID == wItemPart.PartID
											&& p.OrderID == wItemPart.OrderID)
									.findFirst();
							if (wMessageOption.isPresent()) {
								APSMessage wMessage = wMessageOption.get();
								wMessage.UniqueID = wItemPart.UniqueID;
								wItemPart.APSMessage = wMessage.MessageText;
							}
						}
					} else {
						wLinePart.TaskPartList = new ArrayList<APSTaskPart>();
						wLinePart.TaskPartList.add(new APSTaskPart());
					}
				}
			}

			// 表格数据
			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			List<FPCPart> wStationList = new ArrayList<FPCPart>();
			// 排序
			if (wWorkAreaStationList != null && wWorkAreaStationList.size() > 0) {
				wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			}
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wHeadMap.put("Station_" + String.valueOf(wItem.StationID), wOption.get().Name);
					wStationList.add(wOption.get());
				}
			}

			wTableMap.add(wHeadMap);
			wTableMap.addAll(wAPSService.ChangeToTable(wOrderList, wCheckTaskList, wWorkAreaStationList).Result);
			SetResult(wResult, "TableInfoList", wTableMap);
			SetResult(wResult, "OrderColumn", wStationList);

			if (!StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_FAILED);
			}

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			SetResult(wResult, "TreeList", wTreeList);
			SetResult(wResult, "TaskList", wCheckTaskList);

			if (wCheckTaskList != null && wCheckTaskList.size() > 0) {
				// 最大时间
				Calendar wMaxTime = wCheckTaskList.stream().max(Comparator.comparing(APSTaskPart::getEndTime))
						.get().EndTime;
				this.SetResult(wResult, "MaxTime", wMaxTime);
			}

		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 保存工位任务
	 */
	@PostMapping("/SaveTaskList")
	public Object SaveTaskList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			// 前端参数(工位任务列表、冲突消息列表)
			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			@SuppressWarnings("unchecked")
			Map<String, Object> wDataParam = CloneTool.Clone(wParam.get("data"), Map.class);
			Calendar wStartTime = CloneTool.Clone(wDataParam.get("StartTime"), Calendar.class);
			Calendar wEndTime = CloneTool.Clone(wDataParam.get("EndTime"), Calendar.class);
			List<APSTaskPart> wTaskPartList = CloneTool.CloneArray(wDataParam.get("TaskPartList"), APSTaskPart.class);
			List<APSMessage> wMessageList = CloneTool.CloneArray(wDataParam.get("MsgList"), APSMessage.class);

			if (wStartTime.compareTo(wEndTime) > 0 || wTaskPartList == null || wTaskPartList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_SaveTaskPartList(wLoginUser, wStartTime, wEndTime,
					wTaskPartList, wMessageList);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wTaskPartList, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 保存工序计划(工序排程)
	 */
	@PostMapping("/SaveStepPlan")
	public Object SaveStepPlan(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<APSTaskPart> wStepPlanList = CloneTool.CloneArray(wParam.get("data"), APSTaskPart.class);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_SaveStepPlan(wLoginUser, wStepPlanList);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取树形数据
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/TreeList")
	public Object TreeList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			List<APSMessage> wMessageList = CloneTool.CloneArray(wParam.get("MessageList"), APSMessage.class);
			List<APSTaskPart> wTaskPartList = CloneTool.CloneArray(wParam.get("TaskPartList"), APSTaskPart.class);

			// 返回Tree型数据
			// 产线列表
			List<FMCLine> wLineList = wCoreService.FMC_QueryLineList(wLoginUser).List(FMCLine.class);
			if (wLineList == null || wLineList.size() <= 0)
				return wResult;

			// 获取工艺路径
			Map<Integer, List<FPCRoutePart>> wRoutePartListMap = new HashMap<Integer, List<FPCRoutePart>>();

			// 产线列表
			wLineList.removeIf(p -> p.Active == 2);

			List<OMSOrder> wOrderList = new ArrayList<OMSOrder>();
			for (APSTaskPart wAPSTaskPart : wTaskPartList) {
				if (wOrderList.stream().anyMatch(p -> p.ID == wAPSTaskPart.OrderID))
					continue;
				ServiceResult<OMSOrder> wRst = wOMSService.OMS_QueryOMSOrder(wLoginUser, wAPSTaskPart.OrderID);
				if (wRst.Result != null && wRst.Result.ID > 0)
					wOrderList.add(wRst.Result);
			}

			wOrderList
					.forEach(p -> p.LineName = wLineList.stream().filter(q -> q.ID == p.LineID).findFirst().get().Name);

			// 赋值RouteID
			wTaskPartList.forEach(
					p -> p.RouteID = wOrderList.stream().filter(q -> q.ID == p.OrderID).findFirst().get().RouteID);

			Map<Integer, List<FPCRoutePart>> wRouteMap = new HashMap<Integer, List<FPCRoutePart>>();
			for (OMSOrder wOMSOrder : wOrderList) {
				if (wRoutePartListMap.containsKey(wOMSOrder.ID))
					continue;

				if (wOMSOrder.RouteID > 0) {
					List<FPCRoutePart> wPartList = null;
					if (wRouteMap.containsKey(wOMSOrder.RouteID)) {
						wPartList = wRouteMap.get(wOMSOrder.RouteID);
					} else {
						wPartList = wCoreService.FPC_QueryRoutePartList(wLoginUser, wOMSOrder.RouteID)
								.List(FPCRoutePart.class);
						wRouteMap.put(wOMSOrder.RouteID, wPartList);
					}

					if (wPartList != null && wPartList.size() > 0) {
						wRoutePartListMap.put(wOMSOrder.ID, wPartList);
					}
				}
			}

			// 工位列表
			List<FPCPart> wPartList = wCoreService.FPC_QueryPartList(wLoginUser).List(FPCPart.class);
			if (wPartList == null || wPartList.size() <= 0)
				return wResult;

			int wFlag = 1;
			int wOrderFlag = 1;
			List<APSTaskPart> wTreeList = new ArrayList<APSTaskPart>();
			// 工区工位列表
			APIResult wApiResult = wLFSService.LFS_QueryWorkAreaStationList(wLoginUser);
			List<LFSWorkAreaStation> wWorkAreaStationList = wApiResult.List(LFSWorkAreaStation.class);
			if (wWorkAreaStationList == null || wWorkAreaStationList.size() <= 0)
				return wResult;

			// 去除订单以外的工位数据
			List<FPCRoutePart> wRoutePartList = new ArrayList<FPCRoutePart>();
			for (int wOrderID : wRoutePartListMap.keySet()) {
				wRoutePartList.addAll(wRoutePartListMap.get(wOrderID));
			}
			wWorkAreaStationList.removeIf(p -> !wRoutePartList.stream().anyMatch(q -> q.PartID == p.StationID));

			// 排序
			wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				// 工位
				APSTaskPart wAPSTaskPart = new APSTaskPart();
				wAPSTaskPart.PartID = wItem.StationID;
				wAPSTaskPart.PartOrder = wOrderFlag++;
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wAPSTaskPart.PartName = wOption.get().Name;
				}
				wAPSTaskPart.UniqueID = wFlag++;
				wTreeList.add(wAPSTaskPart);

				for (FMCLine wLine : wLineList) {
					// 产线
					APSTaskPart wLinePart = new APSTaskPart();
					wLinePart.UniqueID = wFlag++;
					wLinePart.LineID = wLine.ID;
					wLinePart.LineName = wLine.Name;
					wAPSTaskPart.TaskPartList.add(wLinePart);

					// 工位任务
					wLinePart.TaskPartList = wTaskPartList.stream()
							.filter(p -> p.PartID == wItem.StationID && p.LineID == wLine.ID)
							.collect(Collectors.toList());
					if (wLinePart.TaskPartList != null && wLinePart.TaskPartList.size() > 0) {
						for (APSTaskPart wItemPart : wLinePart.TaskPartList) {
							wItemPart.UniqueID = wFlag++;
							wItemPart.PartOrder = wAPSTaskPart.PartOrder;

							// 为冲突消息设置唯一标识
							if (wMessageList == null || wMessageList.size() <= 0)
								continue;
							Optional<APSMessage> wMessageOption = wMessageList.stream()
									.filter(p -> p.LineID == wItemPart.LineID && p.PartID == wItemPart.PartID
											&& p.OrderID == wItemPart.OrderID)
									.findFirst();
							if (wMessageOption.isPresent()) {
								APSMessage wMessage = wMessageOption.get();
								wMessage.UniqueID = wItemPart.UniqueID;
								wItemPart.APSMessage = wMessage.MessageText;
							}
						}
					} else {
						wLinePart.TaskPartList = new ArrayList<APSTaskPart>();
						wLinePart.TaskPartList.add(new APSTaskPart());
					}
				}
			}
			// 树形结果
			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wTreeList, null);
			// 表格数据

			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			List<FPCPart> wStationList = new ArrayList<FPCPart>();
			// 排序
			if (wWorkAreaStationList != null && wWorkAreaStationList.size() > 0) {
				wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			}
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wHeadMap.put("Station_" + String.valueOf(wItem.StationID), wOption.get().Name);
					wStationList.add(wOption.get());
				}
			}
			wTableMap.add(wHeadMap);
			wTableMap.addAll(wAPSService.ChangeToTable(wOrderList, wTaskPartList, wWorkAreaStationList).Result);
			SetResult(wResult, "TableInfoList", wTableMap);
			SetResult(wResult, "OrderColumn", wStationList);

			// 获取去重后的订单工艺路线
			List<Integer> wRouteIDList = wOrderList.stream().map(p -> p.RouteID).distinct()
					.collect(Collectors.toList());
			List<FPCRoute> wRouteList = wAPSService.ASP_QueryRouteList(wLoginUser, wRouteIDList, wRouteMap).Result;

			this.SetResult(wResult, "RouteList", wRouteList);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取表格数据
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/TableList")
	public Object TableList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			List<OMSOrder> wOrderList = CloneTool.CloneArray(wParam.get("OrderList"), OMSOrder.class);
			List<APSTaskPart> wTaskPartList = CloneTool.CloneArray(wParam.get("TaskPartList"), APSTaskPart.class);
			Integer wType = StringUtils.parseInt(wParam.get("Type"));

			// 订单排序
//			wOrderList.sort(Comparator.comparing(OMSOrder::getRealReceiveDate));

			List<Integer> wList = wOrderList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());

			OutResult<Integer> wErrorCode = new OutResult<Integer>(0);
			wOrderList = OMSOrderDAO.getInstance().SelectListByIDList(wLoginUser, wList, wErrorCode);

			wOrderList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));
			// 工位任务排序
			wTaskPartList = OrderTaskPartList(wOrderList, wTaskPartList);

			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			List<FPCPart> wStationList = new ArrayList<FPCPart>();
			List<LFSWorkAreaStation> wWorkAreaStationList = wLFSService.LFS_QueryWorkAreaStationList(wLoginUser)
					.List(LFSWorkAreaStation.class);
			List<FPCPart> wPartList = wFMCService.FPC_QueryPartList(wLoginUser, 0, 0, 0, -1).List(FPCPart.class);
			// 排序
			if (wWorkAreaStationList != null && wWorkAreaStationList.size() > 0) {
				wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			}

			// 去除不在计划内的工位
			List<APSTaskPart> wCloneTaskPartList = wTaskPartList;
			wWorkAreaStationList.removeIf(p -> !wCloneTaskPartList.stream().anyMatch(q -> q.PartID == p.StationID));

			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				Optional<FPCPart> wOption = wPartList.stream().filter(p -> p.ID == wItem.StationID).findFirst();
				if (wOption.isPresent()) {
					wHeadMap.put("Station_" + String.valueOf(wItem.StationID), wOption.get().Name);
					wStationList.add(wOption.get());
				}
			}
			wTableMap.add(wHeadMap);
			if (wType.intValue() == 1) {
				wTableMap.addAll(
						wAPSService.ChangeToTableByEndTime(wOrderList, wTaskPartList, wWorkAreaStationList).Result);
			} else {
				wTableMap.addAll(wAPSService.ChangeToTable(wOrderList, wTaskPartList, wWorkAreaStationList).Result);
			}

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
			SetResult(wResult, "TableInfoList", wTableMap);
			SetResult(wResult, "OrderColumn", wStationList);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 排序
	 */
	private List<APSTaskPart> OrderTaskPartList(List<OMSOrder> wOrderList, List<APSTaskPart> wTaskPartList) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			if (wTaskPartList == null || wTaskPartList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOrderList) {
				List<APSTaskPart> wList = wTaskPartList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				if (wList.size() > 0) {
					wResult.addAll(wList);
				}
			}
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 获取表格数据(工序排程)
	 */
	@PostMapping("/TableListStep")
	public Object TableListStep(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			List<OMSOrder> wOrderList = CloneTool.CloneArray(wParam.get("OrderList"), OMSOrder.class);
			List<APSTaskPart> wTaskPartList = CloneTool.CloneArray(wParam.get("TaskPartList"), APSTaskPart.class);
			Integer wType = StringUtils.parseInt(wParam.get("Type"));

			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();
			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			List<FPCPart> wStationList = new ArrayList<FPCPart>();

			List<FPCPartPoint> wStepList = APSConstans.GetFPCPartPointList().values().stream()
					.collect(Collectors.toList());

			List<LFSWorkAreaStation> wWorkAreaStationList = new ArrayList<LFSWorkAreaStation>();
			for (APSTaskPart wItem : wTaskPartList) {
				FPCPart wPart = new FPCPart();
				if (wStepList.stream().anyMatch(p -> p.ID == wItem.PartID)) {
					FPCPartPoint wStep = wStepList.stream().filter(p -> p.ID == wItem.PartID).findFirst().get();
					wPart.ID = wStep.ID;
					wPart.Name = wStep.Name;
				}
				wHeadMap.put("Station_" + String.valueOf(wItem.PartID), wPart.Name);
				wStationList.add(wPart);

				LFSWorkAreaStation wLFSWorkAreaStation = new LFSWorkAreaStation();
				wLFSWorkAreaStation.StationID = wPart.ID;
				wLFSWorkAreaStation.StationName = wPart.Name;
				wWorkAreaStationList.add(wLFSWorkAreaStation);
			}

			wTableMap.add(wHeadMap);
			if (wType.intValue() == 1) {
				wTableMap.addAll(
						wAPSService.ChangeToTableByEndTime(wOrderList, wTaskPartList, wWorkAreaStationList).Result);
			} else {
				wTableMap.addAll(wAPSService.ChangeToTable(wOrderList, wTaskPartList, wWorkAreaStationList).Result);
			}

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
			SetResult(wResult, "TableInfoList", wTableMap);
			SetResult(wResult, "OrderColumn", wStationList);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取表格数据
	 */
	@GetMapping("/MonthTableList")
	public Object MonthTableList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			// 查询进厂但没出厂订单
			// 查询对应订单工艺路线
			// 查询对应订单周计划
			List<OMSOrder> wOrderList = wOMSService.OMS_QueryOMSOrderList(wLoginUser, -1, -1, "", -1, -1, -1, "", "", 1,
					StringUtils.parseListArgs(OMSOrderStatus.ReceivedTelegraph.getValue(),
							OMSOrderStatus.EnterFactoryed.getValue(), OMSOrderStatus.Repairing.getValue(),
							OMSOrderStatus.FinishedWork.getValue(), OMSOrderStatus.ToOutChcek.getValue(),
							OMSOrderStatus.ToOutConfirm.getValue()))
					.getResult();

			// 移除实际进厂时间在2021-8-29 00:00:00之前的订单
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2021, 7, 29, 0, 0, 0);
			wOrderList.removeIf(p -> p.RealReceiveDate.compareTo(wBaseTime) < 0);

			wOrderList.removeIf(p -> p.Active != 1 || p.ID <= 0);
			if (wOrderList.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "无在修订单");
				return wResult;
			}

			// 订单排序
			wOrderList.sort(Comparator.comparing(OMSOrder::getERPID).thenComparing(OMSOrder::getRealReceiveDate));

			List<Integer> wOrderIDListTemp = wOrderList.stream().map(p -> p.ID).distinct().collect(Collectors.toList());

			List<APSTaskPart> wTaskPartList = wAPSService
					.APS_QueryTaskPartList(wLoginUser, APSShiftPeriod.Week.getValue(), null, wOrderIDListTemp)
					.getResult();
			// 移除未排数据
			wTaskPartList.removeIf(p -> p.Status < 2);

			// 工位任务排序
			wTaskPartList = OrderTaskPart(wTaskPartList, wOrderList);

			if (wTaskPartList.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "无修车计划");
				return wResult;
			}

			wOrderIDListTemp = wTaskPartList.stream().map(p -> p.OrderID).distinct().collect(Collectors.toList());

			List<Integer> wOrderIDList = wOrderIDListTemp;
			wOrderList.removeIf(p -> !wOrderIDList.contains(p.ID));

			if (wOrderList.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "无修车计划");
				return wResult;
			}

			List<Map<String, Object>> wTableMap = new ArrayList<Map<String, Object>>();

			List<LFSWorkAreaStation> wWorkAreaStationList = wLFSService.LFS_QueryWorkAreaStationList(wLoginUser)
					.List(LFSWorkAreaStation.class);
			if (wWorkAreaStationList != null && wWorkAreaStationList.size() > 0) {
				wWorkAreaStationList.sort(Comparator.comparing(LFSWorkAreaStation::getOrderNum));
			}
			if (wWorkAreaStationList.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "工区工位数据不存在");
				return wResult;
			}

			Map<Integer, FPCPart> wPartMap = wFMCService.FPC_QueryPartList(wLoginUser, 0, 0, 0, -1).List(FPCPart.class)
					.stream().collect(Collectors.toMap(p -> p.ID, p -> p));
			// 排序
			if (wPartMap.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "工位数据不存在");
				return wResult;
			}

			// 去除不在计划内的工位
			List<APSTaskPart> wCloneTaskPartList = wTaskPartList;
			wWorkAreaStationList.removeIf(p -> !wCloneTaskPartList.stream().anyMatch(q -> q.PartID == p.StationID));
			if (wWorkAreaStationList.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "工区工位数据对应计划不存在");
				return wResult;
			}

			Map<String, Object> wHeadMap = new HashMap<String, Object>();
			List<FPCPart> wStationList = new ArrayList<FPCPart>();
			for (LFSWorkAreaStation wItem : wWorkAreaStationList) {
				if (wPartMap.containsKey(wItem.StationID)) {
					wHeadMap.put("Station_" + wItem.StationID, wPartMap.get(wItem.StationID).Name);
					wStationList.add(wPartMap.get(wItem.StationID));
				}
			}
			if (wStationList.size() <= 0 || wHeadMap.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "修车计划无对应工位存在！");
				return wResult;
			}
			wTableMap.add(wHeadMap);

			// 处理wOrderList修改StopTime
			List<AndonTime> wAndonTimeList = wAPSService.ASP_QueryAndonTimeList(wLoginUser,
					wOrderList.stream().map(p -> p.ID).collect(Collectors.toList()));
			HandleOrderList(wLoginUser, wAndonTimeList, wOrderList);

			wTableMap.addAll(wAPSService.ChangeToTable(wOrderList, wTaskPartList, wWorkAreaStationList).Result);

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
			SetResult(wResult, "TableInfoList", wTableMap);
			SetResult(wResult, "OrderColumn", wStationList);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 按照订单的顺序排序
	 */
	private List<APSTaskPart> OrderTaskPart(List<APSTaskPart> wTaskPartList, List<OMSOrder> wOrderList) {
		List<APSTaskPart> wResult = new ArrayList<APSTaskPart>();
		try {
			if (wTaskPartList == null || wTaskPartList.size() <= 0) {
				return wResult;
			}

			for (OMSOrder wOMSOrder : wOrderList) {
				List<APSTaskPart> wList = wTaskPartList.stream().filter(p -> p.OrderID == wOMSOrder.ID)
						.collect(Collectors.toList());
				if (wList != null && wList.size() > 0) {
					wResult.addAll(wList);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 处理订单，修改停时属性
	 */
	private void HandleOrderList(BMSEmployee wLoginUser, List<AndonTime> wAndonTimeList, List<OMSOrder> wOrderList) {
		try {
			if (wOrderList == null || wOrderList.size() <= 0 || wAndonTimeList == null || wAndonTimeList.size() <= 0) {
				return;
			}

			for (OMSOrder wOMSOrder : wOrderList) {
				if (wAndonTimeList.stream().anyMatch(p -> p.OrderID == wOMSOrder.ID)) {
					wOMSOrder.StopTime = wAndonTimeList.stream().filter(p -> p.OrderID == wOMSOrder.ID).findFirst()
							.get().StopTime;
				}
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 下达任务
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/TransmitTask")
	public Object TransmitTask(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			List<APSTaskPart> wList = CloneTool.CloneArray(wParam.get("data"), APSTaskPart.class);

			if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID,
					MBSRoleTree.PlanSubmit.getValue(), 0, 0).Info(Boolean.class)) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
			}

			if (wList == null || wList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			if (wList.get(0).ShiftPeriod == APSShiftPeriod.Month.getValue()) {
				if (wList.stream().anyMatch(p -> p.Status != APSTaskStatus.Audited.getValue())) {
					return GetResult(RetCode.SERVER_CODE_ERR, "请选择已审批的数据!");
				}
			} else if (wList.get(0).ShiftPeriod == APSShiftPeriod.Week.getValue()) {
				if (wList.stream().anyMatch(p -> p.Status != APSTaskStatus.Audited.getValue())) {
					return GetResult(RetCode.SERVER_CODE_ERR, "请选择已审批的数据!");
				}
			}

			wList.forEach(p -> p.Status = APSTaskStatus.Issued.getValue());

			ServiceResult<Long> wServiceResult = new ServiceResult<Long>();
			for (APSTaskPart wItem : wList) {
				wServiceResult = wAPSService.APS_UpdateTaskPart(wLoginUser, wItem);
				if (wServiceResult == null || wServiceResult.Result <= 0)
					break;
			}
			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 查询审批任务
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/AuditTaskList")
	public Object AuditTaskList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wAPSShiftPeriod = StringUtils.parseInt(request.getParameter("APSShiftPeriod"));
			Calendar wShiftDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));
			int wTagType = StringUtils.parseInt(request.getParameter("TagType"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSSchedulingVersion>> wServiceResult = wAPSService.APS_QueryAuditTaskList(wLoginUser,
					wAPSShiftPeriod, wShiftDate, wTagType);

			if (wServiceResult.Result == null) {
				wServiceResult.Result = new ArrayList<APSSchedulingVersion>();
			}
			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 审批任务
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Audit")
	public Object Audit(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data") || !wParam.containsKey("OperateType")
					|| !wParam.containsKey("APSShiftPeriod"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			APSSchedulingVersion wAPSSchedulingVersion = CloneTool.Clone(wParam.get("data"),
					APSSchedulingVersion.class);
			int wOperateType = StringUtils.parseInt(wParam.get("OperateType"));
			int wAPSShiftPeriod = StringUtils.parseInt(wParam.get("APSShiftPeriod"));

			if (wAPSSchedulingVersion == null || wAPSSchedulingVersion.ID <= 0)
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_AuditOperate(wLoginUser, wAPSSchedulingVersion,
					wOperateType, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取月计划内车型集合
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/PlanProductList")
	public Object PlanProductList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<FPCProduct>> wServiceResult = wAPSService.APS_PlanProductList(wLoginUser,
					APSShiftPeriod.Month.getValue());

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 查询今日待完工工位任务
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/TodayToDo")
	public Object ToDayToDo(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_QueryTodayToDoTaskPartList(wLoginUser);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", new ArrayList<Integer>(), wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取工作时间点
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/WorkHours")
	public Object WorkHours(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<APSWorkHour> wServiceResult = wAPSService.APS_QueryWorkHour(wLoginUser);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);
				this.SetResult(wResult, "MaxWorkHour", wServiceResult.Result.MaxWorkHour);
				this.SetResult(wResult, "MiddleWorkHour", wServiceResult.Result.MiddleWorkHour);
				this.SetResult(wResult, "MinWorkHour", wServiceResult.Result.MinWorkHour);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 保存工作时间点
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/SaveWorkHour")
	public Object SaveWorkHour(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);
			// 获取参数
			int wMaxWorkHour = StringUtils.parseInt(wParam.get("MaxWorkHour"));
			int wMinWorkHour = StringUtils.parseInt(wParam.get("MinWorkHour"));
			int wMiddleWorkHour = StringUtils.parseInt(wParam.get("MiddleWorkHour"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_SaveWorkHour(wLoginUser, wMinWorkHour,
					wMiddleWorkHour, wMaxWorkHour);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 查询台位的车当前的状态
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/PartNoStatus")
	public Object PartNoStatus(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			List<FMCWorkspace> wList = CloneTool.CloneArray(wParam.get("data"), FMCWorkspace.class);
			if (wList == null || wList.size() <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			// 获取参数
//			ServiceResult<List<FMCWorkspace>> wServiceResult = wAPSService.APS_QueryPartNoStatus(wLoginUser, wList);
			ServiceResult<List<FMCWorkspace>> wServiceResult = wAPSService.APS_QueryPartNoStatusNew(wLoginUser, wList);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	@PostMapping({ "/CompareList" })
	public Object CompareList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			List<Integer> wOrderIDList = CloneTool.CloneArray(wParam.get("data"), Integer.class);

			ServiceResult<List<APSTaskPart>> wServiceResult = this.wAPSService.APS_QueryCompareList(wLoginUser,
					wOrderIDList);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result,
						wServiceResult.CustomResult.get("LineID"));
				SetResult(wResult, "TreeList", wServiceResult.CustomResult.get("TreeList"));
				SetResult(wResult, "StartTime", wServiceResult.CustomResult.get("StartTime"));
				SetResult(wResult, "EndTime", wServiceResult.CustomResult.get("EndTime"));
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 计算开始时间和结束时间之间的间隔小时
	 * 
	 * @param wStartTime 开始时间
	 * @param wEndTime   结束时间
	 * @return 间隔小时
	 */
	public double CalIntervalHour(Calendar wStartTime, Calendar wEndTime) {
		double wResult = 0.0;
		try {
			Calendar wBaseTime = Calendar.getInstance();
			wBaseTime.set(2000, 0, 1);
			if (wStartTime == null) {
				wStartTime = wBaseTime;
			}
			if (wEndTime == null) {
				wEndTime = wBaseTime;
			}
			if (wStartTime.compareTo(wEndTime) >= 0) {
				return wResult;
			}

			long wStart = wStartTime.getTime().getTime();
			long wEnd = wEndTime.getTime().getTime();
			long wMinutes = (wEnd - wStart) / (1000 * 60);
			double wHour = (double) wMinutes / 60;
			wResult = formatDouble(wHour);
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 四舍五入保留两位小数
	 * 
	 * @param wNumber
	 * @return
	 */
	public double formatDouble(double wNumber) {
		double wResult = 0.0;
		try {
			// 如果不需要四舍五入，可以使用RoundingMode.DOWN
			BigDecimal wBG = new BigDecimal(wNumber).setScale(2, RoundingMode.UP);
			wResult = wBG.doubleValue();
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
		return wResult;
	}

	/**
	 * 导出计划
	 * 
	 * @param request
	 * @param response
	 */
	@ResponseBody
	@GetMapping("/Export")
	public Object Export(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			String wVersionNo = StringUtils.parseString(request.getParameter("VersionNo"));
			String wSuffix = StringUtils.parseString(request.getParameter("Suffix"));
			int wTaskPartID = StringUtils.parseInt(request.getParameter("TaskPartID"));
			int wAPSShiftPeriod = StringUtils.parseInt(request.getParameter("APSShiftPeriod"));

			ServiceResult<String> wServiceResult = wAPSService.Export(wLoginUser, wVersionNo, wSuffix, wTaskPartID,
					wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "提示：导出成功!", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.getFaultCode());
			}
		} catch (Exception e) {
			wResult = GetResult(RetCode.SERVER_CODE_ERR, e.toString());
			logger.error(e.toString());
		}
		return wResult;
	}

	/**
	 * 查询周计划或月计划记录
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/Query")
	public Object Query(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wAPSShiftPeriod = StringUtils.parseInt(request.getParameter("APSShiftPeriod"));

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryTaskPartList(wLoginUser, wOrderID,
					wStartTime, wEndTime, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 多辆车的月计划和周计划记录查询
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/QueryMore")
	public Object QueryMore(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<Integer> wOrderIDList = CloneTool.CloneArray(wParam.get("OrderIDList"), Integer.class);
			Calendar wStartTime = StringUtils.parseCalendar(wParam.get("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(wParam.get("EndTime"));
			int wAPSShiftPeriod = StringUtils.parseInt(wParam.get("APSShiftPeriod"));

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryMoreTaskPartList(wLoginUser,
					wOrderIDList, wStartTime, wEndTime, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 通过年月查询月计划
	 */
	@GetMapping("/MonthPlanByMonth")
	public Object MonthPlanByMonth(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ①获取参数
			int wYear = StringUtils.parseInt(request.getParameter("Year"));
			int wMonth = StringUtils.parseInt(request.getParameter("Month"));

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryMonthPlanByMonth(wLoginUser, wYear,
					wMonth);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "OMSOrderList", wServiceResult.CustomResult.get("OMSOrderList"));
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 获取周计划，，
	 */
	@GetMapping("/WeekPlanByWeek")
	public Object WeekPlanByWeek(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// ①获取参数
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<APSTaskPart>> wServiceResult = wAPSService.APS_QueryWeekPlanByWeek(wLoginUser,
					wStartTime, wEndTime);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "OMSOrderList", wServiceResult.CustomResult.get("OMSOrderList"));
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 激活已下达的周计划
	 */
	@GetMapping("/ActiveTaskPart")
	public Object ActiveTaskPart(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wAPSSchedulingVersionBPMID = StringUtils.parseInt(request.getParameter("APSSchedulingVersionBPMID"));

			if (wAPSSchedulingVersionBPMID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：参数错误!");
			}

			ServiceResult<Integer> wServiceResult = wAPSService.APS_ActiveTaskPart(wLoginUser,
					wAPSSchedulingVersionBPMID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wServiceResult.FaultCode);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}
}
