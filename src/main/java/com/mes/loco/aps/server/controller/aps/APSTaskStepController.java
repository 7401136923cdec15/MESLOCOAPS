package com.mes.loco.aps.server.controller.aps;

import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RestController;

import com.mes.loco.aps.server.controller.BaseController;
import com.mes.loco.aps.server.service.APSService;
import com.mes.loco.aps.server.service.CoreService;
import com.mes.loco.aps.server.service.MyHelperService;
import com.mes.loco.aps.server.service.SFCService;
import com.mes.loco.aps.server.service.mesenum.APSTaskStatus;
import com.mes.loco.aps.server.service.mesenum.MBSRoleTree;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSDayPlanAudit;
import com.mes.loco.aps.server.service.po.aps.APSPartNoDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskPart;
import com.mes.loco.aps.server.service.po.aps.APSTaskPartDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepDetails;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepInfo;
import com.mes.loco.aps.server.service.po.aps.APSTaskStepPlan;
import com.mes.loco.aps.server.service.po.aps.APSWorkAreaDetails;
import com.mes.loco.aps.server.service.po.bms.BMSDepartment;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.fpc.FPCPart;
import com.mes.loco.aps.server.service.po.fpc.FPCPartPoint;
import com.mes.loco.aps.server.service.po.oms.OMSOrder;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStep;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepInfo;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 
 * @author PengYouWang
 * @CreateTime 2020-1-8 13:40:24
 * @LastEditTime 2020-1-11 11:52:50
 */
