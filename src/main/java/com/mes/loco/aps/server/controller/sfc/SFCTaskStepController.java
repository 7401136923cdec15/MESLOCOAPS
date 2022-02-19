package com.mes.loco.aps.server.controller.sfc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.mes.loco.aps.server.service.CoreService;
import com.mes.loco.aps.server.service.SFCService;
import com.mes.loco.aps.server.service.po.ServiceResult;
import com.mes.loco.aps.server.service.po.aps.APSTaskStep;
import com.mes.loco.aps.server.service.po.bms.BMSEmployee;
import com.mes.loco.aps.server.service.po.ipt.IPTPreCheckProblem;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStep;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepCar;
import com.mes.loco.aps.server.service.po.sfc.SFCTaskStepPart;
import com.mes.loco.aps.server.service.utils.CloneTool;
import com.mes.loco.aps.server.service.utils.StringUtils;
import com.mes.loco.aps.server.utils.RetCode;

/**
 * 派工控制器
 * 
 * @author PengYouWang
 * @CreateTime 2019年12月26日22:46:00
 * @LastEditTime 2020-1-9 10:39:12
 */
@RestController
@RequestMapping("/api/SFCTaskStep")
public class SFCTaskStepController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(SFCTaskStepController.class);

	@Autowired
	SFCService wSFCService;

	@Autowired
	CoreService wCoreService;

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

			BMSEmployee wLoginUser = this.GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));

			ServiceResult<SFCTaskStep> wServiceResult = wSFCService.SFC_QueryTaskStep(wLoginUser, wID);

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
	 * 条件查询派工记录
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

			BMSEmployee wLoginUser = this.GetSession(request);

			int wID = StringUtils.parseInt(request.getParameter("ID"));
			int wTaskStepID = StringUtils.parseInt(request.getParameter("TaskStepID"));
			int wShiftID = StringUtils.parseInt(request.getParameter("ShiftID"));
			int wOperatorID = StringUtils.parseInt(request.getParameter("OperatorID"));

			ServiceResult<List<SFCTaskStep>> wServiceResult = wSFCService.SFC_QueryTaskStepList(wLoginUser, wID,
					wTaskStepID, wShiftID, wOperatorID, -1);

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
	 * 删除派工记录
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/DeleteAll")
	public Object DeleteAll(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			List<SFCTaskStep> wList = CloneTool.CloneArray(wParam.get("data"), SFCTaskStep.class);
			ServiceResult<Integer> wServiceResult = wSFCService.SFC_DeleteList(wLoginUser, wList);

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
	 * 班组长查询某天的派工记录
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/MonitorRecord")
	public Object MonitorRecord(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wShiftDate = StringUtils.parseCalendar(request.getParameter("ShiftDate"));

			ServiceResult<List<SFCTaskStep>> wServiceResult = wSFCService.SFC_QueryTaskStepListByMonitorID(wLoginUser,
					wShiftDate);

			if (wServiceResult.Result == null)
				wServiceResult.Result = new ArrayList<SFCTaskStep>();

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
	 * 班组成员查询某天的派工任务(打卡的数据源)
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/EmployeeAll")
	public Object EmployeeAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			Calendar wStartTime = StringUtils.parseCalendar(request.getParameter("StartTime"));
			Calendar wEndTime = StringUtils.parseCalendar(request.getParameter("EndTime"));
			int wTagTypes = StringUtils.parseInt(request.getParameter("TagTypes"));

			ServiceResult<List<SFCTaskStep>> wServiceResult = wSFCService.SFC_QueryTaskStepListByEmployeeID(wLoginUser,
					wStartTime, wEndTime, wTagTypes);

			if (wServiceResult.Result == null) {
				wServiceResult.Result = new ArrayList<SFCTaskStep>();
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

			BMSEmployee wLoginUser = this.GetSession(request);

			if (!wParam.containsKey("data"))
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			SFCTaskStep wSFCTaskStep = CloneTool.Clone(wParam.get("data"), SFCTaskStep.class);
			if (wSFCTaskStep == null)
				wResult = GetResult(RetCode.SERVER_CODE_ERR, "参数错误!");

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_UpdateTaskStep(wLoginUser, wSFCTaskStep);

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
	 * 查询派工人员列表
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/PGEmployeeList")
	public Object PGEmployeeList(HttpServletRequest request) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = this.GetSession(request);

			int wAPSTaskStepID = StringUtils.parseInt(request.getParameter("APSTaskStepID"));

			ServiceResult<List<BMSEmployee>> wServiceResult = wSFCService.SFC_QueryPGEmployeeList(wLoginUser,
					wAPSTaskStepID);

			if (StringUtils.isEmpty(wServiceResult.getFaultCode())) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "", wServiceResult.Result, null);
				ServiceResult<List<BMSEmployee>> wSourceSR = wSFCService.SFC_QueryDispatchEmployeeList(wLoginUser,
						false, new ArrayList<Integer>(Arrays.asList(wAPSTaskStepID)));
				this.SetResult(wResult, "SourceList", wSourceSR.Result);
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
	 * 一次性提交派工人员
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/SubmitAll")
	public Object SubmitAll(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 【派工、全派工】权限控制
			if (!wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 501001, 0, 0)
					.Info(Boolean.class)
					&& !wCoreService.BMS_CheckPowerByAuthorityID(wLoginUser.CompanyID, wLoginUser.ID, 501002, 0, 0)
							.Info(Boolean.class)) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
			}

			if (!(wParam.containsKey("data"))) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			List<APSTaskStep> wAPSTaskStepList = CloneTool.CloneArray(wParam.get("data"), APSTaskStep.class);

//			ServiceResult<Integer> wServiceResult = wSFCService.SFC_TaskStepSubmitAll(wLoginUser, wAPSTaskStepList);

//			ServiceResult<Integer> wServiceResult = wSFCService.SFC_TaskStepSubmitAllNew(wLoginUser, wAPSTaskStepList);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_TaskStepSubmitAllNew_v2(wLoginUser,
					wAPSTaskStepList);

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
	 * 添加派工人员(开工状态下)
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/AddPerson")
	public Object AddPerson(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			APSTaskStep wData = CloneTool.Clone(wParam.get("data"), APSTaskStep.class);
			List<Integer> wPersonIDList = CloneTool.CloneArray(wParam.get("PersonIDList"), Integer.class);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_AddPerson(wLoginUser, wData, wPersonIDList);

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
	 * 保存派工人员
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/SaveAll")
	public Object SaveAll(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Object wResult = new Object();
		try {
			if (this.CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			if (!(wParam.containsKey("data"))) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			List<APSTaskStep> wAPSTaskStepList = CloneTool.CloneArray(wParam.get("data"), APSTaskStep.class);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_TaskStepSaveAll(wLoginUser, wAPSTaskStepList);
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
	 * 班组长调整派工任务的工时
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

			// 获取参数
			List<SFCTaskStep> wSFCTaskStepList = CloneTool.CloneArray(wParam.get("SFCTaskStepList"), SFCTaskStep.class);
			Double wHour = StringUtils.parseDouble(wParam.get("Hour"));

			if (wSFCTaskStepList == null || wSFCTaskStepList.size() <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			if (!wSFCTaskStepList.stream().allMatch(p -> p.MonitorID == wLoginUser.ID)) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_CODE_UNROLE);
			}

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_AdjustHour(wLoginUser, wSFCTaskStepList, wHour);

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
	 * 问题项派工
	 * 
	 * @param request
	 * @param wParam
	 * @return
	 */
	@PostMapping("/SubmitPGProblems")
	public Object SubmitPGProblems(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<String, Object>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			List<IPTPreCheckProblem> wDataList = CloneTool.CloneArray(wParam.get("data"), IPTPreCheckProblem.class);
			if (wDataList == null || wDataList.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_SubmitPGProblems(wLoginUser, wDataList);

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

	@PostMapping({ "/TaskAllByTaskStepIDList" })
	public Object TaskAllByTaskStepIDList(HttpServletRequest request, @RequestBody Map<String, Object> wParam) {
		Map<String, Object> wResult = new HashMap<>();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			List<Integer> wTaskStepIDList = CloneTool.CloneArray(wParam.get("data"), Integer.class);
			if (wTaskStepIDList == null || wTaskStepIDList.size() <= 0) {
				wResult = GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
				return wResult;
			}

			ServiceResult<List<SFCTaskStep>> wServiceResult = this.wSFCService.SFC_QueryTaskStepList(wLoginUser,
					wTaskStepIDList);

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
	 * 删除重复的派工记录
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping("/DeleteRepeat")
	public Object PreCheckTaskAll(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<Integer> wServiceResult = wSFCService.SFC_DeleteRepeat(wLoginUser);

			if (StringUtils.isEmpty(wServiceResult.FaultCode)) {
				wResult = GetResult(RetCode.SERVER_CODE_SUC, "删除成功", null, wServiceResult.Result);
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
	 * 获取派工车辆列表
	 */
	@GetMapping("/DispatchCarList")
	public Object DispatchCarList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			ServiceResult<List<SFCTaskStepCar>> wServiceResult = wSFCService.SFC_QueryDispatchCarList(wLoginUser);

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
	 * 获取派工工位列表
	 */
	@GetMapping("/DispatchPartList")
	public Object DispatchPartList(HttpServletRequest request) {
		Object wResult = new Object();
		try {
			if (CheckCookieEmpty(request)) {
				wResult = GetResult(RetCode.SERVER_CODE_UNLOGIN, "");
				return wResult;
			}

			BMSEmployee wLoginUser = GetSession(request);

			// 获取参数
			int wOrderID = StringUtils.parseInt(request.getParameter("OrderID"));

			if (wOrderID <= 0) {
				return GetResult(RetCode.SERVER_CODE_ERR, RetCode.SERVER_RST_ERROR_OUT);
			}

			ServiceResult<List<SFCTaskStepPart>> wServiceResult = wSFCService.SFC_QueryDispatchPartList(wLoginUser,
					wOrderID);

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