@RestController
@RequestMapping("/api/APSTaskStep")
public class APSTaskStepController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(APSTaskStepController.class);

	@Autowired
	APSService wAPSService;

	@Autowired
	SFCService wSFCService;

	@Autowired
	CoreService wCoreService;

	@Autowired
	MyHelperService wMyHelperService;

	/**
	 * 日计划审批单
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/AuditInfo")
	public Object AuditInfo(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wID = StringUtils.parseInt(request.getParameter("ID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<APSDayPlanAudit> wServiceResult = wAPSService.APS_QueryDayPlanAudit(wLoginUser, wID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

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

			ServiceResult<APSTaskStep> wServiceResult = wAPSService.APS_QueryTaskStep(wLoginUser, wID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wServiceResult.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 根据工位计划ID查询工序计划集合
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/All")
	public Object All(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wAPSTaskPartID = StringUtils.parseInt(request.getParameter("APSTaskPartID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepList(wLoginUser, -1, -1, -1,
					wAPSTaskPartID, -1, -1, -1, -1, -1, 1, null);
			ServiceResult<APSTaskPart> wTaskPart = wAPSService.APS_QueryTaskPart(wLoginUser, wAPSTaskPartID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, wTaskPart.Result);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 条件查询所有工序计划
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/AllList")
	public Object AllList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wAPSTaskPartID = StringUtils.parseInt(wParam.get("APSTaskPartID"));
			int wOrderID = StringUtils.parseInt(wParam.get("OrderID"));
			int wStationID = StringUtils.parseInt(wParam.get("StationID"));
			List<Integer> wStateIDList = CloneTool.CloneArray(wParam.get("StateIDList"), Integer.class);

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepList(wLoginUser, -1,
					wOrderID, -1, wAPSTaskPartID, -1, -1, wStationID, -1, -1, 1, wStateIDList);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 根据工位计划ID查询报表工序计划集合
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/RFTaskList")
	public Object RFTaskList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			int wAPSTaskPartID = StringUtils.parseInt(request.getParameter("APSTaskPartID"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepList(wLoginUser, -1, -1, -1,
					wAPSTaskPartID, -1, -1, -1, -1, -1, 1, null);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
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
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			APSTaskStep wAPSTaskStep = CloneTool.Clone(wParam.get("data"), APSTaskStep.class);
			if (wAPSTaskStep == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_UpdateTaskStep(wLoginUser, wAPSTaskStep);

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
	 * 新增或更新
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/SaveList")
	public Object SaveList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");
			}

			List<APSTaskStep> wList = CloneTool.CloneArray(wParam.get("data"), APSTaskStep.class);
			if (wList == null) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");
			}

			ServiceResult<Integer> wServiceResult = new ServiceResult<Integer>();
			for (APSTaskStep wItem : wList) {
				wItem.ProductNo = (wItem.PartNo.split("#"))[0];
				// 下达时刻
				if (wItem.Status == APSTaskStatus.Issued.getValue())
					wItem.ReadyTime = Calendar.getInstance();
				wServiceResult = wAPSService.APS_UpdateTaskStep(wLoginUser, wItem);
				if (wServiceResult.Result <= 0)
					break;
			}

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
	 * 车号分类日计划查询
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/PartNoDetails")
	public Object PartNoDetails(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wWorkAreaID = StringUtils.parseInt(request.getParameter("WorkAreaID"));

//			ServiceResult<List<APSPartNoDetails>> wServiceResult = wAPSService.APS_QueryPartNoDetails(wLoginUser,
//					wStartTime, wEndTime);
			ServiceResult<List<APSPartNoDetails>> wServiceResult = wAPSService.APS_QueryPartNoDetailsNew(wLoginUser,
					wStartTime, wEndTime, wWorkAreaID);

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
	 * 工区分类日计划查询
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/AreaDetails")
	public Object AreaDetails(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wWorkAreaID = StringUtils.parseInt(request.getParameter("WorkAreaID"));

//			ServiceResult<List<APSWorkAreaDetails>> wServiceResult = wAPSService.APS_QueryAreaDetails(wLoginUser,
//					wStartTime, wEndTime, wOrderID);

			ServiceResult<List<APSWorkAreaDetails>> wServiceResult = wAPSService.APS_QueryAreaDetailsNew(wLoginUser,
					wStartTime, wEndTime, wOrderID, wWorkAreaID);

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
	 * 根据时间段查询日计划
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/Query")
	public Object Query(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wAreaID = StringUtils.parseInt(request.getParameter("AreaID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

//			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepByTime(wLoginUser,
//					wStartTime, wEndTime, wIsAudit);

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepByTimeNew(wLoginUser,
					wStartTime, wEndTime, wAreaID, wOrderID);

			if (wServiceResult.Result == null) {
				wServiceResult.Result = new ArrayList<APSTaskStep>();
			}

			if (wServiceResult.Result.size() > 0 && wOrderID > 0) {
				wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.OrderID == wOrderID)
						.collect(Collectors.toList());
			}

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "OrderList", wServiceResult.CustomResult.get("OrderList"));

				List<APSTaskPartDetails> wDetails = (List<APSTaskPartDetails>) wServiceResult.CustomResult
						.get("TaskPartList");
				if (wOrderID > 0) {
					wDetails = wDetails.stream().filter(p -> p.OrderID == wOrderID).collect(Collectors.toList());
				}
				if (wAreaID > 0) {
					wDetails = wDetails.stream().filter(p -> p.AreaID == wAreaID).collect(Collectors.toList());
				}

				this.SetResult(wResult, "TaskPartList", wDetails);
				if (wServiceResult.CustomResult.containsKey("ToDoList")) {
					this.SetResult(wResult, "ToDoList", wServiceResult.CustomResult.get("ToDoList"));
				}
				if (wServiceResult.CustomResult.containsKey("DoneList")) {
					this.SetResult(wResult, "DoneList", wServiceResult.CustomResult.get("DoneList"));
				}
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
	 * 日计划查询 通过时间 工区 修程 工位查询日计划
	 */
	@SuppressWarnings("unchecked")
	@GetMapping("/QueryAll")
	public Object QueryAll(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wAreaID = StringUtils.parseInt(request.getParameter("AreaID"));
			int wLineID = StringUtils.parseInt(request.getParameter("LineID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepByConditions(wLoginUser,
					wStartTime, wEndTime, wAreaID, wLineID, wPartID);

			if (wOrderID > 0) {
				wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.OrderID == wOrderID)
						.collect(Collectors.toList());
			}

			if (wServiceResult.Result == null)
				wServiceResult.Result = new ArrayList<APSTaskStep>();

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);

				List<OMSOrder> wOrderList = (List<OMSOrder>) wServiceResult.CustomResult.get("OrderList");
				if (wOrderID > 0) {
					wOrderList = wOrderList.stream().filter(p -> p.ID == wOrderID).collect(Collectors.toList());
				}
				List<APSTaskPartDetails> wDetails = (List<APSTaskPartDetails>) wServiceResult.CustomResult
						.get("TaskPartList");
				if (wOrderID > 0) {
					wDetails = wDetails.stream().filter(p -> p.OrderID == wOrderID).collect(Collectors.toList());
				}

				this.SetResult(wResult, "OrderList", wOrderList);
				this.SetResult(wResult, "TaskPartList", wDetails);
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
	 * 生成日计划
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/Create")
	public Object Create(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data")) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");
				return wResult;
			}

			Calendar wShiftDate = CloneTool.Clone(wParam.get("data"), Calendar.class);
			if (wShiftDate == null) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");
				return wResult;
			}

			ServiceResult<String> wTipResult = wAPSService.APS_CheckIsAreaLeader(wLoginUser);
			if (StringUtils.isNotEmpty(wTipResult.Result)) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, wTipResult.Result);
				return wResult;
			}

//			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_CreateTaskStepByShiftDate(wLoginUser,
//					wShiftDate);

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_CreateTaskStepByShiftDateNew(wLoginUser,
					wShiftDate);

			if (wServiceResult.Result == null)
				wServiceResult.Result = new ArrayList<APSTaskStep>();

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "OrderList", wServiceResult.CustomResult.get("OrderList"));
				this.SetResult(wResult, "TaskPartList", wServiceResult.CustomResult.get("TaskPartList"));
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
	 * 查班长的工序任务(派工用)
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/MonitorTaskList")
	public Object MonitorTaskList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wTaskDate = StringUtils.parseCalendar(request.getParameter("TaskDate"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			String wPartNo = StringUtils.parseString(request.getParameter("PartNo"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepListByOrder(wLoginUser,
					wTaskDate, wOrderID, wPartID, wPartNo);

//			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepListByMonitorID(wLoginUser,
//					wTaskDate, wLoginUser.ID);

//			if (wServiceResult.Result != null && wServiceResult.Result.size() > 0) {
//				if (wPartID > 0) {
//					wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.PartID == wPartID)
//							.collect(Collectors.toList());
//				}
//				if (wOrderID > 0) {
//					wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.OrderID == wOrderID)
//							.collect(Collectors.toList());
//				}
//				if (StringUtils.isNotEmpty(wPartNo)) {
//					wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.PartNo.equals(wPartNo))
//							.collect(Collectors.toList());
//				}
//			}

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
//				// 人员列表
//				List<BMSEmployee> wEmployeeList = wCoreService.BMS_GetEmployeeAll(wLoginUser, 0, 0, 1)
//						.List(BMSEmployee.class);
//				// 赋值派工人员
//				if (wServiceResult.Result == null || wServiceResult.Result.size() <= 0)
//					return wResult;
//				AssignNames(wLoginUser, wEmployeeList, wServiceResult);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	private void AssignNames(BMSEmployee wLoginUser, List<BMSEmployee> wEmployeeList,
			ServiceResult<List<APSTaskStep>> wServiceResult) {
		try {
			for (APSTaskStep wAPSTaskStep : wServiceResult.Result) {
				ServiceResult<List<SFCTaskStep>> wSFCTaskStepList = wSFCService.SFC_QueryTaskStepList(wLoginUser, -1,
						wAPSTaskStep.ID, -1, -1, -1);
				if (wSFCTaskStepList == null || wSFCTaskStepList.Result == null || wSFCTaskStepList.Result.size() <= 0)
					continue;
				List<String> wNames = new ArrayList<String>();
				for (SFCTaskStep wSFCTaskStep : wSFCTaskStepList.Result) {
					Optional<BMSEmployee> wEmpOption = wEmployeeList.stream()
							.filter(p -> p.ID == wSFCTaskStep.OperatorID).findFirst();
					if (wEmpOption.isPresent()) {
						wNames.add(wEmpOption.get().Name);
					}
				}
				if (wNames.size() > 0)
					wAPSTaskStep.Operators = StringUtils.Join(",", wNames);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * 车号列表、工序列表
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/CarStepList")
	public Object CarStepList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wTaskDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryTaskStepListByMonitorID(wLoginUser,
					wTaskDate, wLoginUser.ID);

			// 车号列表
			List<String> wCarNoList = new ArrayList<String>();
			if (wServiceResult.Result != null && wServiceResult.Result.size() > 0) {
				for (APSTaskStep wStep : wServiceResult.Result) {
					if (!wCarNoList.stream().anyMatch(p -> p.equals(wStep.PartNo)))
						wCarNoList.add(wStep.PartNo);
				}
			}

			// 工序列表
			List<FPCPartPoint> wPartPointList = wCoreService.FPC_QueryPartPointList(wLoginUser)
					.List(FPCPartPoint.class);
			List<FPCPartPoint> wRstPointList = new ArrayList<FPCPartPoint>();
			if (wServiceResult.Result != null && wServiceResult.Result.size() > 0) {
				for (APSTaskStep wStep : wServiceResult.Result) {
					Optional<FPCPartPoint> wTOption = wPartPointList.stream().filter(p -> p.ID == wStep.StepID)
							.findFirst();
					if (wTOption.isPresent()) {
						wRstPointList.add(wTOption.get());
					}
				}
			}

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, null);

				// 去重
				if (wRstPointList.size() > 0) {
					wRstPointList = new ArrayList<FPCPartPoint>(wRstPointList.stream()
							.collect(Collectors.toMap(FPCPartPoint::getID, account -> account, (k1, k2) -> k2))
							.values());
				}

				SetResult(wResult, "PartNoList", wCarNoList);
				SetResult(wResult, "StepList", wRstPointList);

			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 判断登陆者是否有全派工权限
	 */
	@GetMapping("/IsDispatchPower")
	public Object IsDispatchPower(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			boolean wResultFlag = false;
			// 获取参数
			if (wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 501002, 0, 0)
					.Info(Boolean.class)) {
				wResultFlag = true;
			}

			wResult = GetResult(RetCode.SERVER_CODE_SUC, "", null, wResultFlag);
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 派工数据源
	 */
	@GetMapping("/CarStationList")
	public Object CarStationList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wTaskDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));
			int wWorkAreaID = StringUtils.parseInt(request.getParameter("WorkAreaID"));

			ServiceResult<List<SFCTaskStepInfo>> wServiceResult = wAPSService.APS_GetCarStationList(wLoginUser,
					wTaskDate, wWorkAreaID);

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
	 * 查询工序任务记录(派工记录)
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/TaskStepRecord")
	public Object TaskStepRecord(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));

			ServiceResult<List<APSTaskStep>> wServiceResult = this.wAPSService.APS_QueryTaskStepListByTime(wLoginUser,
					wStartTime, wEndTime);

			List<SFCTaskStepInfo> wSFCTaskStepInfoList = new ArrayList<>();

			if (wServiceResult.Result != null && (wServiceResult.Result).size() > 0) {
				List<APSTaskStep> wDistinctList = new ArrayList<APSTaskStep>(wServiceResult.Result.stream()
						.collect(Collectors.toMap(APSTaskStep::getID, account -> account, (k1, k2) -> k2)).values());

				SFCTaskStepInfo wSFCTaskStepInfo = null;
				for (APSTaskStep wAPSTaskStep : wDistinctList) {
					wSFCTaskStepInfo = new SFCTaskStepInfo();
					wSFCTaskStepInfo.OrderID = wAPSTaskStep.OrderID;
					wSFCTaskStepInfo.OrderNo = wAPSTaskStep.OrderNo;
					wSFCTaskStepInfo.PartNo = wAPSTaskStep.PartNo;
					wSFCTaskStepInfo.StationID = wAPSTaskStep.PartID;
					wSFCTaskStepInfo.StationName = wAPSTaskStep.PartName;
					wSFCTaskStepInfo.ToDispatch = (int) wServiceResult.Result.stream()
							.filter(p -> (p.OrderID == wAPSTaskStep.OrderID && p.PartID == wAPSTaskStep.PartID
									&& !p.IsDispatched))
							.count();
					wSFCTaskStepInfo.Dispatched = (int) wServiceResult.Result.stream()
							.filter(p -> (p.OrderID == wAPSTaskStep.OrderID && p.PartID == wAPSTaskStep.PartID
									&& p.IsDispatched))
							.count();
					if (!wSFCTaskStepInfoList.stream()
							.anyMatch(p -> p.OrderID == wAPSTaskStep.OrderID && p.StationID == wAPSTaskStep.PartID)) {
						wSFCTaskStepInfoList.add(wSFCTaskStepInfo);
					}
				}
			}

			// 去除已派工数为0的数据
			if (wSFCTaskStepInfoList.size() > 0) {
				wSFCTaskStepInfoList.removeIf(p -> p.Dispatched == 0);
			}

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wSFCTaskStepInfoList, null);
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
	 * 条件查询记录详情
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/RecordInfo")
	public Object RecordInfo(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			String wPartNo = StringUtils.parseString(request.getParameter("PartNo"));
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			ServiceResult<List<APSTaskStep>> wServiceResult = this.wAPSService.APS_QueryTaskStepListByTime(wLoginUser,
					wStartTime, wEndTime);

			if (wServiceResult.Result != null && wServiceResult.Result.size() > 0) {
				if (wPartID > 0) {
					wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.PartID == wPartID)
							.collect(Collectors.toList());
				}
				if (wOrderID > 0) {
					wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.OrderID == wOrderID)
							.collect(Collectors.toList());
				}
				if (StringUtils.isNotEmpty(wPartNo)) {
					wServiceResult.Result = wServiceResult.Result.stream().filter(p -> p.PartNo.equals(wPartNo))
							.collect(Collectors.toList());
				}
			}

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				// 人员列表
				List<BMSEmployee> wEmployeeList = wCoreService.BMS_GetEmployeeAll(wLoginUser, 0, 0, 1)
						.List(BMSEmployee.class);
				// 赋值派工人员
				if (wServiceResult.Result == null || wServiceResult.Result.size() <= 0)
					return wResult;
				AssignNames(wLoginUser, wEmployeeList, wServiceResult);
			} else {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_NULL);
			}
		} catch (Exception ex) {
			logger.error(ex.toString());
			wResult = GetResult(RetCode.SERVER_CODE_ERR, ex.toString(), null, null);
		}
		return wResult;
	}

	/**
	 * 工艺员调整工序任务的工时
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/AdjustHour")
	public Object AdjustHour(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 判断权限
			if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID,
					MBSRoleTree.AdjustHour.getValue(), 0, 0).Info(Boolean.class)) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
			}

			// 获取参数
			List<APSTaskStep> wAPSTaskStepList = CloneTool.CloneArray(wParam.get("APSTaskStepList"), APSTaskStep.class);
			Double wHour = StringUtils.parseDouble(wParam.get("Hour"));
			ServiceResult<Integer> wServiceResult = wAPSService.APS_AdjustHour(wLoginUser, wAPSTaskStepList, wHour);

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
	 * 查询审批任务日计划
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

			Calendar wShiftDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));
			int wTagType = StringUtils.parseInt(request.getParameter("TagType"));

			BMSEmployee wLoginUser = this.GetSession(request);

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryAuditStepTaskList(wLoginUser,
					wTagType, wShiftDate);

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

			if (!wParam.containsKey("TaskList") || !wParam.containsKey("OperateType"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			List<APSTaskStep> wTaskList = CloneTool.CloneArray(wParam.get("TaskList"), APSTaskStep.class);
			int wOperateType = StringUtils.parseInt(wParam.get("OperateType"));

			if (wTaskList == null || wTaskList.size() <= 0)
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_AuditStepOperate(wLoginUser, wTaskList,
					wOperateType);

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
	 * 工区主管任务集合
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@GetMapping("/LeaderTask")
	public Object LeaderTask(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wShiftDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));
			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryLeaderTask(wLoginUser, wShiftDate);

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
	 * 重置工序任务到派工(内部使用)
	 */
	@PostMapping("/ReSet")
	public Object ReSet(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wAPSTaskStepID = StringUtils.parseInt(wParam.get("APSTaskStepID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_ReSetTaskStep(wLoginUser, wAPSTaskStepID);

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
	 * 删除重复日计划(内部使用)
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/Delete")
	public Object Delete(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));

			ServiceResult<Integer> wServiceResult = wAPSService.APS_Delete(wLoginUser, wOrderID, wPartID);

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
	 * 获取日计划信息
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/TaskStepInfo")
	public Object TaskStepInfo(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			Calendar wStartTime = StringUtils.parseCalendar(wParam.get("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(wParam.get("EndTime"));
			List<Integer> wClassIDList = CloneTool.CloneArray(wParam.get("ClassIDList"), Integer.class);
			List<Integer> wPartIDList = CloneTool.CloneArray(wParam.get("PartIDList"), Integer.class);
			List<Integer> wStatusList = CloneTool.CloneArray(wParam.get("StatusList"), Integer.class);
			int wIsDispatched = StringUtils.parseInt(wParam.get("IsDispached"));

			ServiceResult<List<APSTaskStepInfo>> wServiceResult = wAPSService.APS_QueryTaskStepInfo(wLoginUser,
					wStartTime, wEndTime, wClassIDList, wPartIDList, wStatusList, wIsDispatched);

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
	 * 获取登录者的班组列表
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/ClassList")
	public Object ClassList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<List<BMSDepartment>> wServiceResult = wAPSService.APS_QueryClassList(wLoginUser);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);

				this.SetResult(wResult, "IsAllClass", wServiceResult.CustomResult.get("IsAllClass"));
				this.SetResult(wResult, "IsAllPart", wServiceResult.CustomResult.get("IsAllPart"));
				this.SetResult(wResult, "PartList", wServiceResult.CustomResult.get("PartList"));
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
	 * 获取登陆者的工位列表
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/StationList")
	public Object StationList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<List<FPCPart>> wServiceResult = wAPSService.APS_QueryStationList(wLoginUser);

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
	 * 获取工序任务详情(三检进度)
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/TransInfo")
	public Object TransInfo(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wAPSTaskPartID = StringUtils.parseInt(request.getParameter("APSTaskPartID"));

			ServiceResult<List<APSTaskStepDetails>> wServiceResult = wAPSService.APS_QueryTransInfo(wLoginUser,
					wAPSTaskPartID);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result,
						wServiceResult.CustomResult.get("APSTaskPart"));
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
	 * 获取工序任务集合
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/OrderTimeInfoList")
	public Object OrderTimeInfoList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));
			String wStationName = StringUtils.parseString(request.getParameter("StationName"));

			ServiceResult<List<APSTaskStep>> wServiceResult = wAPSService.APS_QueryOrderTimeInfoList(wLoginUser,
					wOrderID, wStationName);

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
	 * 查询工序排程计划
	 */
	@GetMapping("/StepPlanList")
	public Object StepPlanList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));
			String wPartIDs = StringUtils.parseString(request.getParameter("PartIDs"));
			int wAPSShiftPeriod = StringUtils.parseInt(request.getParameter("APSShiftPeriod"));

			if (StringUtils.isEmpty(wOrderIDs) || StringUtils.isEmpty(wPartIDs)) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：参数错误，订单ID或工位计划ID不能小于或等于0!");
			}

			ServiceResult<List<APSTaskStepPlan>> wServiceResult = wAPSService.APS_QueryStepPlanList(wLoginUser,
					wOrderIDs, wPartIDs, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "TableInfoList", wServiceResult.CustomResult.get("TableInfoList"));
				this.SetResult(wResult, "OrderColumn", wServiceResult.CustomResult.get("OrderColumn"));
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
	 * 查询工序排程计划(对比)
	 */
	@GetMapping("/StepPlanCompare")
	public Object StepPlanCompare(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			String wOrderIDs = StringUtils.parseString(request.getParameter("OrderIDs"));
			String wPartIDs = StringUtils.parseString(request.getParameter("PartIDs"));
			int wAPSShiftPeriod = StringUtils.parseInt(request.getParameter("APSShiftPeriod"));

			if (StringUtils.isEmpty(wOrderIDs) || StringUtils.isEmpty(wPartIDs)) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：参数错误，订单ID或工位计划ID不能小于或等于0!");
			}

			ServiceResult<List<APSTaskStepPlan>> wServiceResult = wAPSService.APS_QueryStepPlanCompare(wLoginUser,
					wOrderIDs, wPartIDs, wAPSShiftPeriod);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				this.SetResult(wResult, "TableInfoList", wServiceResult.CustomResult.get("TableInfoList"));
				this.SetResult(wResult, "OrderColumn", wServiceResult.CustomResult.get("OrderColumn"));
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
	 * 同步工序计划
	 */
	@GetMapping("/SynchronizedStepPlan")
	public Object SynchronizedStepPlan(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wPartID = StringUtils.parseInt(request.getParameter("PartID"));
			String wOrders = StringUtils.parseString(request.getParameter("Orders"));

			if (wPartID <= 0 || StringUtils.isEmpty(wOrders)) {
				return GetResult(RetCode.SERVER_CODE_ERR, "提示：参数错误!");
			}

			ServiceResult<Integer> wServiceResult = wAPSService.APS_SynchronizedStepPlan(wLoginUser, wPartID, wOrders);

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
	 * 撤销已下达的日计划
	 */
	@PostMapping("/CancelPlan")
	public Object CancelPlan(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			List<APSTaskStep> wDataList = CloneTool.CloneArray(wParam.get("data"), APSTaskStep.class);

			ServiceResult<Integer> wServiceResult = wAPSService.APS_CancelTaskStepPlan(wLoginUser, wDataList);

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
}
